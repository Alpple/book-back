package com.book.beans.vo;

import com.book.beans.TbEmp;
import com.book.beans.TbStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageVO {
    TbStorage storage;
    TbEmp emp;
    BookVO book;
}
