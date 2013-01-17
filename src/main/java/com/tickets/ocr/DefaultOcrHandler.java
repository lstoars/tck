package com.tickets.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

import com.tickets.ocr.util.CommandExec;
import com.tickets.ocr.util.ImageIOHelper;

public class DefaultOcrHandler implements OcrHandler {

	@Override
	public String recognizeText(File imageFile, String imageFormat)
			throws Exception {
		File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
		if (tempImage == null) {
			return "";
		}
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer maybe = new StringBuffer();
		List<String> cmdList = new ArrayList<String>();
		if (OS.isWindows()) {
			cmdList.add(StaticInfo.TESS_PATH + "//tesseract");
		} else if (OS.isLinux()) {
			cmdList.add("tesseract");
		} else {
			cmdList.add(StaticInfo.TESS_PATH + "//tesseract");
		}
		cmdList.add("");
		cmdList.add(outputFile.getName());
		cmdList.add(StaticInfo.LANG_OPTION);
		cmdList.add("chi_sim");
		cmdList.add("eng");
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());
		cmdList.set(1, tempImage.getName());
		pb.command(cmdList);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		//初始化timeout的process
		CommandExec commandExec = new CommandExec(StaticInfo.TIME_OUT, StaticInfo.INTERCAL);
		Process processBefore = commandExec.exec(process);
		if (processBefore == null) {
			return "";
		}
		int w = process.waitFor();

		// 删除临时文件
		tempImage.delete();
		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputFile
					.getAbsolutePath()
					+ ".txt"), "UTF-8"));
			String str;
			while ((str = in.readLine()) != null) {
				maybe.append(str).append(StaticInfo.EOL);
			}
			in.close();
		}
		new File(outputFile.getAbsolutePath() + ".txt").delete();
		String mayBeStr = maybe.toString();
		String result = null;
		if(mayBeStr != null){
			result = mayBeStr.replaceAll("[\\s]", "");
		}
		return result;
	}

}
