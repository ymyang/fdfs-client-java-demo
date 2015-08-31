package com.ymiyun.fdfs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class CRC32Test {

	public static void main(String[] args) {
		try {
			System.out.println(crc32("d:/test-4.jpg"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static long crc32(String file) throws IOException {
		FileInputStream is = null;
		CheckedInputStream cis = null;
		try {
			is = new FileInputStream(file);
			CRC32 crc32 = new CRC32();
			cis = new CheckedInputStream(is, crc32);
			while (cis.read() != -1) {
			}
			return crc32.getValue();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			if (cis != null) {
				try {
					cis.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
