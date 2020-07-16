package com.avit.kbcpremium;

import com.avit.kbcpremium.auth.LoginPostData;
import com.avit.kbcpremium.auth.LoginResponseData;
import com.avit.kbcpremium.auth.RegisterPostData;
import com.avit.kbcpremium.notification.NotificationPostData;
import com.avit.kbcpremium.notification.NotificationResponseData;
import com.avit.kbcpremium.ui.booking.BookingItems;
import com.avit.kbcpremium.ui.bookseat.BookingNotificationPostData;
import com.avit.kbcpremium.ui.bookseat.BookingNotificationResponseData;
import com.avit.kbcpremium.ui.bookseat.BookingSeatDetails;
import com.avit.kbcpremium.ui.cart.AvailabilityResponse;
import com.avit.kbcpremium.ui.cart.CartItem;
import com.avit.kbcpremium.ui.products.BrandItem;
import com.avit.kbcpremium.ui.products.ProductItem;
import com.avit.kbcpremium.ui.products.SubProducts;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkApi {

//    String BASE_URL = "http://192.168.225.20:5000/";
    String BASE_URL = "http://18.188.149.40:5000/";

    @GET("/brandProducts/{id}")
    Call<ArrayList<SubProducts>> getBrandItems(@Path(value = "id") String brandName,@Query("offset") int offset,@Query("start") int start);

    @GET("/brandProducts/{id}")
    Call<ArrayList<ProductItem>> getBrandWithOnlyItems(@Path(value = "id") String brandName, @Query("offset") int offset, @Query("start") int start);

    @POST("/user/signIn")
    Call<LoginResponseData> signIn(@Body LoginPostData postData);

    @POST("/user/signUp")
    Call<ResponseBody> signUp(@Body RegisterPostData postData);

    @GET("/availability/orderAvailability")
    Call<AvailabilityResponse> checkAvailability();

    @POST("/payment/checkout")
    Call<NotificationResponseData> sendNotificationToServer(@Body NotificationPostData notificationPostData);

    @GET("bookingItem/{id}")
    Call<BookingItems> getBookingItemsOf(@Path(value = "id") String bookingCategory);

    @POST("/bookseat")
    Call<BookingNotificationResponseData> sendBookingNotifcation(@Body BookingNotificationPostData bookingNotificationPostData);

    @GET("/bookseat/{id}")
    Call<BookingSeatDetails> getBookingSeatDetails(@Path(value = "id") String date);

}
