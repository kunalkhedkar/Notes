

# Retrofit

# Read Data using retrofit

- ##### Add dependency in build.gradle

```groovy
// Retrofit networking
implementation 'com.squareup.retrofit2:retrofit:2.4.0'
// Gson for parsing
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
```



- ##### Create Interface for client

GithubClient.java

```java
public interface GitHubClient {
    @GET("/users/{user}/repos")
    Call<List<GitHubRepoModel>> reposForUser(@Path("user") String user);
}
```

here user should be dynamic so we pass username as parameter to **reposForUser** method in {} braces.

and annotated parameter variable as @Path and gave default value "user". 

- ##### Create Model class - to parse response object

```java
public class GitHubRepoModel {
    private String name;
    public String getName() {
        return name;
    }
}
```

This is simple pojo class which contains exacts same name which is going to present in response.



- ##### Create Retrofit-Builder

```java
Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create());
```

#2 method : Is base URL 

#3 method : We can specify parsing technic, we just need to pass its object to 

​		       addConverterFactory method.

​`here we use Gson for parsing. (need to add dependency of retrofit-gson)`

- ##### create Retrofit object from retrofit-builder

```java
Retrofit retrofit = builder.build();
```

- ##### Instantiate  client interface object

```java
GitHubClient client = retrofit.create(GitHubClient.class);
```

- ##### Call web service using client method

```java
Call<List<GitHubRepo>> call = client.reposForUser("username");
```

- ##### Call service asynchrony

```java
call.enqueue(new Callback<List<GitHubRepo>>() {
	@Override
	public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
     	List<GitHubRepo> repos = response.body();
      }
    @Override
     public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
     }
});
```

here we call enqueue method on call object which will get from calling web-service method.

enqueue need callback object which has 2 compulsory method to override 

1. onResponse  -  get response by calling response.body() method
2. onFailure       -  it will call if service fails or no connection found





# Write data to server

Same as Reading, no change in creating retrofit object and call service.



- ##### Create model class which we want to send to server

```java
class User {
    
    String name;
    String email;
    int age;
    String[] topics;
    Integer id;

    public User(String name, String email, int age, String[] topics) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.topics = topics;
    }

    public Integer getId() {
        return id;
    }
}
```

**Note** : If Object property is null retrofit will just ignore. So in our user class ID is null and we are sending it to server since it is null retrofit  will not create json property in request. it will just ignore.

- ##### Create interface for user-client

  ```java
  public interface UserClient {
          @POST("user")
          Call<User> createAccount(@Body User user);
  
  }
  ```

here want to send user object as request body so we need to annotate user object as a @Body()

we can use `body` declaration to make sure java object get pass as body in request payload.



# Log Request and response



- ##### Add dependency

```groovy
implementation 'com.squareup.okhttp3:logging-interceptor:3.6.0'
```

- ##### Add custom okhttp client to retrofit with log-interceptor

Retrofit **use default instance of okhttp** as a network layer unless u pass a custom. Now we will create custom one.

```java

OkHttpClient.Builder OkHttpClientBuilder=new OkHttpClient.Builder();
HttpLoggingInterceptor logger=new HttpLoggingInterceptor();
logger.setLevel(HttpLoggingInterceptor.Level.BODY);
OkHttpClientBuilder.addInterceptor(logger);

```

here we want to add log-interceptor, so we create object of HttpLoggingInterceptor and add it to OkHttpClientBuilder.

we can set **log level** by using method **setLevel** values can be 

- Body 	-	Logs request and response lines and their respective headers and bodies (if present).

	 Basic	-	Logs request and response lines.

	 Header	-	Logs request and response lines and their respective headers.

	 None 	-	No logs.

  

- ##### Add custom OkHttpClient to retrofit-builder

> Retrofit.Builder retrofitBuilder=new Retrofit.Builder()
>         						.baseUrl("http://192.168.5.51/ProductService/Service1.svc/")
>       							.addConverterFactory(GsonConverterFactory.create())
> 						        **.client(OkHttpClientBuilder.build());**



Note: Make sure you do not log any request-response in production level for that

```java
if(BuildConfig.DEBUG){
    OkHttpClientBuilder.addInterceptor(logger);
}
```



# Upload file to server

- ##### Create client interface


