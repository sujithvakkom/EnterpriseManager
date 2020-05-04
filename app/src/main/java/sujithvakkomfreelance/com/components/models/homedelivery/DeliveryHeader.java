package sujithvakkomfreelance.com.components.models.homedelivery;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.localdata.IDataClass;
import sujithvakkomfreelance.com.components.localdata.logEnabled;

public class DeliveryHeader extends  DeliverySchedule
implements Serializable , Comparable<DeliveryHeader> {
    @SerializedName("HeaderId")
    public Integer HeaderId;
    @SerializedName("Receipt")
    public String Receipt;
    @SerializedName("OrderNumber")
    public String OrderNumber;
    @SerializedName("CustomerName")
    public String CustomerName;
    @SerializedName("SaleDate")
    public Date SaleDate;
    @SerializedName("Phone")
    public String Phone;
    @SerializedName("DeliveryAddress")
    public String DeliveryAddress;
    @SerializedName("DeliveryLines")
    public ArrayList<DeliveryLine> DeliveryLines;

    private transient byte[]  Attachment;
    @SerializedName("saleType")
    public String saleType;
    @SerializedName("deliveryType")
    public String deliveryType;
    @SerializedName("attachmentName")
    public String attachmentName;

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getCustomerLabel() {
        String customerLabel = "";
        if (CustomerName != null) {
            customerLabel += CustomerName;
            if (Phone != null)
                customerLabel += (System.getProperty("line.separator") + Phone);

        } else if (Phone != null)
            customerLabel += Phone;
        if (customerLabel.isEmpty()) customerLabel = "No customer";
        return customerLabel;
    }

    public String getSaleTypeLabel() {
        return getSaleType()+
                System.lineSeparator()+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.SaleDate);
        /*return Html.fromHtml(getSaleType()+System.lineSeparator()+"</br><sup>"+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.SaleDate)+"</sup>");*/
    }

    public String getDeliveryTypeLabel(String homeDeliverFlag) {
        String label =
                 getDeliveryType().equals(homeDeliverFlag) ?
                        getDeliveryType() +
                                System.lineSeparator() +
                                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.ScheduledDate) :
                        getDeliveryType();
        return label;
        /*
        return Html.fromHtml(getDeliveryType()+System.lineSeparator()+"</br><sup>"+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.ScheduledDate)+"</sup>");*/
    }

    public Bitmap getAttachment() {
        return BitmapFactory.decodeByteArray(this.Attachment , 0, this.Attachment.length);
    }

    public void setAttachment(Bitmap attachment) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        attachment.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Attachment = byteArray;
        attachment.recycle();
    }

    public String saveAttachment(Context context){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File mypath = new File(directory, imageFileName+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            this.getAttachment()
                    .compress(Bitmap.CompressFormat.JPEG,
                            75,
                            fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        this.attachmentName =  mypath.getAbsolutePath();
        return mypath.getAbsolutePath();
    }

    public MultipartBody.Part getRequestBodyPart() {
        try {
            String gsonSting = this.getGSON();

            MediaType MEDIATYPE_GSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(MEDIATYPE_GSON,
                    gsonSting
            );
            return MultipartBody.Part.create(body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public RequestBody getRequestBody() {
        try {
            String gsonSting = this.getGSON();

            MediaType MEDIATYPE_GSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(MEDIATYPE_GSON,
                    gsonSting
            );
            return  body;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getGSON() {
        Gson gson = new Gson();
        return gson.toJson(this,this.getClass());
    }

    public String getReceipt() {
        return Receipt;
    }

    public Date getReceiptDate() {
        return SaleDate;
    }

    public String  getEmirate() {
    return Emirate;
    }

    public String getCustomerName() {
    return CustomerName;
    }

    public String getCustomerAddress() {
        return this.DeliveryAddress;
    }

    public String getCustomerPhone() {
        return this.Phone;
    }

    public String getGPS_CORDINATES() {
        return GPS;
    }

    public String  getTimeSlot() {
    return TimeSlot;
    }

    /**
     * Set the status of header and lines
     * @param status
     */
    public void setStatus(int status) {
        super.setStatus(status);
        for(DeliveryLine line : DeliveryLines){
            line.setStatus(status);
        }
    }

    public String getDescription() {
        return this.OrderNumber+"- "+this.CustomerName;
    }

    public Date getScheduledDate() {
        return this.ScheduledDate;
    }

    public String[] getDeliveryList() {
        try {
            String[] result = new String[this.DeliveryLines.size()];
            for (int i = 0; i < this.DeliveryLines.size(); i++) {
                result[i] = this.DeliveryLines.get(i).Description;
            }
            return result;
        }catch (Exception ex){
            return  null;
        }
    }


    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    @Override
    public int compareTo(DeliveryHeader deliveryHeader) {
        if(this.ScheduledDate.compareTo(deliveryHeader.ScheduledDate)>0)
            return 1;
        return 0;
    }
}