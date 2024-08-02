package com.book.beans.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.book.beans.TbBook;
import com.book.beans.TbType;
import com.book.mapper.TbTypeMapper;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class BookQuery {
    private Long id;
    private String name;
    private String author;
    private String publisher;
    private String typeName;

    BookQuery() {

    }

    public TbBook toTb() {
        TbBook tbBook = new TbBook();
        tbBook.setId(this.id);
        tbBook.setName(this.name);
        tbBook.setAuthor(this.author);
        tbBook.setPublisher(this.publisher);
        return tbBook;
    }

    public boolean wrapper(LambdaQueryWrapper<TbBook> wrapper, TbTypeMapper mapper) {
        if (typeName != null) {
            List<TbType> list = mapper.selectList(Wrappers.<TbType>lambdaQuery().eq(TbType::getName, typeName));
            if (list.isEmpty()) {
                wrapper.eq(TbBook::getId, -1);
                return false;
            }
            wrapper.in(TbBook::getTypeId, list.stream().map(TbType::getId).collect(Collectors.toList()));
        }
        TbBook tbBook = this.toTb();
        if (tbBook.getName()!=null) {
            wrapper.like(TbBook::getName,tbBook.getName());
            tbBook.setName(null);
        }
        wrapper.setEntity(tbBook);

        return true;
    }

}
