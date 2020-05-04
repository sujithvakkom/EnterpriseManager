package sujithvakkomfreelance.com.components.models.homedelivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import sujithvakkomfreelance.com.components.localdata.logEnabled;

public class DeliverySchedule extends logEnabled implements Serializable {
    @SerializedName("Status")
    public Integer Status;
    @SerializedName("ScheduledDate")
    public Date ScheduledDate;
    @SerializedName("TimeSlot")
    public String TimeSlot;
    @SerializedName("DriverName")
    public String DriverName;
    @SerializedName("VehicleCode")
    public String VehicleCode;
    @SerializedName("GPS")
    public String GPS;
    @SerializedName("MapURL")
    public String MapURL;
    @SerializedName("Remarks")
    public String Remarks;
    @SerializedName("Emirate")
    public String Emirate;
    @SerializedName("UserName")
    public String UserName;

    protected void setStatus(int status) {
        this.Status = status;
    }

    public void setDriver(String user_name) {
        DriverName = user_name;
    }

    public Integer getStatus() {
        return this.Status;
    }

    public String getDriver() {
        return this.DriverName;
    }

    public String getRemark() {
        return this.Remarks;
    }

    public void setRemarks(String remark) {
        this.Remarks = remark != null ?
                remark.trim() :
                null;
    }
}