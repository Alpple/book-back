package com.book;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.mapper.TbBookMapper;
import com.book.service.IBookService;
import com.book.service.ITypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookApplicationTests {
	public static void main(String[] args) {
		System.out.println("/register/reader/zu11".matches("/register/.*"));
	}
	@Autowired
	ITypeService typeService;

	@Autowired
	IBookService bookService;
	@Autowired
	TbBookMapper bookMapper;

	@Test
	public void testSelect() {
		System.out.println("bookService.list() = " + bookService.pageVO(new Page<>(1,2),null).getRecords());

	}


}
