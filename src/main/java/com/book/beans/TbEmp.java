package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_emp")
@Data
public class TbEmp {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String sex;
    private String account;
    private String password;
    private String tel;
    Boolean enabled;

}