```java
public interface FileClient {
    
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadphoto(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part photofile
    );

}
```

here as usual we need to specify web-service end point by annotating @POST

For file we need add one more annotation which is @Multipart. Multipart need request type to POST or PUT

Multipart requests sperate request into different parts so we need to annotate every parameter with the   `@part` annotation.

- Parameters with there types

#1 **Description** - it is just a simple string . we can replace its type with string but its has some side effects

so we keep it `ResponseBody`.

#2 **Actual file** - need to add type as `MultipartBody.Part`



- Call web-service method

  As we mention in method parameter we need to create RequestBody object.

  

  In client method we have two parameter , lets create them

- > description 

  we need create requestBody object, we use static method from RequestBody class.

  ```java
  RequestBody descriptionPart=RequestBody.create(MultipartBody.FORM,"fileName");
  ```

  **first parameter** is MediaType , this need to be **MultipartBody.FORM** when sending addition data along with file 

  Second parameter is description String its self.

  

- > photoFile

     Its a two step process

  - **First we create requestBody for the file**

```java
RequestBody photoFilePart=RequestBody.create(
        						MediaType.parse(getContentResolver().getType(fileuri)),
        						FileUtils.getFile(this,fileuri) );
```

As above it take two parameter `MediaType` and `file`

- MediaType

  If we know file type we can directly set it in first parameter, 

  But for Dynamic purpose we take mediaType by parsing it using `MediaType.parse` and inside it we pass `fileUri` to contentResolver.getType.

- File

  ​	just File object. we need do this stuff because of file chooser of android.
  - **Second step**

okHttp needs to knows about part and file name we can describe those by creating new  MultipartBody.Part object using createForm object 

```java
MultipartBody.Partfile Partfile = MultipartBody.Part.createFormData("photo",
													photofile.getName(),
													photoFilePart);
```

#1 parameter is part name which we have specified in part annotation for the description 

#2 File name

#3 photoFilePart object



- **call web-service method**

  ```java
  Call<ResponseBody> call = fileClient.uploadphoto(descriptionPart, Partfile);
  call.enqueue(new Callback<ResponseBody>() {...}
  ```

- **Final call be** 

  ```java
  RequestBody descriptionPart=ResponseBody.create(MultipartBody.FORM,"fileName");
  
  RequestBody photoFilePart=RequestBody.create(
          						MediaType.parse(getContentResolver().getType(fileuri)),
          						FileUtils.getFile(this,fileuri) );
  
  MultipartBody.Partfile=MultipartBody.Part.createFormData("photo",
                                                           photofile.getName(),
                                                           photoFilePart);
  
  FileClient fileClient = retrofit.create(FileClient.class);
  
  Call<ResponseBody> call = fileClient.uploadphoto(descriptionPart, file);
  call.enqueue(new Callback<ResponseBody>() { ... });
  ```







# **Passing Multiple Parts Along a File with**

What if we want to send more part along with file, extra describing Strings

Lets create util method to cerate StringPart and filePart  

```java
public  static RequestBody createPartFromString(String string){
    return  RequestBody.create(MultipartBody.FORM,string);
}
```

```java
public static MultipartBody.Part createFilePart(String partName, Uri fileUri){

    File file= FileUtils.getFile(context,fileUri);

    //Create RequestBody instance from file
    RequestBody requestFilePart=RequestBody.create(
            MediaType.parse(context.getContentResolver().getType(fileUri)),file);

    return MultipartBody.Part.createFormData(partName,file.getName(),requestFilePart);
}
```



web-service call will be

```java
Call<ResponseBody> call1 = fileClient.uploadphoto(
                                MultiPartUtil.createPartFromString("description"),
                                MultiPartUtil.createPartFromString("location"),
                                MultiPartUtil.createPartFromString("year"),
                                MultiPartUtil.createPartFromString("photographer"),
                                MultiPartUtil.createFilePart("photo",fileuri));

call.enqueue(new Callback<ResponseBody>() { ... });
```



> ### Multiple part Using Map
>

we can send as much part as we need but as number of parts grows its become difficult to maintain so we can replace multiple same type part with map.

```
@Multipart
@POST("upload")
Call<ResponseBody> uploadphoto(
        @PartMap Map<String,RequestBody> dataPart,
        @Part MultipartBody.Part photofile
);
```

