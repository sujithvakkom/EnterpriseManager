package sujithvakkomfreelance.com.components.models;


import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class LocationTracker implements Serializable {
    private Integer id;
    @SerializedName("VehicleCode")
    private String VehicleCode;
    @SerializedName("Latitude")
    private Double Latitude;
    @SerializedName("Longitude")
    private Double Longitude;
    @SerializedName("Accuracy")
    private Float accuracy;
    @SerializedName("CreatedTime")
    private Date CreatedTime;

    private Integer synched;

    public LocationTracker(
            @Nullable Integer id,
            String vehicleCode,
            Date createdTime,
            Double latitude,
            Double longitude,
            Float accuracy
    ) {
        if(id!=null)
        this.setId(id);
        this.setVehicleCode(vehicleCode) ;
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setAccuracy(accuracy);
        this.setCreatedTime(createdTime);
        this.setSynched(0);
    }

    public String getVehicleCode() {
        return VehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        VehicleCode = vehicleCode;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Date getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Date createdTime) {
        CreatedTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSynched(Integer synched) {
        this.synched = synched;
    }

    public Integer getSynched() {
        return synched;
    }
}
