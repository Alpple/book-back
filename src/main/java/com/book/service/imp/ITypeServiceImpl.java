package com.book.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.beans.TbBook;
import com.book.beans.TbType;
import com.book.config.exception.GeneralException;
import com.book.mapper.TbBookMapper;
import com.book.mapper.TbTypeMapper;
import com.book.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ITypeServiceImpl extends ServiceImpl<TbTypeMapper, TbType> implements ITypeService {
    @Override
    public boolean save(TbType entity) {
        checkReName(entity.getName());
        return super.save(entity);
    }

    public void checkReName(String name) {
        long count = this.getBaseMapper().selectCount(
                Wrappers.<TbType>lambdaQuery().eq(TbType::getName, name)
        );

        if (count != 0) {
            throw new GeneralException("类型已存在！");
        }
    }

    @Autowired
    TbBookMapper bookMapper;

    @Override
    public boolean removeById(Serializable id) {
        Long count = bookMapper.selectCount(
                Wrappers.<TbBook>lambdaQuery()
                        .eq(TbBook::getTypeId, id)
        );

        if (count != 0) {
            throw new GeneralException("此类型下还有图书，不能删除！");
        }
        return super.removeById(id);
    }

    @Override
    public boolean updateById(TbType entity) {
        checkReName(entity.getName());
        return super.updateById(entity);
    }

    @Override
    public Page<TbType> page(int page, int size, TbType type) {
        Page<TbType> result = new Page<>(page, size);
        LambdaQueryWrapper<TbType> wrapper = Wrappers.<TbType>lambdaQuery()
                .orderByDesc(TbType::getId);
        if (type.getName() != null) {
            wrapper.like(TbType::getName, type.getName());
            type.setName(null);
        }
        wrapper.setEntity(type);

        this.page(result, wrapper);

        return result;
    }
}