we need so annotate it with @PartMap and map type.

And web-service call will be

```java
Map<String, RequestBody> partMap = new HashMap<>();
```

```java
partMap.put("description", MultiPartUtil.createPartFromString("description"));
partMap.put("location", MultiPartUtil.createPartFromString("location"));
partMap.put("year", MultiPartUtil.createPartFromString("year"));
partMap.put("photographer", MultiPartUtil.createPartFromString("photographer"));

Call<ResponseBody> call2 = fileClient.uploadphoto(partMap,
        MultiPartUtil.createFilePart("photo", fileuri));
```





# Upload Multiple Files

simple send list of MultipartBody.Part

```java
@Multipart
@POST("upload")
Call<ResponseBody> uploadphoto(
        @Part("description") RequestBody description,
        @Part List<MultipartBody.Part> files
);
```

web-service call will be

```java
List<MultipartBody.Part> partList=new ArrayList<>();

for (int i = 0; i < fileuris.size(); i++) {
    partList.add(MultiPartUtil.createFilePart(""+i,fileuris.get(i)));
}

Call<ResponseBody> call3 = fileClient.uploadphoto(
    								MultiPartUtil.createPartFromString("description"),
        							partList);
```



# Custom Request Headers

Often its necessary fill app meta info to request in request header. 

> **Static Headers** 

```java
@Headers("Cache-Control: max-age=3600")
@POST("user")
Call<User> createAccount(@Body User user);
```

 We just need to add annotation @Headers and give value. Value will string which include key and value.

We can also send multiple header using {}

```java
@Headers({"Cache-Control: max-age=3600",
          "User-Agent: Android",
          "User-Type: test"
        })
@POST("user")
Call<User> createAccount(@Body User user);
```

> **Dynamic Header**

Often we don't have values for header in advance. we can use thus there second way to set header

we can set dynamic header inside method body

```java
@POST("user")
Call<User> createAccount(
        @Header("Header-user-key") String userName,
        @Body User user);
```

method call will be

```java
userClient.createAccount("header-Username-Value",user);
```


> **What if we put Both Header**

```java
@Headers("Cache-Control: max-age=3600")
@POST("user")
Call<User> createAccount(
        @Header("Cache-Control") String userName,
        @Body User user);
```

If we put both header static as well as dynamic 

Dynamic header will not replace static. Both headers will be added.

```javascript
Cache-Control="Cache-Control:   max-age=3600 , Dynamic-header-Username-Value "
```



# Synchronous and Asynchronous Requests

> ##### Asynchronous -   enqueue 
>

Thread will **not block** until network operation complete. Thread will free to perform next operation.

```java
        Call<ResponseBody> call = fileClient.uploadphoto(descriptionPart, file);
        
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
```



> ##### synchronous  -   execute
>

Thread will **block** until network operation complete. When net operation complete then only thread move forward to perform next operation.

```java
try {
    
    	Response<ResponseBody> result = call.execute();
  
} catch (IOException e) {
    e.printStackTrace();
} 
```

In higher version of android app will directly **crash** if you try to perform network operation on main thread.

we can use Intent-service or background thread. To perform **synchronous**  request.



# Request Headers in OkHttp Interceptor

Retrofit support Static as well as Dynamic header. But if have large app and too many end points, you will need to add this header to every single endpoint declaration. (web-service call) This is neither efficient or developer friendly. Now here OkHttp Interceptor comes into picture because you can manage herder in one central place for all request. So we will go down a level, so instead of solving the problem of adding header at retrofit level, we go into utilized network level of okhttp. OkHttp offers the interceptor, which is an easy way to customized every request of the app in single place before it actually goes out to the network.

Retrofit use default instance of okHttp. So we need to create customized okhttp object and add interceptor.

```java
OkHttpClient.Builder okHttpBuilder=new OkHttpClient.Builder(); 
```

we need add interceptor

```java
okHttpBuilder.addInterceptor(new Interceptor() {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Request.Builder newRequestBuilder=request.newBuilder().addHeader("Auth","auth-token");

        return chain.proceed(newRequestBuilder.build());
    }
});
```

here we got chain object using which we can get request object. we can not change request so we need to create new-request by using request object got from chain.

