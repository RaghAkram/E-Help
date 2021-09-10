package com.e_help.Notification;



import com.e_help.Notification.Notifications.MyResponse;
import com.e_help.Notification.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" + // Key اخدناه من حساب الفايربيس تبعنا
                            "AAAAek32-cM:APA91bFNvmx0BHhuQT8m4l16659RNRTL67M8DkU-bc-jWc14Lmnv_gJJjkapJYLMRglUiPOR9m4qIgqXy6VMUo-YiBSkS218XNH0U6gIq1VIwWFEgouWT_ch92SsHVvkTvcFpvffFBbi"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
