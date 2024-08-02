package com.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.beans.TbEmp;
import com.book.beans.TbReader;
import com.book.beans.TbRoot;
import com.book.config.exception.GeneralException;

public interface IUserService {
    void pageReader(Page<TbReader> page, TbReader reader);

    void pageEmp(Page<TbEmp> page, TbEmp emp);

    TbReader saveReader(TbReader reader);

    TbEmp saveEmp(TbEmp emp);

    TbReader updateReader(TbReader reader);

    TbEmp updateEmp(TbEmp emp);

    boolean removeReader(long id);

    default boolean enabledEmp(long id, boolean enabled) {
        TbEmp emp = new TbEmp();
        emp.setId(id);
        emp.setEnabled(enabled);
        return updateEmp(emp) != null;
    }

    ;

    default boolean updatePasswordReader(String tel, String password) {
        TbReader e = verReaderTel(tel);
        if (e == null) {
            throw new GeneralException("手机号未注册");
        }
        TbReader reader = new TbReader();
        reader.setId(e.getId());
        reader.setPassword(password);
        return updateReader(reader) != null;
    }

    default boolean updatePasswordEmp(String tel, String password) {
        TbEmp e = verEmpTel(tel);
        if (e == null) {
            throw new GeneralException("手机号未注册");
        }
        TbEmp emp = new TbEmp();
        emp.setId(e.getId());
        emp.setPassword(password);
        return updateEmp(emp) != null;
    }

    default boolean resetPasswordReader(long id) {
        TbReader reader = new TbReader();
        reader.setId(id);
        reader.setPassword("12345678");
        return updateReader(reader) != null;
    }

    default boolean resetPasswordEmp(long id) {
        TbEmp emp = new TbEmp();
        emp.setId(id);
        emp.setPassword("12345678");
        return updateEmp(emp) != null;
    }


    TbRoot rootLogin(String acc, String pass);

    TbReader readerLogin(String acc, String pass);

    TbEmp empLogin(String acc, String pass);


    TbReader verReaderTel(String tel);

    TbEmp verEmpTel(String tel);
}
