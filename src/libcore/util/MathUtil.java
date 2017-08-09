package libcore.util;

import java.util.Arrays;



/**
 * @author Andyfoo
 * 
 * 
 * 功能说明：请补充
 */
public class MathUtil {


	
	// 取随机数
	/*
	 * 生成 min-max之间的数字， 包括min,max 
	 * 
	 */
	public static int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}
	// 生成随机数的数组，
	public static int[] getUniqueRandArray(int num, int min, int max) {
		int[] sel = new int[num];
		int n = 0;
		for (int i = 0; i < num; i++) {
			do {
				n = getRand(min, max);
			} while (ArrayUtil.inArray(n, sel));
			sel[i] = n;
		}
		return sel;
	}

	// 生成随机数的数组，并排序
	public static int[] getUniqueRandArray(int num, int min, int max, String sort) {
		int[] sel = getUniqueRandArray(num, min, max);
		if (sort.equals("ASC")) {
			Arrays.sort(sel);
			int[] sel2 = new int[sel.length];
			for (int i = sel.length; i > 0; i--) {
				sel2[sel.length - i] = sel[i - 1];
			}
			return sel;
		} else {
			Arrays.sort(sel);
			return sel;
		}
	}

	// 生成随机数字符串的数组，
	public static String[] getUniqueStringRandArray(int num, int len, int min, int max) {
		int[] sel = getUniqueRandArray(num, min, max);
		String[] result = new String[sel.length];
		for (int i = 0; i < sel.length; i++) {
			result[i] = StringUtil.strPad(Integer.toString(sel[i]), len, "0");
		}
		return result;
	}

	// 生成随机数字符串的数组，并排序
	public static String[] getUniqueStringRandArray(int num, int len, int min, int max, String sort) {
		int[] sel = getUniqueRandArray(num, min, max, sort);
		String[] result = new String[sel.length];
		for (int i = 0; i < sel.length; i++) {
			result[i] = StringUtil.strPad(Integer.toString(sel[i]), len, "0");
		}
		return result;
	}

}
 