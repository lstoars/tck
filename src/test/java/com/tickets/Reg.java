package com.tickets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str ="<input type=\"hidden\" name=\"org.apache.struts.taglib.html.TOKEN\" value=\"b0359ff3d6ecb429322ac06016b09740\"></div>";
		Pattern p = Pattern
				.compile("[a-zA-Z0-9]{32}");
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			System.out.println(m.group());
		}

	}

}
