package com.meteo.meteo.Utils;

import org.json.JSONObject;

public class JsonHelper {
    public String getString(JSONObject object, String key) {
        if (!object.has(key)) {
            return "";
        }
        return object.getString(key);
    }

    public Double getDouble(JSONObject object, String key) {
        if (!object.has(key)) {
            return 0.0;
        }
        return object.getDouble(key);
    }

    public Integer getInteger(JSONObject object, String key) {
        if (!object.has(key)) {
            return 0;
        }
        return object.getInt(key);
    }
}
