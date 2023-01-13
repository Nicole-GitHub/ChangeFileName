package com;

import java.io.File;
import java.util.Scanner;

public class Main {
	private static final String className = Main.class.getName();

	public static void main(String[] args) throws Exception {

		// 判斷當前執行的啟動方式是IDE還是jar
		boolean isStartupFromJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).isFile();
		System.out.println("isStartupFromJar: " + isStartupFromJar);

		String changeType = "", folderName = "";
		String path = System.getProperty("user.dir") + File.separator; // Jar
		if(!isStartupFromJar) { // IDE
			path = System.getProperty("os.name").contains("Mac") ? "/Users/nicole/Downloads/" // Mac
//					: "D:/Downloads/"; // Win
					:"C:/Users/nicole_tsou/Downloads/"; // Win
			changeType = "1";
			folderName = "";
		}
		
		/**
		 * 透過windows的cmd執行時需將System.in格式轉為big5才不會讓中文變亂碼
		 * 即使在cmd下chcp 65001轉成utf-8也沒用
		 * 但在eclipse執行時不能轉為big5
		 */
		Scanner s = null;
		try {
			s = isStartupFromJar ? new Scanner(System.in, "big5") : new Scanner(System.in);
			System.out.println("請輸入修改類型(1:單一 2:批次): ");
			changeType = "".equals(changeType) ? s.nextLine() : changeType;
			if ("1".equals(changeType)) {
				System.out.println("若有下層目錄則請輸入目錄名稱(含/)，若無則留空: ");
				folderName = "".equals(folderName) ? s.nextLine() : folderName;
			}

			if ("1".equals(changeType))
				ChangeFileName.run(path, folderName);
			else
				ChangeFileNameBat.run(path);
		}catch(Exception ex) {
			throw new Exception(className + " main Error: \n" + ex);
		}finally {
			if(s != null) s.close();
		}
			
	}
}
