package sujithvakkomfreelance.com.components.web;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;
import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.models.LocationTracker;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;

public interface IWebClient {
    //i.e. api/HomeDelivery/GetAuthFor/
    @GET("/api/HomeDelivery/GetAuthFor")
    Call<UserDetail> GetAuthFor(@Query("UserName") String UserName, @Query("Password") String Password);

    @GET("/api/HomeDelivery/GetUserProfileFor")
    Call<UserDetail> GetUserProfileFor(@Query("UserName") String UserName, @Query("Session") String Session);

    @POST("/api/BiometricAPI/UpdateBioMetric")
    Call<String> UpdateBioMetric(@Query("UserName") String UserName, @Query("SignatureString") String SignatureString, @Query("Session") String Session);

    //i.e. api/HomeDelivery/GetDelivery/
    @GET("/api/HomeDelivery/GetDelivery")
    Call<List<delivery>> GetDelivery(@Query("receipt") String Receipt, @Query("status")int status, @Query("driver") String driver);

    //i.e. api/HomeDelivery/GetDelivery/
    @GET("/api/DeliveryJob/GetDeliveryJob")
    Call<DeliveryHeader> GetDeliveryJob(@Query("OrderNumber") String OrderNumber, @Query("VehicleCode") String VehicleCode, @Query("Status")String Status);

    //i.e. api/HomeDelivery/GetDelivery/
    @GET("/api/DeliveryJob/GetDeliveryJobs")
    Call< List<DeliveryHeader>> GetDeliveryJobs(@Query("VehicleCode") String VehicleCode, @Query("Status")String Status);

    //i.e. api/HomeDelivery/GetDelivery/
    @GET("/api/DeliveryJob/GetDeliveryJobsNotifications")
    Call< List<DeliveryHeader>> GetDeliveryJobsNotifications(@Query("VehicleCode") String VehicleCode, @Query("Status")String Status);

    //i.e. api/HomeDelivery/SetDeliveryJobs/
    @POST("/api/DeliveryJob/SetDeliveryJobs")
    Call<DeliveryHeader> SetDeliveryJobs(@Body DeliveryHeader DeliveryJob);

    //i.e. api/HomeDelivery/UpdateDelivery/
    @POST("/api/HomeDelivery/UpdateDelivery")
    Call<String> UpdateDelivery(@Query("receipt") String Receipt,@Query("status")int status,@Query("driver") String driver);

    //i.e. api/HomeDelivery/UpdateLog/
    @POST("/api/HomeDelivery/UpdateLog")
    Call<String> UpdateLog(@Query("status")int status,
                           @Query("driver") String driver,
                           @Query("remark") String remark,
                           @Query("headerId") int headerId,
                           @Query("detailsId") int detailsId);

    @POST("/api/Geo/PostUserLocation")
    Call<String> PostUserLocation(@Body ArrayList<LocationTracker> LocationTrackers);
}
