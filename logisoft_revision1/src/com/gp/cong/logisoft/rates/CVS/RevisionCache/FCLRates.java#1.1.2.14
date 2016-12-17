/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.logiware.ims.client.ImsModel;
import java.text.DecimalFormat;
import java.util.TreeSet;

/**
 *
 * @author Administrator
 * FCLRates is nothing but NVO rates.
 */
public class FCLRates extends TreeSet<SSLine> implements Rates {

    private String originId;
    private String originName;
    private String destinationId;
    private String destinationName;
    private SSLine previousSsLine;
    private String origin;
    private String destination;
    private ImsModel imsModel;
    private Double distance;
    private Double lat;
    private Double lng;
    private boolean included = false;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public FCLRates(String originId, String destinationId) {
        this.originId = originId;
        this.destinationId = destinationId;
    }

    public FCLRates(Object[] objects) {
        if (objects[ORIGIN_TERMINAL_ID] != null) {
            originId = objects[ORIGIN_TERMINAL_ID].toString();
        }
        if (objects[DESTINATION_PORT_ID] != null) {
            destinationId = objects[DESTINATION_PORT_ID].toString();
        }
        if (objects[ORIGIN_TERMINAL_NAME] != null) {
            originName = objects[ORIGIN_TERMINAL_NAME].toString();
        }
        if (objects[DESTINATION_PORT_NAME] != null) {
            destinationName = objects[DESTINATION_PORT_NAME].toString();
        }
        if (objects[ORIGIN] != null) {
            origin = objects[ORIGIN].toString();
        }
        if (objects[DESTINATION] != null) {
            destination = objects[DESTINATION].toString();
        }

        if (objects[LAT] != null && !CommonUtils.isEmpty(objects[LAT].toString())) {
            this.lat = Double.parseDouble(objects[LAT].toString());
        }

        if (objects[LNG] != null && !CommonUtils.isEmpty(objects[LNG].toString())) {
            this.lng = Double.parseDouble(objects[LNG].toString());
        }
        addSsLine(objects);
    }

    public boolean addSsLine(Object[] row) {
        SSLine currentSsline = new SSLine(row);
        if (previousSsLine != null && previousSsLine.equals(currentSsline)) {
            previousSsLine.addChargeCode(row);
        } else {
            add(currentSsline);
            previousSsLine = currentSsline;
        }
        return true;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public SSLine getPreviousSsLine() {
        return previousSsLine;
    }

    public void setPreviousSsLine(SSLine previousSsLine) {
        this.previousSsLine = previousSsLine;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    public ImsModel getImsModel() {
        return imsModel;
    }

    public void setImsModel(ImsModel imsModel) {
        this.imsModel = imsModel;
    }

    public Integer getLowestTransitTime() {
        Integer lowest = -1;
        if (this.size() > 1) {
            for (SSLine ssline : this) {
                if (null != ssline.getTransitTime() && !ssline.getTransitTime().trim().equals("") && !ssline.getTransitTime().trim().equals("0")) {
                    lowest = -1 == lowest ? Integer.parseInt(ssline.getTransitTime()) : (lowest > Integer.parseInt(ssline.getTransitTime()) ? Integer.parseInt(ssline.getTransitTime()) : lowest);
                }
            }
        }
        return lowest;
    }
    DecimalFormat df = new DecimalFormat("#.##");

    public Double distFromDoor(Zipcode doorOrigin) {
        if (null != this.lat && null != this.lng) {
            Double lat1 = Double.parseDouble(doorOrigin.getLat());
            Double lng1 = Double.parseDouble(doorOrigin.getLng());
            Double earthRadius = 3958.75;
            Double dLat = Math.toRadians(this.lat - lat1);
            Double dLng = Math.toRadians(this.lng - lng1);
            Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(this.lat))
                    * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            Double dist = earthRadius * c;
            return Double.parseDouble(df.format(dist));
        }
        return 0d;
    }

    public Double distFromDoorForNullLat(Zipcode doorOrigin, String unloccode) throws Exception {
        //  if(CommonUtils.isNotEmpty(doorOrigin)) {
        if (unloccode.lastIndexOf("(") > -1 && unloccode.lastIndexOf(")") > -1) {
            unloccode = unloccode.substring(unloccode.lastIndexOf("(") + 1, unloccode.lastIndexOf(")"));
        }
        UnLocation unLocation = new UnLocationDAO().getUnlocation(unloccode);
        if (null != unLocation && null != doorOrigin && CommonUtils.isNotEmpty(unLocation.getLat()) && CommonUtils.isNotEmpty(doorOrigin.getLat())) {
            return distFromDoorNullLat(doorOrigin, Double.parseDouble(unLocation.getLat()), Double.parseDouble(unLocation.getLng()));
        }
        return 0d;

    }

    public Double distFromDoorNullLat(Zipcode doorOrigin, Double lat, Double lng) throws Exception {
        DecimalFormat df = new DecimalFormat("#.##");
        if (null != lat && null != lng) {
            Double lat1 = Double.parseDouble(doorOrigin.getLat());
            Double lng1 = Double.parseDouble(doorOrigin.getLng());
            Double earthRadius = 3958.75;
            Double dLat = Math.toRadians(lat - lat1);
            Double dLng = Math.toRadians(lng - lng1);
            Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat))
                    * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            Double dist = earthRadius * c;
            return dist;
        }
        return 0d;
    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
