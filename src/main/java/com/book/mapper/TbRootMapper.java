package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.beans.TbBook;
import com.book.beans.TbRoot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbRootMapper extends BaseMapper<TbRoot> {
}
