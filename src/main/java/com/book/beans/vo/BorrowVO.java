package com.book.beans.vo;

import com.book.beans.TbBorrow;
import com.book.beans.TbReader;
import lombok.Data;

@Data
public class BorrowVO {
    BookVO book;
    TbBorrow borrow;
    TbReader reader;
 }
