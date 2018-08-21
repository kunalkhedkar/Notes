# Android Multithreading

> By Default your code runs on UI thread.

##### Message Queue

Android maintains a a queue which is called as a message queue. which contains the task that need to update the UI.

So this message queue is basically a queue of task that need to be executed one after another.

Now, question is who is going execute the task that are present in that message queue.

For that we have another thread which is under continues loop, which keeps on executing task present in message queue. Since this thread is under continues loop it is called as **looper**.

UI thread itself is under continues loop 

Now, if we are using thread and we try to update UI component from thread then application get crashes because android says 

```java
//exp: Only the original thread that created a view hierarchy can touch its view
```

Thread can not directly put the task in that message queue. For that we use something called as handler.

Thread give task that  want to put in message queue	to the handler, and handler will be having reference to message queue it will place that particular task in the message queue. As per android os task should be type of runnable instance. This runnable instance will be put in this particular queue.

```java
// get the handler object
Handler handler=new Handler(getApplicationContext().getMainLooper());

// inside thread
handler.post(new Runnable() {
	@Override
	public void run() {
		textview.setText("updated count");
	}
});
```

Another way of doing is we don't need to create handler object just use post method on view.

```java
textview.post(new Runnable() {
    @Override
    public void run() {
        textview.setText("updated count");     
    }
});
```



Android provide a very nice api to do this all stuff and that is AsyncTask.  

onPreExecute() 

doInBackground()

onPostExecute()

> to update UI in between 

we need call method publishProgress(value..) from doInBackground method.

And we can update ui in onProgressUpdate(value...) method.



Once the asyncTask has been stopped, we can not restart the same AsyncTask.

we can **cancel** the asyncTask by using method 

```java
myAsyncTask.cancel(true);
```

when asyncTask get cancel it will call onCancelled() method.

In doInbackground we can check the task is cancel or not to stop loop or any operation.

```java
if(isCancelled()){
	break; 
}

```



## Create a custom looper

![handler-looper](https://github.com/kunalkhedkar/Notes/blob/master/img/handler-looper.png)



We will create our own thread that will behave like looper and and this particular thread or a looper thread will be able to host the task that it wants to execute.

plan is, From a ui thread we want to trigger a new thread and this new thread basically use the handler and then put the task to the custom looper that that we have created.

we want run this our custom looper thread to run infinitely for that we use 

 

```java
Looper.prepare();

Looper.loop();
```

To give tasks to the looper we will create handler.

LooperThread.java

```java
public class LooperThread extends Thread {

    Handler handler;

    @Override
    public void run() {
        Looper.prepare();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(TAG,"Thread id when message is posted: "+ 											Thread.currentThread().getId()+", Count : "+msg.obj);
            }
        };
        Looper.loop();
    }
}
```

To make a looper class we need to add two method

- Looper.prepare();

- Looper.loop();

  After Looper.prepare() we will initialize handler object and pass runnable object. here we are simply log message.

MainActivity.java

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private boolean mStopLoop;
   
    Handler handler;
    
    LooperThread looperThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        looperThread=new LooperThread();
        looperThread.start();

        mStopLoop = true;
         executeOnCustomLooper();
         //executeOnCustoLooperWithCustomHandler();
    }
  }
```

```java
    public void executeOnCustomLooper(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mStopLoop){
                    try{
                      
                        Thread.sleep(1000);
                        count++;
                        Message message=new Message();
                        message.obj=""+count;
                        looperThread.handler.sendMessage(message);
                        
                    }catch (InterruptedException exception){
                        Log.i(TAG,"Thread for interrupted");
                    }

                }
            }
        }).start();

    }
```

`looperThread.handler.sendMessage(message);`  to send message to handler

```java
// Update UI from custom looper  
public void executeOnCustoLooperWithCustomHandler(){

      looperThread.handler.post(new Runnable() {
      @Override
      public void run() {
          while (mStopLoop){
             try{
                 	Thread.sleep(1000);
                    count++;
                 
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewthreadCount.setText(" "+count);
                            }
                        });
                    }catch (InterruptedException exception){
                        Log.i(TAG,"Thread for interrupted");
                    }

                }
            }
        });
    }
```

Call post method on handler directly  `looperThread.handler.post`

To stop Looper

```java
if(looperThread!=null && looperThread.isAlive()){
    looperThread.handler.getLooper().quit();
}
```





## HandlerThread

It is same as creating custom looper, just in compact way

```java
public class MyHandlerThread extends HandlerThread {

    public Handler mHandler;

    public CustomHandlerThread(String name){
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(TAG,"Thread id when message is posted: "+ 				                           Thread.currentThread().getId()+", Count : "+msg.obj);
            }
        };
    }

}
```

Instead of extending by normal thread and override run method, now we need to crate class that extends

HandlerThread. And we need to override `onLooperPrepared()` method and initialize handler object.

In MainActivity

```java
myHandlerThread = new MyHandlerThread("CustomHandlerThread");
myHandlerThread.start();

// simple 
public void executeOnCustomLooper(){
    new Thread(new Runnable() {
        @Override
        public void run() {

            Message message=new Message();
            message.obj=""+count;

            myHandlerThread.mHandler.sendMessage(message);
            
        }
    }
	}).start();

}

// update UI
public void executeOnCustoLooperWithCustomHandler(){

    myHandlerThread.mHandler.post(new Runnable() {
        @Override
        public void run() {
            while (mStopLoop){
                try{
                    Thread.sleep(1000);
                    count++;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textViewthreadCount.setText(" "+count);
                        }
                    });
                }catch (InterruptedException exception){
                    Log.i(TAG,"Thread for interrupted");
                }

            }
        }
    });
}
```

To stop Looper

```java
myHandlerThread.getLooper().quit();
```

