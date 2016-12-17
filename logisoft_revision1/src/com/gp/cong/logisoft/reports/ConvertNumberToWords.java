package com.gp.cong.logisoft.reports;

import java.text.DecimalFormat;

public class ConvertNumberToWords {

  private static final String[] tensNames = {
    "",
    " TEN",
    " TWENTY",
    " THIRTY",
    " FORTY",
    " FIFTY",
    " SIXTY",
    " SEVENTY",
    " EIGHTY",
    " NINETY"
  };

  private static final String[] numNames = {
    "",
    " ONE",
    " TWO",
    " THREE",
    " FOUR",
    " FIVE",
    " SIX",
    " SEVEN",
    " EIGHT",
    " NINE",
    " TEN",
    " ELEVEN",
    " TWELVE",
    " THIRTEEN",
    " FOURTEEN",
    " FIFTEEN",
    " SIXTEEN",
    " SEVENTEEN",
    " EIGHTEEN",
    " NINETEEN"
  };

  private static String convertLessThanOneThousand(int number) {
    String soFar;

    if (number % 100 < 20){
      soFar = numNames[number % 100];
      number /= 100;
    }
    else {
      soFar = numNames[number % 10];
      number /= 10;

      soFar = tensNames[number % 10] + soFar;
      number /= 10;
    }
    if (number == 0) return soFar;
    return numNames[number] + " HUNDRED" + soFar;
  }


  public static String convert(long number) {
    // 0 to 999 999 999 999
    if (number == 0) { return "ZERO"; }
    String snumber = Long.toString(number);
    // pad with "0"
    String mask = "000000000000";
    DecimalFormat df = new DecimalFormat(mask);
    snumber = df.format(number);

    // XXXnnnnnnnnn 
    int billions = Integer.parseInt(snumber.substring(0,3));
    // nnnXXXnnnnnn
    int millions  = Integer.parseInt(snumber.substring(3,6)); 
    // nnnnnnXXXnnn
    int hundredThousands = Integer.parseInt(snumber.substring(6,9)); 
    // nnnnnnnnnXXX
    int thousands = Integer.parseInt(snumber.substring(9,12));    

    String tradBillions;
    switch (billions) {
    case 0:
      tradBillions = "";
      break;
    case 1 :
      tradBillions = convertLessThanOneThousand(billions) 
      + " BILLION ";
      break;
    default :
      tradBillions = convertLessThanOneThousand(billions) 
      + " BILLION ";
    }
    String result =  tradBillions;

    String tradMillions;
    switch (millions) {
    case 0:
      tradMillions = "";
      break;
    case 1 :
      tradMillions = convertLessThanOneThousand(millions) 
      + " MILLION ";
      break;
    default :
      tradMillions = convertLessThanOneThousand(millions) 
      + " MILLION ";
    }
    result =  result + tradMillions;

    String tradHundredThousands;
    switch (hundredThousands) {
    case 0:
      tradHundredThousands = "";
      break;
    case 1 :
      tradHundredThousands = "ONE THOUSAND ";
      break;
    default :
      tradHundredThousands = convertLessThanOneThousand(hundredThousands) 
      + " THOUSAND ";
    }
    result =  result + tradHundredThousands;

    String tradThousand;
    tradThousand = convertLessThanOneThousand(thousands);
    result =  result + tradThousand;

    // remove extra spaces!
    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
  }

}


