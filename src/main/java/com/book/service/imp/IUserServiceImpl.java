package com.book.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.TbBorrow;
import com.book.beans.TbEmp;
import com.book.beans.TbReader;
import com.book.beans.TbRoot;
import com.book.config.exception.GeneralException;
import com.book.mapper.TbBorrowMapper;
import com.book.mapper.TbEmpMapper;
import com.book.mapper.TbReaderMapper;
import com.book.mapper.TbRootMapper;
import com.book.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    TbReaderMapper readerMapper;
    @Autowired
    TbEmpMapper empMapper;

    public void pageReader(Page<TbReader> page, TbReader reader) {
        LambdaQueryWrapper<TbReader> wrapper = Wrappers.<TbReader>lambdaQuery();
        if (reader.getName() != null) {
            wrapper.like(TbReader::getName, reader.getName());
            reader.setName(null);
        }
        wrapper.setEntity(reader);
        wrapper.orderByDesc(TbReader::getId);
        readerMapper.selectPage(page, wrapper);
    }

    @Override
    public void pageEmp(Page<TbEmp> page, TbEmp emp) {
        LambdaQueryWrapper<TbEmp> wrapper = Wrappers.<TbEmp>lambdaQuery();
        if (emp.getName() != null) {
            wrapper.like(TbEmp::getName, emp.getName());
            emp.setName(null);
        }
        wrapper.setEntity(emp);
        wrapper.orderByDesc(TbEmp::getId);
        empMapper.selectPage(page, wrapper);
    }

    @Override
    public TbReader saveReader(TbReader reader) {
        //
        reader.setId(null);

        TbReader needNull = this.readerMapper.selectOne(
                Wrappers.<TbReader>lambdaQuery()
                        .eq(TbReader::getTel, reader.getTel())
        );
        if (needNull != null) {
            throw new GeneralException("名字重复");
        }
        needNull = this.readerMapper.selectOne(
                Wrappers.<TbReader>lambdaQuery()
                        .eq(TbReader::getAccount, reader.getAccount())
        );
        if (needNull != null) {
            throw new GeneralException("账号重复");
        }

        if (this.readerMapper.insert(reader) != 1) {
            throw new GeneralException("操作失败");
        }

        return reader;
    }

    @Override
    public TbEmp saveEmp(TbEmp emp) {

        //
        emp.setId(null);

        TbEmp needNull = this.empMapper.selectOne(
                Wrappers.<TbEmp>lambdaQuery()
                        .eq(TbEmp::getTel, emp.getTel())
        );
        if (needNull != null) {
            throw new GeneralException("手机号重复");
        }
        needNull = this.empMapper.selectOne(
                Wrappers.<TbEmp>lambdaQuery()
                        .eq(TbEmp::getAccount, emp.getAccount())
        );
        if (needNull != null) {
            throw new GeneralException("账号重复");
        }

        if (this.empMapper.insert(emp) != 1) {
            throw new GeneralException("操作失败");
        }

        return emp;
    }

    @Override
    public TbReader updateReader(TbReader reader) {
        if (reader.getId() == null) {
            throw new GeneralException("用户不存");
        }
        if (reader.getTel() != null) {
            TbReader needNull = this.readerMapper.selectOne(
                    Wrappers.<TbReader>lambdaQuery()
                            .eq(TbReader::getTel, reader.getTel())
                            .ne(TbReader::getId, reader.getId())
            );
            if (needNull != null) {
                throw new GeneralException("名字重复");
            }
        }

        if (this.readerMapper.updateById(reader) != 1) {
            throw new GeneralException("修改失败");
        }
        return this.readerMapper.selectById(reader.getId());
    }

    @Override
    public TbEmp updateEmp(TbEmp emp) {
        if (emp.getId() == null) {
            throw new GeneralException("用户不存");
        }
        if (emp.getTel() != null) {
            TbEmp needNull = this.empMapper.selectOne(
                    Wrappers.<TbEmp>lambdaQuery()
                            .eq(TbEmp::getTel, emp.getTel())
                            .ne(TbEmp::getId, emp.getId())
            );
            if (needNull != null) {
                throw new GeneralException("名字重复");
            }
        }

        if (this.empMapper.updateById(emp) != 1) {
            throw new GeneralException("修改失败");
        }
        return this.empMapper.selectById(emp.getId());
    }

    @Autowired
    TbBorrowMapper borrowMapper;

    @Override
    public boolean removeReader(long id) {
        List<TbBorrow> borrows = borrowMapper.selectList(
                Wrappers.<TbBorrow>lambdaQuery()
                        .isNull(TbBorrow::getReturnTime)
                        .eq(TbBorrow::getReaderId, id)
        );
        if (!borrows.isEmpty()) {
            throw new GeneralException("还有图书未还");
        }
        LambdaQueryWrapper<TbBorrow> wra = Wrappers.<TbBorrow>lambdaQuery().eq(TbBorrow::getReaderId, id);
        long count = borrowMapper.selectCount(wra);
        long delete = borrowMapper.delete(wra);
        if (count != delete) {
            throw new GeneralException("级联删除借阅记录失败，请重试");
        }
        if (readerMapper.deleteById(id) != 1) {
            throw new GeneralException("删除失败，请重试");
        }
        return true;
    }

    @Autowired
    TbRootMapper rootMapper;

    @Override
    public TbRoot rootLogin(String acc, String pass) {
        Long aLong = rootMapper.selectCount(
                Wrappers.<TbRoot>lambdaQuery()
                        .eq(TbRoot::getAccount, acc)
        );
        if (aLong == 0) {
            throw new GeneralException("账号不存在");
        }
        return rootMapper.selectOne(
                Wrappers.<TbRoot>lambdaQuery()
                        .eq(TbRoot::getAccount, acc)
                        .eq(TbRoot::getPassword, pass)
        );
    }

    @Override
    public TbReader readerLogin(String acc, String pass) {
        Long aLong = readerMapper.selectCount(
                Wrappers.<TbReader>lambdaQuery()
                        .eq(TbReader::getAccount, acc)
        );
        if (aLong == 0) {
            throw new GeneralException("账号不存在");
        }
        return readerMapper.selectOne(
                Wrappers.<TbReader>lambdaQuery()
                        .eq(TbReader::getAccount, acc)
                        .eq(TbReader::getPassword, pass)
        );
    }

    @Override
    public TbEmp empLogin(String acc, String pass) {
        Long aLong = empMapper.selectCount(
                Wrappers.<TbEmp>lambdaQuery()
                        .eq(TbEmp::getAccount, acc)
        );
        if (aLong == 0) {
            throw new GeneralException("账号不存在");
        }
        return empMapper.selectOne(
                Wrappers.<TbEmp>lambdaQuery()
                        .eq(TbEmp::getAccount, acc)
                        .eq(TbEmp::getPassword, pass)
        );
    }

    @Override
    public TbReader verReaderTel(String tel) {
        return readerMapper.selectOne(Wrappers.<TbReader>lambdaQuery().eq(TbReader::getTel, tel));
    }

    @Override
    public TbEmp verEmpTel(String tel) {
        return empMapper.selectOne(Wrappers.<TbEmp>lambdaQuery().eq(TbEmp::getTel, tel));
    }

}