we use **newBuilder()** method on old-request object to create new-request-builder object and here we can add herder by using builder method addHeader(). And simple return request object created from new-request-builder object.

Note : we also can use 

```java
// will append header if already exist
request.newBuilder().addHeader("Auth","auth-token");
					or
// replace header if already exist					
request.newBuilder().Header("Auth","auth-token");
```



# Use Dynamic Urls for Requests

In Retrofit structure we gave base url, But what if we need to use completely different domain or url. For this retrofit allow to use dynamic urls.

```java
@GET
Call<ResponseBody> getUserProfilePhoto( 
    									@Url String url);
```

we simply annotate parameter as @Url and now we can simply pass the entire URL and Retrofit will access that exactly URL.   

```java
String myURL="https://s3.amazon.com/profile-picture/path";
userClient.getUserProfilePhoto(myURL);
```



# Download Files from Server

Retrofit is library for for doing rest request. It does support download file but it's not main feature.

```java
public interface FileDownloadClient {
    @GET("images/futurestudio-university-logo.png")
   Call<ResponseBody> downloadFile();

}
```

No change in calling end point. we can remove addConverterFactory as we are not converting it as java object.

we need write binary data as file to disk in response.

```java
Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("https://futurestud.io/");

Retrofit retrofit = builder.build();

FileDownloadClient fileDownloadClient = retrofit.create(FileDownloadClient.class);
Call<ResponseBody> responseBodyCall = fileDownloadClient.downloadFile();
responseBodyCall.enqueue(new Callback<ResponseBody>() {
@Override
public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
	inputStream = response.body().byteStream();
}
...
});
```

> ##### But there is one problem

Retrofit, by default , keeps the response in memory. So basically the server response with some data, Retrofit will keep connection open wait until everything is there and pass it over to Gson for conversion.

When server response binary data file can pretty large.If you download a 50mb file, Retrofit try to keep those 50mb in memory.If file gets pretty large device will start get crash because they don't have that much memory. 

​	Retrofit solution here is to stream the response to app. Whenever the server response with the first bytes it will hand over the result right away. It won't wait until the entire response is there. The app then has to deal with the incoming stream of bytes and write it to the disk. we need listen to that open stream of bytes and write it down.  

we need add new annotation @Streaming to downloadClient interface. 

```java
@Streaming
@GET()
Call<ResponseBody> downloadFile(@Url String url);
```



After doing this changes app will crash with error `networkonmainthreadexception`

Retrofit goes onto a background thread to execute the request and when the request comes back it call the UI thread in the onResponse() method. So what happen is we have network stream of data coming in and we are trying to write it to disk on UI thread. this way we get `networkonmainthreadexception` . So solution is write response body on background thread. 



# Simple Error Handling

OnResponse callback means that server returns something but it does't mean its a successful response.

**it also can be an error response.**

So to differentiate between successful response and error response retrofit provide method **isSuccessful()**

if response is not successful we can access errorBody from **response.errorBody().**

```java
call.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            Log.d("TAG", "Successful Response");
        }else{
            Log.d("TAG", "error Response"+response.errorBody());
            
        }
    }
...
});
```

We can get http response code by using method **code**

```
response.code();
```



# Send Data Form-Urlencoded

what is form-urlencoded ?

Depending on the type and amount of data being transmitted, one of the methods will be more efficient than the other.  ( `multipart/form-data`  | `application/x-www-form-urlencoded` )

- **application/x-www-form-urlencoded**

  For `application/x-www-form-urlencoded`, the body of the HTTP message sent to the server is essentially one giant query string -- name/value pairs are separated by the ampersand (`&`), and names are separated from values by the equals symbol (`=`). An example of this would be: 

```ruby
MyVariableOne=ValueOne&MyVariableTwo=ValueTwo
```

