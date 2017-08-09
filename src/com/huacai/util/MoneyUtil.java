package com.huacai.util;

import java.math.BigDecimal;

public class MoneyUtil {
	   /**金额为分的格式 */    
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";   
	
	 public static String to_yuan(String amount) throws Exception{    
	        if(!amount.matches(CURRENCY_FEN_REGEX)) {    
	            throw new Exception("金额格式有误");    
	        }    
	        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();    
	    }     

}

