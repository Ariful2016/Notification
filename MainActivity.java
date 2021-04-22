package com.example.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    
    Button LocalNotification,d2d;
    final static String FCM_KEY ="key=AAAAovQb3KU:APA91bGnxxLHb6K1IFPdOKBrifj44eb-uP9DaX0Qhue5H5G-aXq9ElkP1tTH5TJ2dBTy8QNLKLc8czKSkOdOcu7l7_0-oY2WpR7fWAPhOkEmcMDiLiNYmEEJEdjOuoiGn40zBRW7QET6";
    final static String url = "https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue= Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("JobAlert").addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(MainActivity.this, "Subscribe to JA", Toast.LENGTH_SHORT).show();
            }
        });
        
        
        
        if(getIntent().hasExtra("requirement")){
            Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
            intent.putExtra("position",intent.getStringExtra("position"));
            intent.putExtra("requirement",intent.getStringExtra("requirement"));
            startActivity(intent);

        }

        LocalNotification=findViewById(R.id.LocalNoti);
        LocalNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLocalNotifications();
            }
        });

        d2d=findViewById(R.id.d2d);
        d2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D2D_Notification();
            }
        });
    }

    private void D2D_Notification() {
        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"JobAlert");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title","BD Jobs");
                jsonObject.put("body","Send your CV to BD jobs");

                json.put("notification",jsonObject);

                JSONObject jsonExtra = new JSONObject();
                jsonExtra.put("position","Software Developer");
                jsonExtra.put("requirement","Android Studio, Java, Firebase,etc");

                json.put("data",jsonExtra);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String,String> infoMap = new HashMap<>();
                    infoMap.put("content-type","application/json");
                    infoMap.put("authorization",FCM_KEY);

                    return infoMap;
                }
            };

            requestQueue.add(jsonObjectRequest);


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ShowLocalNotifications() {

        //Build Notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"ID");
        builder.setSmallIcon(R.drawable.ic_stat_name2);
        builder.setContentTitle("BD Service");
        builder.setContentText("50% off for 1 week");
        builder.setAutoCancel(true);

        //Build PendingIntent
        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //Build Notification Manager Based on API Version Level
        int id =(int)System.currentTimeMillis();
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ID", "demo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(id,builder.build());
        }else {
            notificationManager.notify(id,builder.build());
        }





    }
}