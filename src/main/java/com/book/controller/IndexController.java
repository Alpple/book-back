package com.book.controller;


import cn.hutool.captcha.CircleCaptcha;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.*;
import com.book.config.exception.GeneralException;
import com.book.service.ITypeService;
import com.book.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class IndexController {

    @Autowired
    CircleCaptcha captcha;

    @GetMapping(value = "/")
    @ResponseBody
    public String index() {
        return "未设置主页路径";
    }


    /**
     * 获取验证码
     *
     * @return 验证码的base64 图片
     */
    @GetMapping(value = "/tel-code")
    @ResponseBody
    public Result<Boolean> tel(HttpSession session, @RequestParam String tel) {
        captcha.createCode();
        String code = captcha.getCode();
        session.setAttribute(tel, code);
        System.out.println("手机 " + tel + " 的验证码= " + code);
        return Result.success(true, "验证码获取成功");
    }

    @GetMapping(value = "/ver-tel")
    @ResponseBody
    public Result<Boolean> verTel(HttpSession session, @RequestParam String tel) {
        if (userService.verReaderTel(tel)!=null){
            return Result.success(true,"手机号检查通过");
        }

        return Result.error(false, "手机号未注册");
    }

    @GetMapping(value = "/captcha-code")
    @ResponseBody
    public Result<HashMap<String, String>> captchaCode(HttpSession session, @RequestParam String key) {
        captcha.createCode();
        String code = captcha.getCode();
        session.setAttribute(key, code);
        System.out.println("captcha-code = " + code);
        HashMap<String, String> map = new HashMap<>();
        map.put("base64", captcha.getImageBase64());
        map.put("code", code);
        return Result.success(map, "验证码获取成功");
    }

    @PostMapping(value = "/code/check")
    @ResponseBody
    public Result<Boolean> captchaCodeCheck(HttpSession session, @RequestParam String key, @RequestParam String code) {
        if (this.checkCaptchaError(key, code, session)) {
            return Result.error(false, "验证码错误");
        }
        return Result.success(true, "验证码获取成功");
    }

    @Autowired
    ITypeService typeService;

    @PutMapping("/register/reader/{code}")
    @ResponseBody
    public Result<TbReader> saveReader(
            @RequestBody TbReader reader,
            @PathVariable String code,
            HttpSession session
    ) {
        this.checkCaptchaError(LOGIN_CODE_READER, code, session);
        TbReader reader1 = userService.saveReader(reader);
        if (reader1 != null) {
            return Result.success(reader1, "注册成功");
        }
        return Result.error(null, "注册失败，请重新尝试");
    }
    @PutMapping("/register/emp/{code}")
    @ResponseBody
    public Result<TbEmp> saveEmp(
            @RequestBody TbEmp emp,
            @PathVariable String code,
            HttpSession session
    ) {
        this.checkCaptchaError(LOGIN_CODE_EMP, code, session);
        TbEmp e = userService.saveEmp(emp);
        if (e != null) {
            return Result.success(e, "注册成功");
        }
        return Result.error(null, "注册失败，请重新尝试");
    }

    @PostMapping("/type/{page}/{size}")
    @ResponseBody
    public Result<Page<TbType>> typePage(@PathVariable int page, @PathVariable int size,
                                         @RequestBody(required = false) TbType type) {
        return Result.success(typeService.page(page, size, type), "查询成功");
    }

    @DeleteMapping("/type/{id}")
    @ResponseBody
    public Result<Boolean> typeRemove(@PathVariable int id) {
        return Result.success(typeService.removeById(id), "查询成功");
    }

    @PostMapping("/type")
    @ResponseBody
    public Result<Boolean> typeUpdate(@RequestBody TbType type) {
        return Result.success(typeService.updateById(type), "查询成功");
    }

    @PutMapping("/type")
    @ResponseBody
    public Result<Boolean> typeSave(@RequestBody TbType type) {
        return Result.success(typeService.save(type), "查询成功");
    }

    public static final String LOGIN_CODE_ROOT = "root-login-code";
    public static final String SESSION_KEY_ROOT = "root-login-session-key";
    public static final String LOGIN_CODE_READER = "reader-login-code";
    public static final String SESSION_KEY_READER = "reader-login-session-key";
    public static final String LOGIN_CODE_EMP = "emp-login-code";
    public static final String SESSION_KEY_EMP = "emp-login-session-key";

    // 退出登录
    @PostMapping("/unLogin")
    @ResponseBody
    public Result<String> unLogin(HttpSession session) {
        session.removeAttribute(LOGIN_CODE_ROOT);
        session.removeAttribute(SESSION_KEY_ROOT);
        session.removeAttribute(LOGIN_CODE_READER);
        session.removeAttribute(SESSION_KEY_READER);
        session.removeAttribute(LOGIN_CODE_EMP);
        session.removeAttribute(SESSION_KEY_EMP);
        session.invalidate();
        return Result.success(null, "退出登录成功");
    }

    private boolean checkCaptchaError(String key, String code, HttpSession session) {
        Object c = session.getAttribute(key);
        if (c == null) {
            throw new GeneralException("请先获取/刷新验证码");
        }
        session.removeAttribute(key);
        return !c.toString().equals(code);
    }

    @Autowired
    IUserService userService;

    // 登录
    @PostMapping("/root/login")
    @ResponseBody
    public Result<TbRoot> login(@RequestParam String acc, @RequestParam String pass, String code, HttpSession session) {
        if (checkCaptchaError(LOGIN_CODE_ROOT, code, session)) {
            return Result.error(null, "验证码错误");
        }
        TbRoot user = userService.rootLogin(acc, pass);
        if (user == null) {
            return Result.error(null, "密码错误");
        }
        session.setAttribute("key",SESSION_KEY_ROOT);
        session.setAttribute(SESSION_KEY_ROOT, user);
        return Result.success(user, "登录成功");
    }

    @PostMapping("/emp/login")
    @ResponseBody
    public Result<TbEmp> empLogin(@RequestParam String acc, @RequestParam String pass, String code, HttpSession session) {
        if (checkCaptchaError(LOGIN_CODE_EMP, code, session)) {
            return Result.error(null, "验证码错误");
        }
        TbEmp user = userService.empLogin(acc, pass);
        if (user == null) {
            return Result.error(null, "密码错误");
        }
        session.setAttribute("key",SESSION_KEY_EMP);
        session.setAttribute(SESSION_KEY_EMP, user);
        return Result.success(user, "登录成功");
    }

    @PostMapping("/reader/login")
    @ResponseBody
    public Result<TbReader> readerLogin(@RequestParam String acc, @RequestParam String pass, String code, HttpSession session) {
        if (checkCaptchaError(LOGIN_CODE_READER, code, session)) {
            return Result.error(null, "验证码错误");
        }
        TbReader user = userService.readerLogin(acc, pass);
        if (user == null) {
            return Result.error(null, "密码错误");
        }
        session.setAttribute("key",SESSION_KEY_READER);
        session.setAttribute(SESSION_KEY_READER, user);
        return Result.success(user, "登录成功");
    }




    // 修改密码
    @PostMapping("/reader/password")
    @ResponseBody
    public Result<Boolean> readerUpdatePass(@RequestParam String password, @RequestParam String tel, HttpSession session) {
        boolean b = userService.updatePasswordReader(tel, password);
        if (b) {
            Object obj = session.getAttribute(SESSION_KEY_READER);
            if (obj != null) {
                TbReader reader = (TbReader) obj;
                reader.setPassword(password);
                session.setAttribute(SESSION_KEY_READER, reader);
            }
            return Result.success(true, "修改密码成功");
        }

        return Result.error(false, "修改密码失败，请重新尝试");
    }

    // 修改密码
    @PostMapping("/emp/password")
    @ResponseBody
    public Result<Boolean> empUpdatePass(@RequestParam String password, @RequestParam String tel, HttpSession session) {
        boolean b = userService.updatePasswordEmp(tel, password);
        if (b) {
            Object obj = session.getAttribute(SESSION_KEY_EMP);
            if (obj != null) {
                TbEmp emp = (TbEmp) obj;
                emp.setPassword(password);
                session.setAttribute(SESSION_KEY_EMP, emp);
            }
            return Result.success(true, "修改密码成功");
        }

        return Result.error(false, "修改密码失败，请重新尝试");
    }

}
