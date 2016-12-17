/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.rates;

/**
 *
 * @author Administrator
 */
public interface Rates {

  int FCL_STD_ID = 0;
  int START_DATE = 1;
  int END_DATE = 2;
  int ORIGIN_TERMINAL_ID = 3;
  int DESTINATION_PORT_ID = 4;
  int ORIGIN_TERMINAL_NAME = 34;
  int DESTINATION_PORT_NAME = 35;
  // for SsLine object
  int SS_LINE_NUMBER = 5;
  int COMMODITY_CODE = 6;
  int CONTRACT = 7;
  int ORIGIN_REGION = 8;
  int DESTINATION_REGION = 9;
  
  int FCL_COST_ID = 10;

  // for ChargeCode object
  int COST_CODE = 11;
  int COST_TYPE = 12;
  int FCL_COST_TYPE_ID = 13;

  // for UnitCost object
  int UNIT_CODE = 27;
  int ACTIVE_AMOUNT = 15;
  int CTC_AMOUNT = 16;
  int FTF_AMOUNT = 17;
  int MINIMUM_AMOUNT = 18;
  int RETAIL_AMOUNT = 19;
  int STANDARD = 20;
  int MARKUP = 21;
  int CURRENCY_CODE = 22;
  int COST_CODE_DESC = 23;
  int POL_CODE = 24;
  int COST_TYPE_DESC = 25;
  int SS_LINE_NAME = 26;
  int UNIT_ID=14;
  int UNIT_CODE_DESC = 28;
  int TRANSIT_TIME = 29;
  int REMARKS = 30;
  int HAZARDOUS_FLAG = 31;
  int POE = 32;
  int ORIGIN = 36;
  int DESTINATION = 37;
  int LOCAL_DRAYAGE = 33;
  int LAT = 38;
  int LNG = 39;
  int CHARGE_CODE = 40;
}
