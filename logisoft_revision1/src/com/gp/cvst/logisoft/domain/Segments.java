package com.gp.cvst.logisoft.domain;

/**
 * Segments generated by MyEclipse - Hibernate Tools
 */
public class Segments implements java.io.Serializable {

    // Fields    
    private Integer id;
    private String segmentType;
    private String segmentValue;

    // Constructors
    /**
     * default constructor
     */
    public Segments() {
    }

    /**
     * full constructor
     */
    public Segments(String segmentType, String segmentValue) {
        this.segmentType = segmentType;
        this.segmentValue = segmentValue;
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSegmentType() {
        return this.segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getSegmentValue() {
        return this.segmentValue;
    }

    public void setSegmentValue(String segmentValue) {
        this.segmentValue = segmentValue;
    }
}