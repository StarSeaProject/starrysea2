package top.starrysea.file;

import java.util.HashMap;
import java.util.Map;

public enum FileType {

	IMG("FFD8FF", "89504E47", "47494638"), PDF("255044462D312E");

	private String[] magicNums;

	private Map<String, String> fileTypeMap = new HashMap<String, String>() {{
		put("FFD8FF", "jpg");
		put("89504E47", "png");
		put("47494638", "gif");
		put("255044462D312E", "pdf");
	}};

	private FileType(String... magicNums) {
		this.magicNums = magicNums;
	}

	public boolean contains(String targetMagicNum) {
		for (String magicNum : magicNums) {
			if (targetMagicNum.startsWith(magicNum)) {
				return true;
			}
		}
		return false;
	}

	public String getTrueFileType(String targetMagicNum) {
		for (String magicNum : magicNums) {
			if (targetMagicNum.startsWith(magicNum)) {
				return fileTypeMap.get(magicNum);
			}
		}
		return "blob";
	}
}
