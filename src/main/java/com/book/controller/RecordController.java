package com.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/record")
public class RecordController {
//    @Autowired
//    RecordService recordService;
//    @Autowired
//    BookService bookService;
//    @Autowired
//    UserService userService;
//
//    @PostMapping("/borrow")
//    public Result<Boolean> borrow(@RequestBody Record record) {
//        try {
//            return Result.success(recordService.borrow(record), "处理成功");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Result.error(false, "处理失败");
//    }
//
//    // 归还
//    @PostMapping("/record")
//    public Result<String> record(@RequestParam Long recordId) {
//        try {
//            return Result.success(recordService.record(recordId), "处理成功");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Result.error(null, "处理失败");
//    }
//
//    @GetMapping("/list/{page}/{size}")
//    public Result<Page<Record>> list(@PathVariable int page, @PathVariable int size,
//                                     @RequestParam(required = false) String bookName,
//                                     @RequestParam(required = false) Boolean onlyReturn,
//                                     @RequestParam(required = false) String userTel) {
//        Page<Record> pageObj = new Page<>(page, size);
//        LambdaQueryWrapper<Record> wrapper = Wrappers.<Record>lambdaQuery();
//        if (bookName != null) {
//            Long bookId = bookService.getOne(Wrappers.<Book>lambdaQuery().eq(Book::getBookName, bookName)).getId();
//            wrapper.eq(Record::getBookId, bookId);
//        }
//        if (userTel != null) {
//            Long userId = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getTel, userTel)).getId();
//            wrapper.eq(Record::getUserId, userId);
//        }
//        if (onlyReturn != null) {
//            if (onlyReturn) {
//                wrapper.isNotNull(Record::getReturnTime);
//            }
//            if (!onlyReturn) {
//                wrapper.isNull(Record::getReturnTime);
//            }
//        }
//        pageObj = recordService.page(
//                pageObj,
//                wrapper
//        );
//        for (Record record : pageObj.getRecords()) {
//            record.setUser(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, record.getUserId())));
//            record.setBook(bookService.getOne(Wrappers.<Book>lambdaQuery().eq(Book::getId, record.getBookId())));
//        }
//        return Result.success(pageObj, "查询成功");
//    }

}
