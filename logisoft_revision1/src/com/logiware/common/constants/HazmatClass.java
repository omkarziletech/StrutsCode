package com.logiware.common.constants;

/**
 *
 * @author Rajesh
 */
public enum HazmatClass {

    FLG("2.1"), NFG("2.2"), FLL("3"), FLS("4.1"), SPC("4.2"), DWW("4.3"), OXI("5.1"), ORG("5.2"), POI("6.1"), COR("8"), CL9("9");

    private final String classification;

    HazmatClass(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return this.classification;
    }

    public static HazmatClass fromString(String classification) {
        if (classification != null) {
            for (HazmatClass hazmatClass : HazmatClass.values()) {
                if (classification.equalsIgnoreCase(hazmatClass.classification)) {
                    return hazmatClass;
                }
            }
        }
        return null;
    }
}
