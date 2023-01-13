package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		// 判斷當前執行的啟動方式是IDE還是jar
		boolean isStartupFromJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).isFile();
		System.out.println("isStartupFromJar: " + isStartupFromJar);

		String changeType = "";
		String path = System.getProperty("user.dir") + File.separator; // Jar
		if(!isStartupFromJar) // IDE
			path = System.getProperty("os.name").contains("Mac") ? "/Users/nicole/Downloads/" // Mac
					: "D:/Downloads/"; // Win
			changeType = "1";
		
		/**
		 * 透過windows的cmd執行時需將System.in格式轉為big5才不會讓中文變亂碼
		 * 即使在cmd下chcp 65001轉成utf-8也沒用
		 * 但在eclipse執行時不能轉為big5
		 */
		Scanner s = null;
		try {
			s =  isStartupFromJar ? new Scanner(System.in, "big5") : new Scanner(System.in);
			System.out.println("請輸入修改類型(1:單一 2:批次): ");
			changeType = "".equals(changeType) ? s.nextLine() : changeType;
		}catch(Exception ex) {
		}finally {
			if(s != null) s.close();
		}
		
		if("1".equals(changeType))
			changeFileName(path);
		else
			changeFileNameBat(path);
			
	}
	
	/**
	 * 讀取檔案內容並轉成utf-8
	 * @param filePathName
	 * @return
	 * @throws IOException
	 */
	public static String readFileContent(String filePathName) throws IOException {
		String temp = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			File file = new File(filePathName);// 檔案路徑(包括檔名稱)
			// 將檔案內容讀入輸入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			// 讀取檔案內容
			while((temp = br.readLine()) != null) {
				buffer.append(temp);
				// 行與行之間的分隔符 相當於“\n”
				buffer.append(System.getProperty("line.separator"));
			}
			
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) br.close();
			if (isr != null) isr.close();
			if (fis != null) fis.close();
		}
		return "";
	}
	
	/**
	 * 修改檔案名稱(批次)
	 * @param outputPath
	 * @throws Exception
	 */
	public static void changeFileNameBat(String outputPath) throws Exception {

		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		
		String[] txtArr = readFileContent(outputPath+"changeFileNameBat.txt").split("\r\n");
		for (String str : txtArr) {
//System.out.println(str);
			if (str != null && str.length() > 3 && str.contains(":")) {
				String[] strArr = str.split(":");
				if ("folderName".equals(strArr[0]))
					map = new HashMap<String, String>();
				map.put(strArr[0], strArr[1]);
			} else {
				mapList.add(map);
			}
		}
		mapList.add(map);
		
		for (Map<String, String> forMap : mapList) {
			String folderName = forMap.get("folderName").toString();
			String fileOriName = forMap.get("fileOriName").toString();
			String videoSourceName = forMap.get("videoSourceName").toString();
			int videoCount = Integer.parseInt(forMap.get("videoCount").toString());
			String change2FileName = forMap.get("change2FileName").toString();
			int videoStartNum = Integer.parseInt(forMap.get("videoStartNum").toString());

//System.out.println("folderName:" + folderName + ",\r\n fileOriName:" + fileOriName
//		+ ",\r\n videoSourceName:" + videoSourceName + ",\r\n videoCount:" + videoCount
//		+ ",\r\n change2FileName:" + change2FileName + ",\r\n videoStartNum:" + videoStartNum);
			
			folderName += (folderName.length() > 3 && !folderName.contains("/")) ? "/" : "";

			String fileNameOri = fileOriName.substring(0, fileOriName.lastIndexOf("."));
			String aux = fileOriName.substring(fileOriName.lastIndexOf(".") + 1);

			for (int i = 0; i < videoCount; i++) {
				if (i == 0) {
					fileOriName = fileNameOri + "." + aux;
				} else {
					fileOriName = fileNameOri + " (" + i + ")." + aux;
				}
				File f = new File(outputPath + folderName + fileOriName);
				if (f.exists())
					f.renameTo(new File(
							outputPath + folderName + change2FileName + " - " + videoSourceName + " - " + videoStartNum++ + "." + aux));
				else
					throw new Exception("no file:" + f);
			}

			System.out.println("已完成檔案: " + fileNameOri);
		}
	}

	/**
	 * 修改檔案名稱
	 * @param outputPath
	 * @throws Exception
	 */
	public static void changeFileName(String outputPath) throws Exception {

		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		
		String[] txtArr = readFileContent(outputPath+"changeFileName.txt").split("\r\n");
		for (String str : txtArr) {
//System.out.println(str);
			if (str != null && str.length() > 3 && str.contains(":")) {
				String[] strArr = str.split(":");
				if ("folderName".equals(strArr[0]))
					map = new HashMap<String, String>();
				map.put(strArr[0], strArr[1]);
			} else {
				mapList.add(map);
			}
		}
		mapList.add(map);
		
		for (Map<String, String> forMap : mapList) {
			String folderName = forMap.get("folderName").toString();
			String fileOriName = forMap.get("fileOriName").toString();
			String videoSourceName = forMap.get("videoSourceName").toString();
			int videoCount = Integer.parseInt(forMap.get("videoCount").toString());
			String change2FileName = forMap.get("change2FileName").toString();
			int videoStartNum = Integer.parseInt(forMap.get("videoStartNum").toString());

//System.out.println("folderName:" + folderName + ",\r\n fileOriName:" + fileOriName
//		+ ",\r\n videoSourceName:" + videoSourceName + ",\r\n videoCount:" + videoCount
//		+ ",\r\n change2FileName:" + change2FileName + ",\r\n videoStartNum:" + videoStartNum);
			
			folderName += (folderName.length() > 3 && !folderName.contains("/")) ? "/" : "";

			String fileNameOri = fileOriName.substring(0, fileOriName.lastIndexOf("."));
			String aux = fileOriName.substring(fileOriName.lastIndexOf(".") + 1);

			for (int i = 0; i < videoCount; i++) {
				if (i == 0) {
					fileOriName = fileNameOri + "." + aux;
				} else {
					fileOriName = fileNameOri + " (" + i + ")." + aux;
				}
				File f = new File(outputPath + folderName + fileOriName);
				if (f.exists())
					f.renameTo(new File(
							outputPath + folderName + change2FileName + " - " + videoSourceName + " - " + videoStartNum++ + "." + aux));
				else
					throw new Exception("no file:" + f);
			}

			System.out.println("已完成檔案: " + fileNameOri);
		}
	}
}
