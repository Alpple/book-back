package com.book.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.beans.*;
import com.book.beans.query.BookQuery;
import com.book.beans.query.StorageQuery;
import com.book.beans.vo.BookVO;
import com.book.beans.vo.BorrowVO;
import com.book.beans.vo.StorageVO;
import com.book.config.exception.GeneralException;
import com.book.mapper.*;
import com.book.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IBookServiceImpl extends ServiceImpl<TbBookMapper, TbBook> implements IBookService {

    @Autowired
    TbTypeMapper typeMapper;
    @Autowired
    TbReaderMapper readerMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean storage(StorageQuery query) {
        TbStorage storage = query.toStorage();
        long count;
        TbBook book;
        // 插入图书，并获取ID
        if (query.getIsCreateNewBook() == Boolean.TRUE) {
              book = query.toNewBook();
            count = this.baseMapper.insert(book);
            if (count == 0) {
                throw new GeneralException("保存图书信息失败");
            }
        } else {
              book = this.baseMapper.selectById(query.getBookId());
            if (book == null) {
                throw new GeneralException("图书不存在");
            }
            book.setNumber(book.getNumber() + storage.getNumber());
            if (!this.updateById(book)) {
                throw new GeneralException("更新图书库存失败");
            }

        }
        // 更新类型数量信息。
        TbType type = typeMapper.selectById(book.getTypeId());
        type.setNumber(type.getNumber() + storage.getNumber());
        if (typeMapper.updateById(type) != 1) {
            // -1 特殊标记
            throw new GeneralException("更新图书数量失败-1");
        }

        storage.setBookId(book.getId());
        count = this.storageMapper.insert(storage);
        if (count == 0) {
            throw new GeneralException("保存入库信息失败");
        }
        return true;
    }

    @Override
    public boolean removeStorage(long id) {
        TbStorage s = this.storageMapper.selectById(id);
        if (s == null) {
            throw new GeneralException("入库记录不存在");
        }
        // 更新图书数量
        TbBook book = this.baseMapper.selectById(s.getBookId());
        // number = 10 - 6 = 4; borrow = 5; 4-5<0
        book.setNumber(book.getNumber() - s.getNumber());
        if (book.getNumber() - book.getBorrowNumber() < 0) {
            throw new GeneralException("删除此记录后，图书的数量小于当前已借出数量！");
        }
        int count = this.baseMapper.updateById(book);
        if (count == 0) {
            throw new GeneralException("更新图书库存失败");
        }
        // 更新类型数量信息。
        TbType type = typeMapper.selectById(book.getTypeId());
        type.setNumber(type.getNumber() - s.getNumber());
        if (typeMapper.updateById(type) != 1) {
            // -1 特殊标记
            throw new GeneralException("更新图书数量失败-1");
        }
        count = this.storageMapper.deleteById(id);
        if (count == 0) {
            throw new GeneralException("删除入库记录失败");
        }
        return true;
    }

    @Override
    public Page<StorageVO> storagePage(int page, int size, Long empId, Long bookId) {
        LambdaQueryWrapper<TbStorage> wrapper = Wrappers.<TbStorage>lambdaQuery()
                .orderByDesc(TbStorage::getId)
                .eq(empId != null, TbStorage::getEmpId, empId)
                .eq(bookId != null, TbStorage::getBookId, bookId);

        Page<StorageVO> pages = new Page<>(page, size);
        Page<TbStorage> ps = new Page<>(page, size);

        this.storageMapper.selectPage(ps, wrapper);

        pages.setPages(ps.getPages());
        pages.setTotal(ps.getTotal());
        pages.setCurrent(ps.getCurrent());

        List<TbStorage> list = ps.getRecords();
        // 没有 记录
        if (list.isEmpty()) {
            return pages;
        }
        List<Long> ids = list.stream().map(TbStorage::getBookId).collect(Collectors.toList());
        // 查图书
        Map<Long, TbBook> books = this.baseMapper.selectBatchIds(ids)
                .stream().collect(Collectors.toMap(TbBook::getId, Function.identity()));

        ids = list.stream().map(TbStorage::getEmpId).collect(Collectors.toList());
        Map<Long, TbEmp> emps = this.empMapper.selectBatchIds(ids)
                .stream().collect(Collectors.toMap(TbEmp::getId, Function.identity()));

        ArrayList<StorageVO> records = new ArrayList<>();
        for (TbStorage tbStorage : list) {
            records.add(
                    new StorageVO(
                            tbStorage,
                            emps.get(tbStorage.getEmpId()),
                            BookVO.create(books.get(tbStorage.getBookId()), typeMapper)
                    )
            );
        }
        pages.setRecords(records);
        return pages;
    }

    @Autowired
    TbEmpMapper empMapper;

    @Override
    public boolean removeBorrow(long borrowId) {
        TbBorrow b = this.borrowMapper.selectById(borrowId);
        if (b == null) {
            throw new GeneralException("图书不存在");
        }
        if (b.getReturnTime() == null) {
            throw new GeneralException("还未归还的借阅记录不能删除");
        }
        return this.borrowMapper.deleteById(borrowId) == 1;
    }

    @Override
    public Page<BorrowVO> borrowPage(int page, int size, Boolean borrow, Long readerId, Long bookId) {
        LambdaQueryWrapper<TbBorrow> wrapper = Wrappers.<TbBorrow>lambdaQuery()
                .orderByDesc(TbBorrow::getId)
                .eq(readerId != null, TbBorrow::getReaderId, readerId)
                .eq(bookId != null, TbBorrow::getBookId, bookId)
                // 未还
                .isNull(borrow == Boolean.TRUE, TbBorrow::getReturnTime)
                // 已还
                .isNotNull(borrow == Boolean.FALSE, TbBorrow::getReturnTime);

        Page<BorrowVO> pages = new Page<>(page, size);
        Page<TbBorrow> ps = new Page<>(page, size);
        //  查借阅表
        this.borrowMapper.selectPage(ps, wrapper);
        ArrayList<BorrowVO> list = new ArrayList<>();

        pages.setPages(ps.getPages());
        pages.setTotal(ps.getTotal());
        pages.setCurrent(ps.getCurrent());

        List<TbBorrow> borrowList = ps.getRecords();
        // 没有 记录
        if (borrowList.isEmpty()) {
            return pages;
        }
        List<Long> ids = borrowList.stream().map(TbBorrow::getBookId).collect(Collectors.toList());
        // in ( )
        // 查图书
        Map<Long, TbBook> books = this.baseMapper.selectBatchIds(ids)
                .stream().collect(Collectors.toMap(TbBook::getId, Function.identity()));

        ids = borrowList.stream().map(TbBorrow::getReaderId).collect(Collectors.toList());
        Map<Long, TbReader> readers = this.readerMapper.selectBatchIds(ids)
                .stream().collect(Collectors.toMap(TbReader::getId, Function.identity()));

        for (TbBorrow tbBorrow : borrowList) {
            BorrowVO vo = new BorrowVO();
            vo.setBook(BookVO.create(books.get(tbBorrow.getBookId()), typeMapper));
            vo.setReader(readers.get(tbBorrow.getReaderId()));
            vo.setBorrow(tbBorrow);
            list.add(vo);
        }

        pages.setRecords(list);
        return pages;
    }

    @Override
    public <E extends IPage<BookVO>> E pageVO(E page, BookQuery book) {
        Page<TbBook> p1 = new Page<>();
        p1.setCurrent(page.getCurrent());
        p1.setSize(page.getSize());

        LambdaQueryWrapper<TbBook> wrapper = Wrappers.<TbBook>lambdaQuery();
        // 中断查询
        if (!book.wrapper(wrapper, typeMapper)) {
            page.setPages(0);
            page.setTotal(0);
            page.setCurrent(0);
            page.setSize(0);
            return page;
        }
        wrapper.orderByDesc(TbBook::getId);
        p1 = this.page(p1, wrapper);

        page.setPages(p1.getPages());
        page.setTotal(p1.getTotal());

        ArrayList<BookVO> vos = new ArrayList<>();
        for (TbBook tb : p1.getRecords()) {
            vos.add(
                    BookVO.create(tb, typeMapper)
            );
        }
        page.setRecords(vos);
        return page;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BookVO updateByIdReturnVO(TbBook book) {
        // 数量不能直接修改
        book.setNumber(null);
        book.setBorrowNumber(null);


        // 类型更改，影响两个类型的统计数量
        int count;
        Long typeId = book.getTypeId();
        if (typeId != null) {
            TbType newType = typeMapper.selectById(typeId);
            TbBook b = this.baseMapper.selectById(book.getId());
            TbType oType = typeMapper.selectById(b.getTypeId());
            newType.setNumber(newType.getNumber() + b.getNumber());
            oType.setNumber(oType.getNumber() - b.getNumber());
            count = typeMapper.updateById(newType);
            if (count == 0) {
                throw new GeneralException("修改类型失败");
            }
            count = typeMapper.updateById(oType);
            if (count == 0) {
                throw new GeneralException("修改类型失败");
            }
        }

        count = this.baseMapper.updateById(book);
        if (count == 0) {
            throw new GeneralException("修改失败");
        }


        return BookVO.create(book, typeMapper);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean returnBook(long readerId, long bookId, Timestamp time) {
        // 查读者
        TbReader reader = readerMapper.selectById(readerId);
        if (reader == null) {
            throw new GeneralException("用户不存在");
        }
        TbBook book = this.baseMapper.selectById(bookId);
        if (book == null) {
            throw new GeneralException("图书不存在");
        }
        TbBorrow borrow = this.borrowMapper.selectOne(
                Wrappers.<TbBorrow>lambdaQuery()
                        .eq(TbBorrow::getReaderId, readerId)
                        .eq(TbBorrow::getBookId, bookId)
                        .isNull(TbBorrow::getReturnTime)
        );
        if (borrow == null) {
            throw new GeneralException("借阅记录不存在");
        }
        // 借阅数量 - 1
        book.setBorrowNumber(book.getBorrowNumber() - 1);
        // 更新图书
        long count = this.baseMapper.updateById(book);
        if (count == 0) {
            throw new GeneralException("操作失败，请重试！");
        }
        // 更新借阅 还书时间
        borrow.setReturnTime(time != null ? time : new Timestamp(System.currentTimeMillis()));
        count = this.borrowMapper.updateById(borrow);
        if (count == 0) {
            throw new GeneralException("操作失败，请重试！");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean borrowBook(long readerId, long bookId) {
        // 查读者
        TbReader reader = readerMapper.selectById(readerId);
        if (reader == null) {
            throw new GeneralException("用户不存在");
        }
        TbBook book = this.baseMapper.selectById(bookId);
        if (book == null) {
            throw new GeneralException("图书不存在");
        }
        // number - borrowNumber > 0
        if (book.getNumber() - book.getBorrowNumber() <= 0) {
            throw new GeneralException("图书已全部借出");
        }
        long count = this.borrowMapper.selectCount(
                Wrappers.<TbBorrow>lambdaQuery()
                        .eq(TbBorrow::getReaderId, readerId)
                        .eq(TbBorrow::getBookId, bookId)
                        .isNull(TbBorrow::getReturnTime)
        );
        if (count != 0) {
            throw new GeneralException("已借此书，还未归还！");
        }
        // 借阅的数量+1
        book.setBorrowNumber(book.getBorrowNumber() + 1);
        // 新增借阅记录
        TbBorrow borrow = new TbBorrow();
        borrow.setReaderId(readerId);
        borrow.setBookId(bookId);
        count = this.borrowMapper.insert(borrow);
        if (count == 0) {
            throw new GeneralException("操作失败，请重试！");
        }
        count = this.baseMapper.updateById(book);
        if (count == 0) {
            throw new GeneralException("操作失败，请重试！");
        }
        return true;
    }

    @Override
    public List<BorrowVO> unReturnByReaderTel(String tel) {
        return unReturnByReader(
                readerMapper.selectOne(Wrappers.<TbReader>lambdaQuery().eq(TbReader::getTel, tel))
        );
    }

    @Override
    public List<BorrowVO> unReturnByReaderId(long id) {
        return unReturnByReader(readerMapper.selectById(id));
    }

    private List<BorrowVO> unReturnByReader(TbReader reader) {
        if (reader == null) {
            throw new GeneralException("用户未注册");
        }
        ArrayList<BorrowVO> list = new ArrayList<>();
        //  查借阅表
        List<TbBorrow> borrowList = borrowMapper.selectList(
                Wrappers.<TbBorrow>lambdaQuery()
                        .eq(TbBorrow::getReaderId, reader.getId())
                        .isNull(TbBorrow::getReturnTime)
        );
        // 没有未归还的借书记录
        if (borrowList.isEmpty()) {
            return list;
        }
        List<Long> ids = borrowList.stream().map(TbBorrow::getBookId).collect(Collectors.toList());

        // in ( )
        // 查图书
        List<TbBook> books = this.baseMapper.selectBatchIds(ids);
        for (TbBorrow tbBorrow : borrowList) {
            BorrowVO vo = new BorrowVO();
            for (TbBook book : books) {
                if (Objects.equals(tbBorrow.getBookId(), book.getId())) {
                    vo.setBook(BookVO.create(book, typeMapper));
                    vo.setBorrow(tbBorrow);
                    break;
                }
            }

            list.add(vo);
        }
        return list;
    }

    @Autowired
    TbBorrowMapper borrowMapper;
    @Autowired
    TbStorageMapper storageMapper;

    /**
     * 如果借阅表中有此书，且未归还，就不允许删除。
     * 否则级联删除借阅表和入库表中的信息。
     *
     * @param id 图书ID
     * @return 成功或抛出Runtime异常
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean removeById(Serializable id) {
        // 查看图书在不在
        TbBook book = this.baseMapper.selectById(id);
        if (book == null) {
            throw new GeneralException("图书不存在");
        }
        // 如果借阅表中有此书，且未归还，就不允许删除
        long count = borrowMapper.selectCountRecordsNotReturnedByBookId(id);
        if (count != 0) {
            throw new GeneralException("该图书还有未归还的图书：" + count + "本");
        }
        // 级联删除借阅表
        LambdaQueryWrapper<TbBorrow> w1 = Wrappers.<TbBorrow>lambdaQuery().eq(TbBorrow::getBookId, id);
        count = borrowMapper.selectCount(w1);
        long delete = borrowMapper.delete(w1);
        if (count != delete) {
            throw new GeneralException("删除图书的级联借阅信息失败");
        }
        // 级联删除入库
        LambdaQueryWrapper<TbStorage> w2 = Wrappers.<TbStorage>lambdaQuery().eq(TbStorage::getBookId, id);
        count = storageMapper.selectCount(w2);
        delete = storageMapper.delete(w2);
        if (count != delete) {
            throw new GeneralException("删除图书的级联入库信息失败");
        }

        // 删除图书信息
        if (!super.removeById(id)) {
            throw new GeneralException("删除图书信息失败");
        }

        return true;
    }

    @Override
    public boolean save(TbBook entity) {
        entity.setId(null);
        Long count = this.baseMapper.selectCount(Wrappers.<TbBook>lambdaQuery().eq(TbBook::getName, entity.getName()));
        if (count != 0) {
            throw new GeneralException("书名重复");
        }
        return super.save(entity);
    }
}
