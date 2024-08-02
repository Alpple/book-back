package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName("tb_borrow")
@Data
public class TbBorrow {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private java.sql.Timestamp borrowTime;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private java.sql.Timestamp returnTime;
    private Long readerId;
    private Long bookId;

}
