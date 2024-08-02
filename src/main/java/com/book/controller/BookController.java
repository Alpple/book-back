package com.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.*;
import com.book.beans.query.BookQuery;
import com.book.beans.query.StorageQuery;
import com.book.beans.vo.BookVO;
import com.book.beans.vo.BorrowVO;
import com.book.beans.vo.StorageVO;
import com.book.service.IBookService;
import com.book.service.ITypeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    IBookService bookService;

    // borrow: true：未归还， false: 已归还， null: 全部
    @GetMapping("/borrow/list/{page}/{size}")
    public Result<Page<BorrowVO>> borrowList(
            @PathVariable int page, @PathVariable int size,
            @RequestParam(required = false) Boolean borrow,
            @RequestParam(required = false) Long readerId,
            @RequestParam(required = false) Long bookId) {
        return Result.success(
                bookService.borrowPage(page, size, borrow, readerId, bookId)
                , "查询成功");
    }


    @DeleteMapping("/borrow/{id}")
    public Result<Boolean> removeBorrow(
            @PathVariable int id) {
        return Result.success(bookService.removeBorrow(id), "删除成功");
    }

    @DeleteMapping("/storage/{id}")
    public Result<Boolean> removeStorage(
            @PathVariable int id) {
        return Result.success(bookService.removeStorage(id), "删除成功");
    }

    @GetMapping("/storage/list/{page}/{size}")
    public Result<Page<StorageVO>> storageList(
            @PathVariable int page, @PathVariable int size,
            @RequestParam(required = false) Long empId,
            @RequestParam(required = false) Long bookId) {
        return Result.success(
                bookService.storagePage(page, size, empId, bookId)
                , "查询成功");
    }

    @PutMapping("/storage")
    public Result<Boolean> storage(@RequestBody StorageQuery query, HttpSession session) {
        Object key = session.getAttribute("key");
        if (key==null){
            return Result.error(false,"请重新登录,再尝试");
        }
        key = session.getAttribute(key.toString());
        if (!(key instanceof TbEmp)) {
            return Result.error(false,"只有图书管理员才有权限");
        }

        query.setEmpId(((TbEmp)key).getId());
        return Result.success(bookService.storage(query), "入库成功");
    }


    @PutMapping("/borrow/{readerId}/{bookId}")
    public Result<Boolean> borrowBook(@PathVariable int readerId, @PathVariable int bookId) {
        return Result.success(bookService.borrowBook(readerId, bookId), "借出成功");
    }

    @PostMapping(value = {"/return/{readerId}/{bookId}", "/return/{readerId}/{bookId}/{time}"})
    public Result<Boolean> returnBook(
            @PathVariable int readerId, @PathVariable int bookId,
            @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") @PathVariable(required = false) Timestamp time) {
        return Result.success(bookService.returnBook(readerId, bookId, time), "还书成功");
    }

    @PostMapping("/list/{page}/{size}")
    public Result<Page<BookVO>> list(@PathVariable int page, @PathVariable int size,
                                     @RequestBody(required = false) BookQuery book) {
        Page<BookVO> list = new Page<>(page, size);
        bookService.pageVO(list, book);
        return Result.success(list, "查询成功");
    }

    @PostMapping("/tb/list/{page}/{size}")
    public Result<Page<TbBook>> listTb(@PathVariable int page, @PathVariable int size,
                                       @RequestBody(required = false) BookQuery book) {
        Page<TbBook> list = new Page<>(page, size);
        LambdaQueryWrapper<TbBook> wrapper = Wrappers.<TbBook>lambdaQuery();
        TbBook book1 = book.toTb();
        if (book1.getName() != null) {
            wrapper.like(TbBook::getName, book1.getName());
            book1.setName(null);
        }
        wrapper.setEntity(book1);
        wrapper.orderByDesc(TbBook::getId);
        bookService.page(list, wrapper);
        return Result.success(list, "查询成功");
    }


    @GetMapping("/reader/unreturn/list/tel/{tel}")
    public Result<List<BorrowVO>> listReaderUnreturn(@PathVariable String tel) {
        return Result.success(bookService.unReturnByReaderTel(tel), "查询成功");
    }

    @GetMapping("/reader/unreturn/list/id/{id}")
    public Result<List<BorrowVO>> listReaderUnreturn(@PathVariable long id) {
        return Result.success(bookService.unReturnByReaderId(id), "查询成功");
    }

    //
    @PostMapping("/update")
    public Result<BookVO> update(@RequestBody TbBook book) {
        return Result.success(bookService.updateByIdReturnVO(book), "更新成功");
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody TbBook book) {
        if (bookService.save(book)) {
            return Result.success(true, "添加成功");
        }
        return Result.error(false, "添加失败，请重试");
    }

    @DeleteMapping("/remove")
    public Result<Boolean> add(@RequestParam int id) {
        return Result.success(bookService.removeById(id), "删除成功");
    }

    @Autowired
    ITypeService typeService;

    @PostMapping("/type/list/{page}/{size}")
    public Result<Page<TbType>> typeList(@PathVariable int page, @PathVariable int size,
                                         @RequestBody(required = false) TbType type) {
        Page<TbType> list = new Page<>(page, size);
        LambdaQueryWrapper<TbType> wrapper = Wrappers.<TbType>lambdaQuery();
        if (type.getName() != null) {
            wrapper.like(TbType::getName, type.getName());
            type.setName(null);
        }
        wrapper.setEntity(type);
        typeService.page(list, wrapper);
        return Result.success(list, "查询成功");
    }
}
