package com.book.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.beans.TbType;
import com.book.config.exception.GeneralException;

import java.io.Serializable;
import java.lang.reflect.Type;

public interface ITypeService extends IService<TbType> {
    public Page<TbType> page(int page, int size, TbType type);

}
