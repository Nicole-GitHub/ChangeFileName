package com;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeFileNameBat {
	private static final String className = ChangeFileNameBat.class.getName();
	
	/**
	 * 修改檔案名稱(批次)
	 * @param outputPath
	 * @throws Exception
	 */
	public static void run(String outputPath) throws Exception {
		try {
			List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
			Map<String, String> map = new HashMap<String, String>();

			String[] txtArr = Tools.readFileContent(outputPath + "ChangeFileNameBat.txt").split("\r\n");
			for (String str : txtArr) {
//System.out.println(str);
				if (str != null && str.length() > 3 && str.contains(":")) {
					String[] strArr = str.split(":");
					if ("folderName".equals(strArr[0]))
						map = new HashMap<String, String>();
					map.put(strArr[0], strArr.length > 1 ? strArr[1] : "");
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
						f.renameTo(new File(outputPath + folderName + change2FileName + " - " + videoSourceName + " - "
								+ videoStartNum++ + "." + aux));
					else
						throw new Exception("no file:" + f);
				}

				System.out.println("已完成檔案: " + fileNameOri);
			}
		} catch (Exception ex) {
			throw new Exception(className + " run Error: \n" + ex);
		}
	}
}