> [Reserved and] non-alphanumeric characters are replaced by `%HH', a percent sign and two hexadecimal digits representing the ASCII code of the character 

That means that for each non-alphanumeric byte that exists in one of our values, it's going to take three bytes to represent it. For large binary files, tripling the payload is going to be highly inefficient.

That's where `multipart/form-data` comes in.

- **multipart/form-data**

With this method of transmitting name/value pairs, each pair is represented as a "part" in a MIME message. Parts are separated by a particular string boundary (chosen specifically so that this boundary string does not occur in any of the "value" payloads). Each part has its own set of MIME headers like `Content-Type`, and particularly `Content-Disposition`, which can give each part its "name." The value piece of each name/value pair is the payload of each part of the MIME message.  



**Let's create FormUrlEncoded request**

```java
@FormUrlEncoded
@POST
Call<ResponseBody> sendUserToServer(

        @Field("name") String name,
        @Field("email") String email,
        @Field("age") String age,
        @Field("topic") List<String> topic,

);
```

here we need add new annotation `@FormUrlEncoded` to specify its FormUrlEncoded request. And inside parameter body we annotate it with `@Field` and we need to specify name for field.

we simply can access endpoint by

```java
Call<ResponseBody> userClientCall = userClient.sendUserToServer( name, email, age, topicsList);
```

Here topics are in one String separated by ","  but we should use array of string. let's do changes

If there is large number of parameters its will hard to maintain, so for that we can use map.

To use map we need to annotate parameter as @FieldMap.

```java
@FormUrlEncoded
@POST
Call<ResponseBody> sendUserToServer(
        @FieldMap Map<String,Object> map
);
```

But Even our map have object type for value, retrofit just make make a string on of it and not apply any logic. Enter topics as a array inside String.      `"[Android, php]"`

![Untitled](C:\Users\Admin\Desktop\Untitled.png)

so we need to send topics separately as list with map.

```java
@FormUrlEncoded
@POST
Call<ResponseBody> sendUserToServer(
        @FieldMap Map<String,Object> map,
        @Field("topics") List<String> topics
); 
```



# Send Plain Text Request Body 

we can use `body` declaration to make sure java object get pass as body in request payload.

**plainTextClient** 

```
@POST("message/")
Call<ResponseBody> sendMessage(@Body String message);
```

But if we use `ConverterFactory` as a `GsonConverterFactory` request type MIME type will be

application/json.

Because as we use `GsonConverterFactory`  every request we are sending is going to be converted by Gson and by default Gson send json object, so its also going to change type of that request `application/json`

This is not exactly what we want.

So solution here is we can use  scalars-converter.

Let's  add dependency in build.gradle file

```groovy
implementation 'com.squareup.retrofit2:converter-scalars:2.4.0'
```

Now, our plainTextClient will remain same we just need to change in Retrofit building step

```java
Retrofit.Builder builder=new Retrofit.Builder()
                        .baseUrl(myURL)
                        .addConverterFactory(ScalarsConverterFactory.create());
```

Using scalar converter we can send String or any other primitive type.

Another way to do this is create request body directly.

```
@POST("message/")
Call<ResponseBody> sendMessage(@Body RequestBody message);
```

we need specify request parameter type as RequestBody.

End-point call will be 

```java
RequestBody stringRequestBody=RequestBody.create(
        MediaType.parse("text/plain"),
        message
);

plainTextClientCall.sendMessage(stringRequestBody);
```

​	

# Add Query Parameters to Request

here is example url which take query parameter 

```PHP
http://example.com/page?parameter=value&also=another
```

```java
public interface QueryDemoClient {

    @GET("searchUser")
    Call<ResponseBody> SearchForUser(

            @Query("apikey") String apikey,
            @Query("id") String id,
            @Query("sort") String sort,
            @Query("pages") String pages
    );
}
```

we also can use map to handle it easily

```java
@GET("searchUser")
Call<ResponseBody> SearchForUser(
        @Query("apikey") String apikey,
        @Query("id") String id,
        @QueryMap Map<String, Object> queries
);	
```

If we need to add a query parameter we can use interceptor instead of adding manually in all request.

It will automatically add query parameter to every request.

```java
OkHttpClient.Builder OkHttpbuilder=new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                //orignal req and url
                Request orignalRequest=chain.request();
                HttpUrl orignalHttpUrl=orignalRequest.url();

                // new req and url
                HttpUrl newHttpUrl = 																  orignalHttpUrl.newBuilder().addQueryParameter("apikey",API_KEY).build();
                Request.Builder newRequestBuilder= orignalRequest.newBuilder().url(newHttpUrl);

                // build req
                Request request=newRequestBuilder.build();

                // return new to proceed next operation
                return chain.proceed(request);
                
            }
        });
