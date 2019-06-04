package top.starrysea.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum FileType {

	IMG("FFD8FF", "89504E47", "47494638"), PDF("255044462D312E");

	private String[] magicNums;

	private static final Map<String, String> fileTypeMap;

	static {
		Map<String, String> map = new HashMap<>();
		map.put("FFD8FF", "jpg");
		map.put("89504E47", "png");
		map.put("47494638", "gif");
		map.put("255044462D312E", "pdf");
		fileTypeMap = Collections.unmodifiableMap(map);
	}

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
		throw new IllegalArgumentException("文件类型不正确");
	}
}
