package com.govtech.esm.util;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

class CSVParserUtilTest {

	@Autowired
	CSVParserUtil csvParserUtil;
	
	@Test
	void testParseCSVFile() throws IOException {

		/*
		 * try {
		 * 
		 * 
		 * MockMultipartFile fileName = new MockMultipartFile("fileName", "fileName",
		 * "text/csv", new
		 * ClassPathResource("userdetails_success.csv").getInputStream());
		 * 
		 * csvParserUtil.parseCSVFile(fileName);
		 * 
		 * }catch (Exception e) { e.printStackTrace(); }
		 */
	}

}
