package com.gp.cong.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Lakshminarayanan
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    //common format is ###,###,##0.00
    public static float round(float amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String roundedAmount = formatter.format(amount);
        return Float.parseFloat(roundedAmount.replace(",", ""));
    }

    public static double round(double amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String roundedAmount = formatter.format(amount);
        return Double.parseDouble(roundedAmount.replace(",", ""));
    }

    public static String formatNumber(Object number, String pattern) {
        NumberFormat numberFormat = new DecimalFormat(pattern);
        return numberFormat.format(number);
    }

    public static String formatAmount(double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return numberFormat.format(amount);
    }

    public static String formatNumber(double number) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String numberValue = numberFormat.format(number).replace("$", "");
        return numberValue.contains("(") ? numberValue.replace("(", "-").replace(")", "") : numberValue;
    }

    public static double parseNumber(String number) {
        return null != number && !number.trim().isEmpty() ? Double.parseDouble(number.replace(",", "")) : 0d;
    }

    public static boolean isNotZero(Object number) {
        if (null != number) {
            if (number instanceof BigDecimal) {
                return ((BigDecimal)number).compareTo(BigDecimal.ZERO) != 0;
            }else if (number instanceof BigInteger) {
                return ((BigInteger)number).compareTo(BigInteger.ZERO) != 0;
            }else if (number instanceof Double) {
                return BigDecimal.valueOf((Double)number).compareTo(BigDecimal.ZERO) != 0;
            } else if (number instanceof Float) {
                return BigDecimal.valueOf((Float)number).compareTo(BigDecimal.ZERO) != 0;
            } else if (number instanceof Long) {
                return ((Long)number).compareTo(0L) != 0;
            } else if (number instanceof Integer) {
                return ((Integer)number).compareTo(0) != 0;
            } else if (number instanceof Short) {
                return (Short) number != 0;
            } else if (number instanceof Byte) {
                return (Byte) number != 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int compareTo(String dbl1, String dbl2) {
        if (null != dbl1 && null != dbl2) {
            BigDecimal bd1 = BigDecimal.valueOf(Double.valueOf(dbl1.replace(",", "")));
            BigDecimal bd2 = BigDecimal.valueOf(Double.valueOf(dbl2.replace(",", "")));
            return bd1.compareTo(bd2);
        } else if (null != dbl1) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int compareTo(Double dbl1, Double dbl2) {
        if (null != dbl1 && null != dbl2) {
            BigDecimal bd1 = BigDecimal.valueOf(dbl1);
            BigDecimal bd2 = BigDecimal.valueOf(dbl2);
            return bd1.compareTo(bd2);
        } else if (null != dbl1) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int compareTo(Integer int1, Integer int2) {
        if (null != int1 && null != int2) {
            return int1.compareTo(int2);
        } else if (null != int1) {
            return 1;
        } else {
            return 0;
        }
    }

    public static String convertToTwoDecimal(Double value) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        BigDecimal d = new BigDecimal(value);
        return df2.format(d);
    }

    public static String convertToTwoDecimalRoundDown(Double value) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        df2.setRoundingMode(RoundingMode.DOWN);
        BigDecimal d = new BigDecimal(value);
        return df2.format(d);
    }

    public static String convertToThreeDecimal(Double value) {
        DecimalFormat df2 = new DecimalFormat("########0.000");
        BigDecimal d = new BigDecimal(value);
        return df2.format(d);
    }
    
    public static String roundDecimalInteger(Double value) {
        String strValue = String.valueOf(value);
        Double decimalValue = Double.parseDouble(strValue.substring(strValue.indexOf("."), strValue.length()));
        int finalValue = value.intValue();
        if(decimalValue >= 0.50){
            finalValue++;
        }
        DecimalFormat df2 = new DecimalFormat("########0.00");
        BigDecimal d = new BigDecimal(finalValue);
        return df2.format(d);
    }
    
    public static String roundDecimalToInteger(Double value) {
        String strValue = String.valueOf(value);
        Double decimalValue = Double.parseDouble(strValue.substring(strValue.indexOf("."), strValue.length()));
        int finalValue = value.intValue();
        if(decimalValue >= 0.50){
            finalValue++;
        }
        DecimalFormat df2 = new DecimalFormat("########0");
        BigDecimal d = new BigDecimal(finalValue);
        return df2.format(d);
    }

    public static String convertToThreeDecimalhash(Double value) {
        DecimalFormat df2 = new DecimalFormat("########0.00#");
        BigDecimal d = new BigDecimal(value);
        return df2.format(d);
    }

    public static String truncateTwoDecimal(Double value) {
        DecimalFormat df2 = new DecimalFormat("########0");
        BigDecimal d = new BigDecimal(value);
        return df2.format(d);
    }
    public static Double convertToDouble(BigDecimal obj) {
		Double d = 0.0;
		if (obj != null) {
			d = obj.doubleValue();
		}
		return d;
	}
    
    public static String truncateTwoDecimalWithoutRoundUp(Double value) {
        DecimalFormat df = new DecimalFormat(".00");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(value);
    }
}
