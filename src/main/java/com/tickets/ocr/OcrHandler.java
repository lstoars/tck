package com.tickets.ocr;

import java.io.File;

public interface OcrHandler {
	/**
	 * ͼƬ�к��ֵ�ʶ��
	 * 
	 * @param imageFile
	 * @param imageFormat
	 * @return
	 */
	public String recognizeText(File imageFile, String imageFormat)
			throws Exception;
}
