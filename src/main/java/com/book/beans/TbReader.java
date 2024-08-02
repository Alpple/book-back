package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_reader")
@Data
public class TbReader {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String sex;
    private String tel;
    private String account;
    private String password;
//    中午的number借阅册数
}
