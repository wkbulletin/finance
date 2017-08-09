/**
 * 类名      		
 * 创建日期 		2008-10-15
 * 作者  		Andyfoo	
 * 审核人     
 */
package libcore.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andyfoo
 * 
 * 
 *         功能说明：请补充
 */
public class ArrayUtil {
	// 连接字符串 ArrayList
	public static String join(String separator, List<?> arr) {
		int len = arr.size();
		String result = "";
		for (int i = 0; i < len; i++) {
			result += arr.get(i) + separator;
		}
		if (result.length() > 0)
			result = result.substring(0, result.length() - separator.length());
		return result;
	}

	// 连接字符串 ArrayList
	public static String join(String separator, ArrayList<?> arr) {
		int len = arr.size();
		String result = "";
		for (int i = 0; i < len; i++) {
			result += arr.get(i) + separator;
		}
		if (result.length() > 0)
			result = result.substring(0, result.length() - separator.length());
		return result;
	}

	// 连接字符串
	public static String join(String separator, String[] arr) {
		int len = arr.length;
		String result = "";
		for (int i = 0; i < len; i++) {
			result += arr[i] + separator;
		}
		if (result.length() > 0)
			result = result.substring(0, result.length() - separator.length());
		return result;
	}

	// 连接字符串
	public static String joinSqlIn(String[] arr) {
		String separator = ",";
		int len = arr.length;
		String result = "";
		for (int i = 0; i < len; i++) {
			result += "'" + arr[i] + "'" + separator;
		}
		if (result.length() > 0)
			result = result.substring(0, result.length() - separator.length());
		return result;
	}

	// 判断一个数字是否在数组中
	public static boolean inArray(int n, int[] arr) {
		if(arr==null)return false;
		for (int i = 0; i < arr.length; i++) {
			if (n == arr[i])
				return true;
		}
		return false;
	}

	// 判断一个字符串是否在数组中
	public static boolean inArray(String n, String[] arr) {
		if(arr==null)return false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(n))
				return true;
		}
		return false;
	}

	// 排序
	public static int[] sortAcs(int[] array) {
		if (array == null) {
			return null;
		}
		int[] srcdata = (int[]) array.clone();
		int size = array.length;
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++) {
				if (srcdata[i] > srcdata[j]) {
					swap(srcdata, i, j);
				}
			}
		return srcdata;
	}

	public static String[] sortAcs(String[] array) {
		int array2[] = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = Integer.valueOf(array[i]);
		}
		array2 = ArrayUtil.sortAcs(array2);
		for (int i = 0; i < array.length; i++) {
			array[i] = String.valueOf(array2[i]);
		}
		return array;
	}

	public static void swap(int[] srcdata, int src, int dest) {
		int temp = srcdata[src];
		srcdata[src] = srcdata[dest];
		srcdata[dest] = temp;
	}
}
