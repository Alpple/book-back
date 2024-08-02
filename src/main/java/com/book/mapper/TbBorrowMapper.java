package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.beans.TbBorrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

@Mapper
public interface TbBorrowMapper extends BaseMapper<TbBorrow> {
    /**
     * 统计bookId为归还的数量
     * @param bookId 图书ID
     * @return 数量
     */
    @Select("select count(0) from tb_borrow where book_id=#{bookId} and return_time is null")
    long selectCountRecordsNotReturnedByBookId(@Param("bookId") Serializable bookId);
}
