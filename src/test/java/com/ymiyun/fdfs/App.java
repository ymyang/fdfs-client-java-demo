package com.ymiyun.fdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class App {

	private static TrackerClient tracker = null;
	private static TrackerServer trackerServer = null;

	static {
		try {
			ClientGlobal.init(App.class.getClassLoader().getResource("fdfs_client.conf").getFile());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static final StorageClient getStorageClient() throws IOException {
		try {
			if (tracker == null) {
				tracker = new TrackerClient();
			}
			if (trackerServer == null) {
				trackerServer = tracker.getConnection();
			}
		} catch (Exception ex) {
			tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
		}
		return new StorageClient(trackerServer, null);
	}

	public static void main(String[] args) {
		try {
			String file = "d:/test.jpg";
			
//			String r1 = test_upload(file);
//			System.out.println(r1);
//			
//			String r2 = test_append(file);
//			System.out.println(r2);
//			
//			String r3 = test_modify(file);
//			System.out.println(r3);
			
			test_fileinfo();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void test_fileinfo() throws Exception {
		FileInfo f = getStorageClient().get_file_info("group1", "M00/00/0C/wKgBeFXclx2AMt8jAAPm5H9JxDA307.jpg");
		System.out.println("crc32:" + f.getCrc32());
	}

	private static String test_upload(String file) throws Exception {
		String[] r = getStorageClient().upload_file(file, null, null);
		return r[0] + "/" + r[1];
	}

	private static String test_append(String filePath) throws Exception {
		int blockSize = 10 * 1024;
		File file = new File(filePath);
		long fileSize = file.length();
		FileInputStream fs = new FileInputStream(file);
		long uploadedSize = 0;
		String group_name = "";
		String appender_filename = "";
		try {
			while (uploadedSize < fileSize) {
				long end = uploadedSize + blockSize;
				if (end > fileSize) {
					end = fileSize;
				}

				int bSize = (int) (end - uploadedSize);

				byte[] buff = new byte[bSize];
				fs.read(buff, 0, bSize);

				if (uploadedSize == 0) {
					String[] arr = getStorageClient().upload_appender_file(buff, "jpg", null);
					group_name = arr[0];
					appender_filename = arr[1];
					System.out.println(group_name + "/" + appender_filename);
				} else {
					getStorageClient().append_file(group_name, appender_filename, buff);
				}
				uploadedSize += bSize;
			}
		} finally {
			fs.close();
		}
		return group_name + "/" + appender_filename;
	}

	private static String test_modify(String filePath) throws Exception {
		int blockSize = 10 * 1024;
		File file = new File(filePath);
		long fileSize = file.length();
		FileInputStream fs = new FileInputStream(file);
		long uploadedSize = 0;
		String group_name = "";
		String appender_filename = "";
		try {
			while (uploadedSize < fileSize) {
				long end = uploadedSize + blockSize;
				if (end > fileSize) {
					end = fileSize;
				}

				int bSize = (int) (end - uploadedSize);

				byte[] buff = new byte[bSize];
				fs.read(buff, 0, bSize);

				if (uploadedSize == 0) {
					String[] arr = getStorageClient().upload_appender_file(buff, "jpg", null);
					group_name = arr[0];
					appender_filename = arr[1];
					System.out.println(group_name + "/" + appender_filename);
				} else {
					getStorageClient().modify_file(group_name, appender_filename, uploadedSize, buff);
				}
				uploadedSize += bSize;
			}
		} finally {
			fs.close();
		}
		return group_name + "/" + appender_filename;
	}
}
