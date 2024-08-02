package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_root")
public class TbRoot {
    @TableId(type = IdType.AUTO)
    Long id;
    String account;
    String password;
}
