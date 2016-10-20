package com.caoyang.tapon.model;


import com.caoyang.tapon.model.base.BaseM;

/**
 * Author：艹羊
 * Created Time:2016/09/30 11:19
 */

public class GeoLocationEntity extends BaseM {
    /**
     * latitude : 24.437162
     * longitude : 118.112948
     */
    private double latitude;
    private double longitude;

    public GeoLocationEntity(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
