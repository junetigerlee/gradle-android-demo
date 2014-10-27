package cn.com.incito.driver.util;

import java.util.UUID;

public class StringUtil {

	/**
	 * 判断字符串是否为空或null
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmptyOrNull(String string) {
		if (string == null) {
			return true;
		}
		if (string.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String filterString(String string) {
		if (isEmptyOrNull(string)) {
			return "";
		}
		return string.trim();
	}

	/**
	 * 获取随机UUID(数据表中的id值为UUID)
	 * 
	 * @return
	 */
	public static String getRandomUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * 往指定的json字符串中追加json字符串
	 * 
	 * @param json
	 * @param jsons
	 * @return
	 */
	public static String pakJson(String json, String... jsons) {
		StringBuilder sBuilder = new StringBuilder();
		json = json.substring(0, json.length() - 1);
		sBuilder.append(json);
		for (int i = 0; i < jsons.length; i++) {
			sBuilder.append(",");
			sBuilder.append(jsons[i].subSequence(1, jsons[i].length() - 1));
		}
		sBuilder.append("}");
		return sBuilder.toString();
	}

	/**
	 * 删除json字符串中的指定节点 <br>
	 * 该方法只能删除简单字符串中的普通节点，如只删除形如："key":"value",
	 * 
	 * @param json
	 *            要操作的字符串
	 * @param point
	 *            要移除的节点
	 * @return
	 */
	public static String removeJsonPoint(String json, String... points) {
		String result = json;
		int len = points.length;
		for (int i = 0; i < len; i++) {
			int index = json.indexOf("\"" + points[i] + "\"");
			result = json.substring(0, index); // remove point
			int index2 = json.indexOf("\",", index);
			result += json.substring(index2 + 2); // remove ",
			json = result;
		}
		return result;
	}

	/*
	 * 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 将字符串中的全角字符转为半角
	 * 
	 * @param src 要转换的包含全角的任意字符串
	 * 
	 * @return 转换之后的字符串
	 */
	public static String toSemiangle(String src) {

		char[] c = src.toCharArray();
		for (int index = 0; index < c.length; index++) {
			if (c[index] == 12288) { // 全角空格
				c[index] = (char) 32;
			} else if (c[index] > 65280 && c[index] < 65375) { // 其他全角字符
				c[index] = (char) (c[index] - 65248);
			}
		}
		return String.valueOf(c);
	}

}
