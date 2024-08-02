package com.book.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_type")
@Data
public class TbType {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String name;
  private Integer number;

}
