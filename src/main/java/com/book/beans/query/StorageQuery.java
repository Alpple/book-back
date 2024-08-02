package com.book.beans.query;

import com.book.beans.TbBook;
import com.book.beans.TbStorage;
import com.book.config.exception.GeneralException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class StorageQuery {
    Long bookId;
    Integer number;
    Long empId;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    Timestamp time;

    Boolean isCreateNewBook;

    String name;
    String author;
    Double price;
    String publisher;
    Long typeId;

    public TbBook toNewBook() {
        TbBook b = new TbBook();
        b.setName(name);
        b.setAuthor(author);
        b.setPrice(price);
        b.setNumber(number);
        b.setPublisher(publisher);
        b.setTypeId(typeId);
        return b;
    }

    public TbStorage toStorage() {
        if (number == null || number == 0) {
            throw new GeneralException("数量不能为0");
        }
        if (empId == null) {
            throw new GeneralException("操作人不能为空");
        }
        TbStorage storage = new TbStorage();
        storage.setTime(time);
        storage.setNumber(number);
        storage.setEmpId(empId);
        storage.setBookId(bookId);
        return storage;
    }

}
