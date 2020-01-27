package com.giraone.pms.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * The GenderType enumeration.
 */
public enum GenderType {

    UNKNOWN("u"), MALE("m"), FEMALE("f");

    private static Map<String, GenderType> stringToEnum = new HashMap<>();

    static {
        for (GenderType e : GenderType.values()) {
            GenderType.stringToEnum.put(e.toString(), e);
        }
    }

    // -------------------------------------------------------------------------------------------------

    private final String str;

    GenderType(String str) {
        this.str = str;
    }

    public static GenderType fromString(String strValue) {
        GenderType ret = GenderType.stringToEnum.get(strValue);
        return ret == null ? GenderType.UNKNOWN : ret;
    }

    @Override
    public String toString() {
        return str;
    }
}
