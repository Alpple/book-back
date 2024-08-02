package com.book.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.book.beans.TbBook;
import com.book.beans.TbType;
import com.book.mapper.TbTypeMapper;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Data
public class BookVO {
    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private Double bookPrice;
    private Integer bookNumber;
    private String bookPublisher;
    private Long typeId;
    private Integer typeNumber;
    private String typeName;
    private Integer borrowNumber;


    private BookVO() {
    }

    public static BookVO create(TbBook book, TbTypeMapper typeMapper) {

        BookVO vo = new BookVO();
        vo.bookId = book.getId();
        vo.bookName = book.getName();
        vo.bookAuthor = book.getAuthor();
        vo.bookPrice = book.getPrice();
        vo.bookNumber = book.getNumber();
        vo.bookPublisher = book.getPublisher();
        vo.borrowNumber = book.getBorrowNumber();

        if (book.getTypeId() != null) {
            TbType type = typeMapper.selectById(book.getTypeId());
            vo.typeId = type.getId();
            vo.typeNumber = type.getNumber();
            vo.typeName = type.getName();
        }
        return vo;
    }
}
