package com.augurit.agcloud.agcom.agsupport.util;

import com.common.locate.LocatePattern;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class Sim {

	private static Sim _this = new Sim();
	
	public NumberUtil numberUtil = new NumberUtil();
	
	private Sim() {
	}
	
	class NumberUtil {
		/*
		 * 基本数字单位;
		 */
		private String[] units = { "千", "百", "十", "" };// 个位
		/*
		 * 大数字单位;
		 */
		private String[] bigUnits = { "万", "亿" };
		/*
		 * 中文数字;
		 */
		private char[] numChars = { '一', '二', '三', '四', '五', '六', '七', '八', '九' };

		private char numZero = '零';

		private char[] allChars = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十' };

		private Pattern cNnumPattern = Pattern.compile("([零一二三四五六七八九十百万千亿]{2,}|[零一二三四五六七八九]+)");

		private Pattern cAlabPattern = Pattern.compile("[1234567890]");

		/**
		 * 用于考虑地址匹配中混杂中文数字问题
		 * 将含中文数字的字符串转换成数字
		 * @param sentence
		 * @author hunter
		 */
		public String numberCN2ArabSentence(String sentence) {
			if (sentence.matches("\\d+"))
				return sentence; //纯数字就直接返回
			sentence = arab2numberCN(sentence); //将数字转成中文字符串
			Matcher m = cNnumPattern.matcher(sentence);
			StringBuffer b = new StringBuffer();
			while (m.find()) {
				m.appendReplacement(b, numberCN2Arab(m.group(0)));
			}
			m.appendTail(b);
			return b.toString();
		}

		public boolean checkIsCnNumber(String content) {
			boolean flag = false;
			for (char c : allChars) {
				if (content.indexOf(c) != -1) {
					flag = true;
					break;
				}
			}
			return flag;
		}

		/**
		 * 将阿拉伯数字转中文数字;
		 *
		 * @param numberCN
		 * @return
		 */
		public String arab2numberCN(String numberCN) {
			//先把阿拉伯数字转成汉字数字
			Matcher m = cAlabPattern.matcher(numberCN);
			StringBuffer b = new StringBuffer();
			while (m.find()) {
				m.appendReplacement(b, numberArab2CN(Integer.parseInt(m.group(0))));
			}
			m.appendTail(b);
			return b.toString();
		}

		/**
		 * 将中文数字转换为阿拉伯数字;
		 *
		 * @param numberCN
		 * @return
		 */
		public String numberCN2Arab(String numberCN) {
			String tempNumberCN = numberCN;
			String tempResultNum = "";
			if (tempNumberCN == null) {
				return "0";
			}
			/*
			 * nums[0] 保存以千单位; nums[1] 保存以万单位; nums[2] 保存以亿单位;
			 */
			String[] nums = new String[bigUnits.length + 1];

			//千位以内,直接处理;
			nums[0] = tempNumberCN;

			/*
			 * 分割大数字,以千为单位进行运算;
			 */
			for (int i = (bigUnits.length - 1); i >= 0; i--) {

				// 是否存在大单位(万,亿...);
				int find = tempNumberCN.indexOf(bigUnits[i]);

				if (find != -1) {
					String[] tempStrs = tempNumberCN.split(bigUnits[i]);

					//清空千位内容;
					if (nums[0] != null) {
						nums[0] = null;
					}

					if (tempStrs.length > 0 && tempStrs[0] != null) {
						nums[i + 1] = tempStrs[0];
					}

					if (tempStrs.length > 1) {
						tempNumberCN = tempStrs[1];

						if (i == 0) {
							nums[0] = tempStrs[1];
						}

					} else {
						//tempNumberCN = null;
						break;
					}
				}
			}
			for (int i = nums.length - 1; i >= 0; i--) {
				if (nums[i] != null) {
					tempResultNum += numberKCN2Arab(nums[i]);
				} else {
					tempResultNum += "0000";
				}
			}
			String reslut = tempResultNum.replaceAll("^0*", "");
			if (reslut.length() == 0) {
				reslut = tempNumberCN;
			}
			return reslut;
		}

		/**
		 * 将一位中文数字转换为一位数字; eg: 一 返回 1;
		 *
		 * @param onlyCNNumber
		 * @return
		 */
		public int numberCharCN2Arab(char onlyCNNumber) {

			if (numChars[0] == onlyCNNumber) {
				return 1;
			} else if (numChars[1] == onlyCNNumber || onlyCNNumber == '两') {// 处理中文习惯用法(二,两)
				return 2;
			} else if (numChars[2] == onlyCNNumber) {
				return 3;
			} else if (numChars[3] == onlyCNNumber) {
				return 4;
			} else if (numChars[4] == onlyCNNumber) {
				return 5;
			} else if (numChars[5] == onlyCNNumber) {
				return 6;
			} else if (numChars[6] == onlyCNNumber) {
				return 7;
			} else if (numChars[7] == onlyCNNumber) {
				return 8;
			} else if (numChars[8] == onlyCNNumber) {
				return 9;
			}

			return 0;
		}

		/**
		 * 将一位数字转换为一位中文数字; eg: 1 返回 一;
		 *
		 * @param onlyArabNumber
		 * @return
		 */
		public char numberCharArab2CN(char onlyArabNumber) {

			if (onlyArabNumber == '0') {
				return numZero;
			}

			if (onlyArabNumber > '0' && onlyArabNumber <= '9') {
				return numChars[onlyArabNumber - '0' - 1];
			}

			return onlyArabNumber;
		}

		/**
		 *
		 * @param num
		 * @return
		 */
		public String numberArab2CN(Integer num) {

			String tempNum = num + "";

			// 传说中的分页算法;
			int numLen = tempNum.length();
			int start = 0;
			int end = 0;
			int per = 4;
			int total = (numLen + per - 1) / per;
			int inc = numLen % per;

			/*
			 * 123,1234,1234 四位一段,进行处理;
			 */
			String[] numStrs = new String[total];

			for (int i = total - 1; i >= 0; i--) {
				start = (i - 1) * per + inc;

				if (start < 0) {
					start = 0;
				}

				end = i * per + inc;

				numStrs[i] = tempNum.substring(start, end);
			}

			String tempResultNum = "";

			int rempNumsLen = numStrs.length;
			for (int i = 0; i < rempNumsLen; i++) {

				// 小于1000补零处理;
				if (i > 0 && Integer.parseInt(numStrs[i]) < 1000) {
					tempResultNum += numZero + numberKArab2CN(Integer.parseInt(numStrs[i]));
				} else {
					tempResultNum += numberKArab2CN(Integer.parseInt(numStrs[i]));
				}

				// 加上单位(万,亿....)
				if (i < rempNumsLen - 1) {
					tempResultNum += bigUnits[rempNumsLen - i - 2];
				}

			}

			// 去掉未位的零
			//tempResultNum = tempResultNum.replaceAll(numZero + "$", "");

			return tempResultNum;
		}

		/**
		 * 将千以内的数字转换为中文数字;
		 *
		 * @param num
		 * @return
		 */
		private String numberKArab2CN(Integer num) {

			char[] numChars = (num + "").toCharArray();

			String tempStr = "";

			int inc = units.length - numChars.length;

			for (int i = 0; i < numChars.length; i++) {
				if (numChars[i] != '0') {
					tempStr += numberCharArab2CN(numChars[i]) + units[i + inc];
				} else {
					tempStr += numberCharArab2CN(numChars[i]);
				}
			}

			// 将连续的零保留一个
			tempStr = tempStr.replaceAll(numZero + "+", numZero + "");

			// 去掉未位的零
			//tempStr = tempStr.replaceAll(numZero + "$", "");

			return tempStr;

		}

		/**
		 * 处理千以内中文数字,返回4位数字字符串,位数不够以"0"补齐;
		 *
		 * @param numberCN
		 * @return
		 */
		private String numberKCN2Arab(String numberCN) {
			if ("".equals(numberCN)) {
				return "";
			}
			int[] nums = new int[4];

			if (numberCN != null) {

				for (int i = 0; i < units.length; i++) {
					int idx = numberCN.indexOf(units[i]);

					if (idx > 0) {
						char tempNumChar = numberCN.charAt(idx - 1);

						int tempNumInt = numberCharCN2Arab(tempNumChar);

						nums[i] = tempNumInt;
					}
				}
				//处理十位
				if (numberCN.indexOf("十") > 0) {
					char ones = numberCN.charAt(numberCN.length() - 1);
					nums[nums.length - 1] = numberCharCN2Arab(ones);
				}
				// 处理个位
				if ((numberCN.length() == 2 || numberCN.length() == 1) && numberCN.charAt(0) == '十') {
					nums[nums.length - 2] = 1;
				}
			}

			// 返回结果
			String tempNum = "";
			for (int i = 0; i < nums.length; i++) {
				tempNum += nums[i];
			}
			tempNum = tempNum.replaceAll("^0*", "");
			if (tempNum.length() == 0) {
				for (int i = 0; i < numberCN.length(); i++) {
					char tem = numberCN.charAt(i);
					tempNum += numberCharCN2Arab(tem);
				}
			}
			return tempNum;
		}
	}
	
	private int min(int one, int two, int three) {
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		return min;
	}

	private int ld(String str1, String str2) {
		int d[][]; //矩阵
		int n = str1.length();
		int m = str2.length();
		int i; //遍历str1的
		int j; //遍历str2的
		char ch1; //str1的
		char ch2; //str2的
		int temp; //记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { //初始化第一列
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) { //初始化第一行
			d[0][j] = j;
		}
		for (i = 1; i <= n; i++) { //遍历str1
			ch1 = str1.charAt(i - 1);
			//去匹配str2
			for (j = 1; j <= m; j++) {
				ch2 = str2.charAt(j - 1);
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				//左边+1,上边+1, 左上角+temp取最小	            
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);

			}
		}
		return d[n][m];
	}

	/**
	 * 采用预编译加快匹配速度
	 */
	private Pattern regex_1 = Pattern.compile("(\\(|（).*(\\)|）)");
	private Pattern regex_2 = Pattern.compile("\\d");
	private Pattern regex_3 = Pattern.compile("[^\\d]");
	private Pattern regex_4 = Pattern.compile("[^东南西北新旧大小上下左右]");

	/**
	 * 算出相似度分数
	 * @param str 关键字
	 * @param idx 搜索结果
	 * @return
	 */
	private double sim(String str, String idx) {
		if (idx == null || idx.trim().length() == 0) return 0;
		
		str = this.numberUtil.numberCN2ArabSentence(str);
		idx = this.numberUtil.numberCN2ArabSentence(idx);
		
		str = regex_1.matcher(str).replaceAll("");
		idx = regex_1.matcher(idx).replaceAll("");

		String cnStr1 = regex_2.matcher(str).replaceAll("");
		String cnStr2 = regex_2.matcher(idx).replaceAll("");
		//去数字匹配算法得到匹配分数
		double score = sim2(cnStr1, cnStr2);
		//全字符串按"区","村","路","街"匹配
		score = sim4(str, idx, score);
		if (score >= 0.95) {
			String numStr1 = regex_3.matcher(str).replaceAll("");
			String numStr2 = regex_3.matcher(idx).replaceAll("");
			sim2(str, idx);
			if (!numStr1.equals(numStr2)) {
				if (numStr1.contains(numStr2)) {
					score = score * 0.1 + 0.9;
				} else if (numStr2.contains(numStr1)) {
					score = score * 0.1 + 0.8;
				} else {
					score = 0;
				}
			}
		} else if (score > 0.5) {
			String numStr1 = regex_3.matcher(str).replaceAll("");
			String numStr2 = regex_3.matcher(idx).replaceAll("");
			double numScore = sim2(numStr1, numStr2);
			if (!numStr1.equals(numStr2)) {
				if (!numStr1.matches("^" + numStr2 + ".*")) {
					numScore = numScore / 4;
				} else {
					numScore = 0.7;
				}
			}
			numStr1 = regex_4.matcher(str).replaceAll("");
			numStr2 = regex_4.matcher(idx).replaceAll("");
			numScore = numScore * sim2(numStr1, numStr2);
			score = score * 0.2 + numScore * 0.8;
		}
		int ld = ld(str, idx);//得到不匹配的字符数
		double score_ = 1 - (double) ld / Math.max(str.length(), idx.length());//得到匹配率
		score = score*(1-0.618)*score_ + score*0.618; 
		return score;
	}

	private double sim2(String str, String idx) {
		if (str.equals(idx))
			return 1;
		double score = 1;
		
		if (str.lastIndexOf(idx) != -1) {
			//用来匹配的字段包含索引被门牌字段的情况下分数直接定义为90分以上
			if (score < 0.95) {
				score = score * 0.1 + 0.9;
			}
		} else if (idx.lastIndexOf(str) != -1) {
			//用来匹配的字段被索引被门牌字段包含的情况下分数直接定义为80分以上
			if (score < 0.8) {
				score = score * 0.1 + 0.8;
			}
		}
		if (str.length() == idx.length() && str.length() > 0) {
			//用来匹配的字段长度与索引被门牌字段长度相等情况下情况下若第一个字与索引第一个字不相等则所得分数需要除4
			if (str.charAt(0) != idx.charAt(0)) {
				score = score / 4;
			}
		}
		return score;
	}

	private LocatePattern pattern = new LocatePattern();
	
	private double sim4(String str, String idx, double score) {
		for (Iterator it = pattern.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			String key = (String)entry.getKey();
			Object[] array = (Object[])entry.getValue();
			Pattern pattern = (Pattern)array[0];
			double right = Double.valueOf((String)array[1]);
			score = sim3(str, idx, key, pattern, right, score);
		}
		return score;
	}
	
	private double sim3(String str, String idx, String key, Pattern regex, double right, double score) {
		if (str.contains(key) && idx.contains(key)) {
			String str1 = regex.matcher(str).replaceAll("$1");
			String str2 = regex.matcher(idx).replaceAll("$1");
			if (!str1.equals(str2)) {
				//给分数低的给不同区的提分 以下略同
				if (str1.lastIndexOf(str2) != -1 && !str1.matches("\\d+[^\\d]+")) {
					if (score < 0.6) {
						score = score * 1.2;
					}
				} else if (str2.lastIndexOf(str1) != -1 && !str2.matches("\\d+[^\\d]+")) {
					if (score < 0.3) {
						score = score * 1.7;
					}
				} else {
					score = score * (1 - right);
				}
			}
		} else if (idx.contains(key)) {
			//如果标准地址有 而 搜索地址没有 就扣分
			score = score * 0.95;
		}
		return score;
	}
	
	/**
	 * 地址匹配接口
	 * @return 0-100的匹配分值
	 */
	public static int same(String str, String indx_) {
		return Integer.parseInt(String.valueOf(_this.sim(str, indx_)*100).replaceAll("(\\.\\d+)", ""));
	}
	
	public static void main(String[] args) {
		String str1 = "广东省珠海市斗门区井岸镇统建二街11号";
		String str2 = "广东省珠海市斗门区井岸镇统建二街11号5-栋";
		System.out.println(Sim.same(str1, str2));
	}

}
