/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.rates;

import com.gp.cong.logisoft.rates.charge.ChargeCode;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author sunil
 */
public class LayoutListener {

    Set<String> carriers;
    Set<String> origins;
    Set<String> destinations;
    Set<String> charges;
    Set<String> nvos;
    private boolean forReport;

    public void addToCarrier(String carrier){
         if(carriers == null){
            carriers = new TreeSet<String>();
        }
        carriers.add(carrier);
    }
    public void addToOrigin(String origin){
         if(origins == null){
            origins = new HashSet();
        }
        origins.add(origin);
    }
    public void addToDestination(String destination){
         if(destinations == null){
            destinations = new TreeSet();
        }
        destinations.add(destination);
        addToList(destinations, destination);
    }
    public void addToCharge(String charge){
         if(charges == null){
            charges = new HashSet();
        }
//        if(! ChargeCode.OCEAN_FREIGHT.equalsIgnoreCase(charge)) {
//        }
        charges.add(charge);
    }
    public void addToNvo(String nvo){
         if(nvos == null){
            nvos = new HashSet();
        }
        nvos.add(nvo);
    }

    public Set<String> getCarriers() {
        return carriers;
    }

    public Set<String> getCharges() {
        return charges;
    }

    public Set<String> getDestinations() {
        return destinations;
    }

    public Set<String> getOrigins() {
        return origins;
    }

    public boolean isForReport() {
        return forReport;
    }

    public void setForReport(boolean forReport) {
        this.forReport = forReport;
    }

    private void addToList(Set set , Object value){
        if(set == null){
            set = new HashSet();
        }
        set.add(value);
    }
}
