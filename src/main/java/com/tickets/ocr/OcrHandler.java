package com.tickets.ocr;

import java.io.File;

public interface OcrHandler {
	/**
	 * 图片中汉字的识别
	 * 
	 * @param imageFile
	 * @param imageFormat
	 * @return
	 */
	public String recognizeText(File imageFile, String imageFormat)
			throws Exception;
}
