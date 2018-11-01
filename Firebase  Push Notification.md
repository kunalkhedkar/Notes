# Firebase : Push Notification

1. Very first go to firebase.com and login with your google account.

2. Then go to console click on new project 

   1. Enter name for your project

3. Add Firebase to your Android app

   1. Register your app  - fill information like app package name
   2. click on register app

4. Now we can download a config-json file (google-services.json)

5. Now go to your android studio change view to project from android and past above config file inside app folder

6. Project-level build.gradle (`<project>/build.gradle`):

   ```
   buildscript {
     dependencies {
       // Add this line
       classpath 'com.google.gms:google-services:4.0.1'
     }
   }
   ```

   App-level build.gradle (`<project>/<app-module>/build.gradle`):

   ```
   dependencies {
     // Add this line
     implementation 'com.google.firebase:firebase-core:16.0.1'
   }
   ...
   // Add to the bottom of the file
   apply plugin: 'com.google.gms.google-services'
   ```

   

   ##### Android Studio version 2.2 or later, the [Firebase Assistant](https://developer.android.com/studio/write/firebase.html?authuser=0) is the simplest way to connect your app   



That's  It what we have to do for setup



Now we need to create 2 services

```java
FirebaseMessagingService
FirebaseInstanceIdService
```

### 1. FirebaseInstanceIdService

Lets create a class that extends FirebaseInstanceIdService

```java
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "***";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

}
```



### 2. FirebaseMessagingService

```java
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MSG";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
    }

    @Override
    public void handleIntent(Intent intent) {
//        super.handleIntent(intent);

        //notification comes here

        Log.d(TAG, "handleIntent: "+intent.getExtras());
        String title=intent.getStringExtra("title");
        String msg=intent.getStringExtra("text");

//        MainActivity.info=intent.getExtras()+"";
        Log.d(TAG, "handleIntent: "+title+"\n"+msg);



        NotificationCompat.Builder b = new NotificationCompat.Builder(this.getApplicationContext());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.my_logo)
                .setTicker("{your tiny message}")
                .setContentTitle(title)
                .setContentText(msg)
                .setContentInfo("INFO")
                .setContentIntent(contentIntent);


        NotificationManager nm = (NotificationManager) 					                                           this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        			nm.notify(1, b.build());


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: ");
    }
}
```

### we can send notification by two way 

- individual token

  - we can get phone token inside FirebaseInstanceIdService service and we store it.

- topic

  - we need to subscribe for some topic

    ```java
    FirebaseMessaging.getInstance().subscribeToTopic(ID);
    ```

    

#### To send push-notification from android mobile use web-service call using volley

```java
    public void sendNotification(String title,String msg,String token) {

        final String Legacy_SERVER_KEY = "server_key";
        
        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", "token");
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);

    }
```



# Firebase database

```
USER_REFERENCE=FirebaseDatabase.getInstance().getReference("user");
```

this will gave database url link

#### Insert

```java
final String ID = STUDENT_REFERENCE.push().getKey();

Student s1 = new Student(ID, name, username, password, RollNumber, mobile, address, roleType, classType);

STUDENT_REFERENCE.child(ID).setValue(s1).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(this, "Register Sucessfully", Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().subscribeToTopic(ID);
        
    }

});
```

#### Read

```java
 DatabaseReference STUDENT_REFERENCE = FirebaseDatabase.getInstance().getReference("student");
        STUDENT_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userDataSnapshot:dataSnapshot.getChildren()){
                    Student ss=userDataSnapshot.getValue(Student.class);
                    if(ss.getS_ID().equals(id)) {
                        FirebaseMessaging.getInstance().subscribeToTopic("student");
                        FirebaseMessaging.getInstance().subscribeToTopic(ss.getS_Class());
                        FirebaseMessaging.getInstance().subscribeToTopic(id);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
```