```





# Authentication

## Basic Authentication

**Basic authentication** is a simple **authentication** scheme built into the HTTP protocol. The client sends HTTP requests with the Authorization header that contains the word **Basic** word followed by a space and a base64-encoded string username:password . 

```ruby
Basic (base64-encoded string username : password)
Basic a3VuYWw6MTIz
```

so Client interface will be

```java
public interface AuthClient {

    @GET("request-Url-With-basic-Authentication")
    Call<ResponseBody> getUser(

                @Header("Authorization") String authHeader
    );
}
```

and end-point calling will be 

1. put **:** between username and password and concat it
2. concat **Basic** word and **base64 encoded** usernamePassword  

```java
String userNamePassword=username+":"+password;
String basicAuthHeader= "Basic "+ Base64.encode(userNamePassword.getBytes(),Base64.NO_WRAP);

AuthClient authClient = retrofit.create(AuthClient.class);
Call<ResponseBody> getUserCall = authClient.getUser(basicAuthHeader);
getUserCall.enqueue(new Callback<ResponseBody>() {...});
```



## Token Authentication

API tokens are a replacement to sending some username/password combination over HTTP which is not secure. Tokens is a bit of a general term. Usually token is a unique identifier of an application requesting access to your service. Your service would generate an token for the application to use when requesting your service. 

Let's make it in two step 

1. first we will send credentials to server then server send Token back
2. Using that token we access secured data 

**So user-validation Client will be**

```java
@POST("validate-login-send-token")
Call<User> login(
        @Body LoginCredential loginCredential
);
```

**Access-secured-data Client will be**

```java
@GET("access-secure-data")
Call<ResponseBody> accessSecuredData(	
        @Header("Authorization") String authToken
);
```

we get token from response 

```java
	LoginClient loginClient = retrofit.create(LoginClient.class);
    LoginCredential credential=new LoginCredential("username","password")
        
    Call<User> loginCall = loginClient.login(credential);
    loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String myToken=response.body().getToken();
            }
			...
        });

