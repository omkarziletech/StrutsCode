package com.gp.cong.logisoft.currency.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "results"
})
@XmlRootElement(name = "query")
public class CurrencyXml {

    @XmlElement(required = true)
    protected CurrencyXml.Results results;

    public CurrencyXml.Results getResults() {
        return results;
    }

    public void setResults(CurrencyXml.Results value) {
        this.results = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "rate"
    })
    public static class Results {

        protected List<CurrencyXml.Results.Rate> rate;

        public List<CurrencyXml.Results.Rate> getRate() {
            if (rate == null) {
                rate = new ArrayList<CurrencyXml.Results.Rate>();
            }
            return this.rate;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "rate",
            "date",
            "time",
            "ask",
            "bid"
        })
        public static class Rate {

            @XmlElement(name = "Name", required = true)
            protected String name;
            @XmlElement(name = "Rate")
            protected float rate;
            @XmlElement(name = "Date", required = true)
            protected String date;
            @XmlElement(name = "Time", required = true)
            protected String time;
            @XmlElement(name = "Ask")
            protected float ask;
            @XmlElement(name = "Bid")
            protected float bid;
            @XmlAttribute
            protected String id;

            public String getName() {
                return name;
            }

            public void setName(String value) {
                this.name = value;
            }

            public float getRate() {
                return rate;
            }

            public void setRate(float value) {
                this.rate = value;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String value) {
                this.date = value;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String value) {
                this.time = value;
            }

            public float getAsk() {
                return ask;
            }

            public void setAsk(float value) {
                this.ask = value;
            }

            public float getBid() {
                return bid;
            }

            public void setBid(float value) {
                this.bid = value;
            }

            public String getId() {
                return id;
            }

            public void setId(String value) {
                this.id = value;
            }
        }
    }
}
