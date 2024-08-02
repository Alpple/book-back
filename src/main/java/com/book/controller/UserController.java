package com.book.controller;


import cn.hutool.captcha.CircleCaptcha;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.Result;
import com.book.beans.TbEmp;
import com.book.beans.TbReader;
import com.book.beans.TbRoot;
import com.book.config.exception.GeneralException;
import com.book.service.imp.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserServiceImpl userService;

    @Autowired
    CircleCaptcha captcha;

    @PostMapping("/reader/list/{page}/{size}")
    public Result<Page<TbReader>> pageReader(@PathVariable int page, @PathVariable int size,
                                             @RequestBody(required = false) TbReader reader) {
        Page<TbReader> list = new Page<>(page, size);
        userService.pageReader(list, reader);
        return Result.success(list, "查询成功");
    }

    @PostMapping("/emp/list/{page}/{size}")
    public Result<Page<TbEmp>> listEmp(@PathVariable int page, @PathVariable int size,
                                       @RequestBody(required = false) TbEmp reader) {
        Page<TbEmp> list = new Page<>(page, size);
        userService.pageEmp(list, reader);
        return Result.success(list, "查询成功");
    }



    @PostMapping("/emp/reset/password")
    public Result<Boolean> empRestPass(@RequestParam long id, HttpSession session) {
        boolean b = userService.resetPasswordEmp(id);
        if (b) {
            Object obj = session.getAttribute("emp");
            if (obj != null) {
                TbEmp emp = (TbEmp) obj;
                emp.setPassword("12345678");
                session.setAttribute("emp", emp);
            }
            return Result.success(true, "重置密码成功");
        }

        return Result.error(false, "重置密码失败，请重新尝试");
    }

    @PostMapping("/reader/reset/password")
    public Result<Boolean> readerRestPass(@RequestParam long id, HttpSession session) {
        boolean b = userService.resetPasswordReader(id);
        if (b) {
            Object obj = session.getAttribute("reader");
            if (obj != null) {
                TbReader reader = (TbReader) obj;
                reader.setPassword("12345678");
                session.setAttribute("reader", reader);
            }
            return Result.success(true, "重置密码成功");
        }

        return Result.error(false, "重置密码失败，请重新尝试");
    }

    @PutMapping("/emp")
    public Result<TbEmp> saveEmp(@RequestBody TbEmp emp) {
        TbEmp emp1 = userService.saveEmp(emp);
        if (emp1 != null) {
            return Result.success(emp1, "注册成功");
        }
        return Result.error(null, "注册失败，请重新尝试");
    }

    @PostMapping("/emp/enabled")
    public Result<Boolean> empEnabled(@RequestParam long id, @RequestParam boolean enabled) {
        boolean b = userService.enabledEmp(id, enabled);
        if (b) {
            return Result.success(true, "操作成功");
        }
        return Result.error(false, "操作失败，请重新尝试");
    }

    @DeleteMapping("/reader/{id}")
    public Result<Boolean> removeReader(@PathVariable long id) {
        boolean b = userService.removeReader(id);
        if (b) {
            return Result.success(true, "删除成功");
        }
        return Result.error(false, "删除失败，请重新尝试");
    }

    @PostMapping("/reader")
    public Result<TbReader> readerUpdate(@RequestBody TbReader reader) {
        reader.setAccount(null);
        TbReader r = userService.updateReader(reader);
        if (r != null) {
            return Result.success(r, "修改成功");
        }
        return Result.error(null, "修改失败，请重新尝试");
    }

    @PostMapping("/emp")
    public Result<TbEmp> readerUpdate(@RequestBody TbEmp emp) {
        emp.setAccount(null);
        emp.setEnabled(null);
        TbEmp r = userService.updateEmp(emp);
        if (r != null) {
            return Result.success(r, "修改成功");
        }
        return Result.error(null, "修改失败，请重新尝试");
    }







}
