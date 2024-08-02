package com.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.beans.query.BookQuery;
import com.book.beans.TbBook;
import com.book.beans.query.StorageQuery;
import com.book.beans.vo.BookVO;
import com.book.beans.vo.BorrowVO;
import com.book.beans.vo.StorageVO;

import java.sql.Timestamp;
import java.util.List;

public interface IBookService extends IService<TbBook> {

    <E extends IPage<BookVO>> E pageVO(E page, BookQuery book);

    BookVO updateByIdReturnVO(TbBook book);

    List<BorrowVO> unReturnByReaderTel(String tel);
    List<BorrowVO> unReturnByReaderId(long id);

    boolean borrowBook(long readerId, long bookId);
    boolean returnBook(long readerId, long bookId, Timestamp time);

    boolean storage(StorageQuery query);


    Page<BorrowVO> borrowPage(int page, int size, Boolean borrow, Long readerId, Long bookId);

    boolean removeBorrow(long borrowId);

    Page<StorageVO> storagePage(int page, int size,   Long empId, Long bookId);
    boolean removeStorage(long id);

}
