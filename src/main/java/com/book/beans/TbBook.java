package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_book")
@Data
public class TbBook {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String author;
    private Double price;
    private Integer number;
    private String publisher;
    private Long typeId;
    private Integer borrowNumber;
}