```

Access secured data call will be

```java
Call<ResponseBody> responseCall = loginClient.accessSecuredData(myToken);
```



##  OAuth Authentication with GitHub

OAuth is a token based authorization method which uses an access token for interaction between user and API. OAuth requires several steps and requests against the API to get your access token.

1. Register an app for the API you want to develop. Use the developer sites of the public API you're going to develop for.
2. Save client id and client secret in your app.
3. Request access to user data from your app.
4. Use the authorization code to get the access token.
5. Use the access token to interact with the API.





1. We need to start activity and provide OAuth service provider login url

   ```java
   String OAuthProviderURL="https://github.com/login/oauth/authorize";
   Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(OAuthProviderURL));
   startActivity(intent);
   ```

2. On Response Github OAuth send code so we need to catch that response for that we need add 

   special intent filter in manifests file.

   ```xml
   <intent-filter>
       <action android:name="android.intent.action.VIEW" />
       <category android:name="android.intent.category.DEFAULT" />
       <category android:name="android.intent.category.BROWSABLE" />
       <!--Response - From redirect URL-->
       <data
           android:host="callback"
           android:scheme="futurestudio" />   
   </intent-filter>
   ```



3. ​	we need to get code sent by OAuth as we provide intent filter we can get it by getIntent method

   ```java
    Uri uri = getIntent().getData();
   
   if (uri != null && uri.toString().startsWith(redirectUri)) {
      // use the parameter your API exposes for the code (mostly it's "code")
      // something like your://redirecturi?code=1234  
      String code = uri.getQueryParameter("code");
      if (code != null) {
               
           } else if (uri.getQueryParameter("error") != null) {
               // show an error message here
           }
       }
   ```



4. ​	Let's create AccessTokenClient

   ```java
   public interface AccessTokenClient {
       @FormUrlEncoded
       @POST("/token")
       Call<AccessToken> getAccessToken(
               @Field("code") String code,
           	@Field("cilent_Secret") String cilentSecret,
               @Field("client_id") String clientId);
   }
   ```











​							//**TODO**







# Optional & Multiple Query Parameters

#### Optional query parameters

so if you need to send request and not every query parameter needs to be set and user does't give any input you can leave those out.

```java
@GET("search-user")
Call<ResponseBody> searchForUser(

        @Query("id") int ID,
        @Query("sort")  String order,
        @Query("page") Integer page     // need to be Interger object so tht we can set it null
);
```

```java
Call<ResponseBody> userSearchCall = userClient.searchForUser(
                                007,
                                null,
                                null

);
```

If query parameter is optional just put it null, then retrofit will ignore them. In this case retrofit set only only one parameter that is ID.



#### Multiple Query parameters

if you need to send multiple query parameters of the same name 

Eg. if you request a search function and you want to search for multiple IDs you can pass every ID as a sub-query parameter but they all have the same name.

```java
@GET("search-user")
Call<ResponseBody> searchForUser(

        @Query("id") int ID1,
        @Query("id") int ID2,
        @Query("sort")  String order,
        @Query("page") Integer page
);
```

here we just need to set query parameter name is same. in our case its `id` . 

If we have dynamic same name parameter we can use list instead of duplicating rows

```java
@GET("search-user")
Call<ResponseBody> searchForUser(
        @Query("id") List<Integer> IDs,
        @Query("sort")  String order,
        @Query("page") Integer page
); 
```

In final request it turns into array of Ids

![Untitled123](C:\Users\Admin\Desktop\Untitled123.png)





## Dynamic Query Parameters

we can send complete dynamic query parameters using **Map**.

```java
@GET("search-user")
Call<ResponseBody> searchForUser(
        @QueryMap Map<String,Object> map
);
```

But if we want any compulsory parameter in query request, map does't  guaranty that it will be included.

So its better to add that parameter separate so that user has to fill it, at least null value.

![3](C:\Users\Admin\Desktop\3.png)



# Cancel Requests

Some time we need to cancel request. Retrofit make it very easy you just need to call cancel method on call object. Immediately it will call onFailure and onFailure method code gets call.

```java
call.cancel();
```

It might say that request fail to user, but it was cancel by user, so deal with retrofit gives one flag method

**isCanceled**() which returns boolean.   

```java
loginCall.enqueue(new Callback<User>() {
    @Override
    public void onResponse(Call<User> call, Response<User> response) {
		// Response
    }
    @Override
    public void onFailure(Call<User> call, Throwable t) {
        if (call.isCanceled()) {
            Toast.makeText(MainActivity.this, "Request canceled by user", Toast.LENGTH_SHORT).
                show();
        }
    }
});
```

# Customizing Gson Converter

we can create customize gson object and pass to retrofit builder like setting date format etc.

```java
Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormat.LONG).create();
```





# Creating a Sustainable Android Client

```java
public class ServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();

    public static <S> S createService(
        Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {		// already exist
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
```

Retrofit generic singleton class 
```java
import android.content.Context;

import com.eagleeyesystems.kot.BuildConfig;
import com.eagleeyesystems.kot.util.SharedPrefUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {


    /** Initialize IP at application class by calling updateServerIP method */
    private static String IP = "";
    private static String BASE_URL = "";

    private static Retrofit retrofit = null;
    //    private static final String BASE_URL = "http://"+IP+"/WebService/Service1.svc/";

    public static void updateServerIP(Context context, String ip) {
        if (ip != null) {
            IP = ip;
            BASE_URL = "http://" + IP + "/WebService/WebService.svc/";
            SharedPrefUtil.save(context, SharedPrefUtil.SERVER_IP_KEY, ip);
            // to assign new base_url with new ip, creating new retrofit object
            retrofit=null;
            buildRetrofitObject();
        }
    }

    private RetrofitHelper() {

    }

    public static <S> S createService(Class<S> serviceClass) {

        if (retrofit == null) {
            retrofit = buildRetrofitObject();
        }

        return retrofit.create(serviceClass);
    }


    private static synchronized Retrofit buildRetrofitObject() {

        if (retrofit == null) {
            OkHttpClient.Builder okHttpClintBuilder = new OkHttpClient.Builder();

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // log only in debug apk
            if (BuildConfig.DEBUG) {
                okHttpClintBuilder.addInterceptor(httpLoggingInterceptor);
            }

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClintBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create());

            return builder.build();

        } else
            return retrofit;
    }
}

```


