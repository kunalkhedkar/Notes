# Dependency Injection

> Dependency Injection is a design pattern in which required objects or dependencies are created 
> by an external entity, and supply them to the dependent object.

#### Why we should use Dependency Injection

1. ##### Maintainability and Expandability

   - Easy to add new feature 
   - Easy to make changes in existing content
   - Easy to fix bugs
   - Easy to read and understand code
   - Easy to ream work
   - Easy to merge code

2. ##### Testing

   - if there are so many dependencies within a method or class it is very hard to create mock objet 				         and provide them for testing.
   - we should avoid creating new object within a class using new keyword.
   - Automated testing is almost impossible without a proper architectural design.
   - Some of the famous Android testing framework are 
     - Espresso
     - Robotium
     - Appium

3. ##### Reusability

   - we should not write same code in more than one place.
   - If a class or method does not depend on many things then its reusable nature increase.

   

# What is Dependency

 

```java
class Car{

    private Engine engineInstance;

    public Car(){
        engineInstance=new Engine();

        start();

    }

    public void start(){
        engineInstance.start();
    }
}
```



Here, there is a class called car, it has an instance variable called engine instance of type engine, in the constructor the car engine construct a new engine object using the new keyword and assign that to engine instance variable and later it calls to start method  we can see car object uses that instance variable to call
the start method of the newly created engine object So in this example you can see the car class depends on the engine class . Wherever cars are engines needs to be there We cannot reuse cars without reusing engines. For this scenario engine class is a In this case cars depend on engines.But engines may not depend on cars.



```java
public class Engine {

    private Piston piston;

    public Engine() {
        piston = new Piston();
    }

    void start(){
        // Do something
    }

}
```

Now let's have a look at the engine class. Looks like this engine class have a dependency called Piston.Engine object cannot survive without the support of piston object, for this case Engine class become the dependent and the Piston class become the dependency. Now, what can you say about car object? looks like we just found an indirect dependency called Piston for the car object.



> #### dependencies can be categorized in many ways

- Class dependencies
- Interface dependencies
- method or field dependencies
- direct and Indirect dependencies



# What is dependency injection?

- we do not allow object to create there dependencies by themselves.
- But we create those dependencies somewhere else and provide them to the object when needed.
- we construct dependencies outside of the object and inject them to the object.

This design pattern is called Dependency Injection.



> ##### Support libraries

-  Guice
-  Dagger 2
-  Butter Knife
-  Android Annotation



# Dagger 2  (Google Dagger) 

- All analysis done at the compile time. 
- Scoping with annotations, 
- No more reflections, 
- field and constructor injection introduced
- No more run time graph composition
- Modules requires configuration than Dagger 1



> ##### Annotation process

Annotation process generate the code during the compile time.

Dagger 2 does the code generation at the compile time using annotation process.



> #### Main Dagger 2 Annotations    

- @Module
- @provide 
- @Inject
- @Component

| Annotation | Usage                                                        |
| ---------- | ------------------------------------------------------------ |
| @Module    | Used on **classes** which contains methods annotated with `@Provides` |
| @Provides  | Can be used on methods in classes annotated with `@Module` and is used for methods which provides objects for dependencies injection. |
| @Inject    | This annotation is for requesting dependencies.                                                                      You can use @Inject annotation inside Activities, Fragments And service providers.To request dependencies. |
| @Component | This **interface** is used to build the interface which acts as a bridge between dependent and dependencies. |





> #### @Inject

@Inject annotation tells the Dagger about the dependencies needed to be transferred to the dependent.

**In Dagger2 There are `three` type of injections**

1. Constructor injection 
2. Field Injection
3. Method Injection



> **Singleton annotation**

This annotation tells dagger to create only one instance of the object (dependency).

When using dagger2 every time you are injecting @singleton annotation class it will be the same instance as long as you  inject it from the same component. If you use a different component, it will not behave like singleton.



```java
@Module
public class MemberDataModule {

    @Provides
    @Singleton
    MemberDataManager provideMemberDataManager(){
        
        return new MemberDataManager();
    }
}
```





**Let's start with code,** **Simple Application**

 first step is to setup dagger2 in our application, Add dependencies in build.gradle file

```groovy
implementation 'com.google.dagger:dagger:2.11'
annotationProcessor 'com.google.dagger:dagger-compiler:2.11'
```

**Application Architecture**

![7.3 Simple APP Architecture.jpg](D:\v\Dagger 2 For Beginners - Dependency Injection, MVP and more\[Tutsgalaxy.com] - Dagger 2 For Beginners - Dependency Injection, MVP and more\4. A  very simple Dagger 2 example\7.3 Simple APP Architecture.jpg.jpg)

**Project files**

App: User can enter member ID, id is going check in data and return member has access or not.

1. MainActivity

   Contains all UI part, EditText and button, get member id and trigger member-check function.

2. Member

   Modular Class which memberId, memberName, memberEmail

3. MemberDataManager

   It contains  members data (database) list and method for validate member id to grant access.

   

4. MemberDataModule

   let's create this class to provide MemberDataMannager dependency

   ```java
   @Module
   public class MemberDataModule {
   
       @Singleton
       @Provides
       MemberDataManager provide_memberDataManager(){
           
           return new MemberDataManager();
       }
   
   }
   ```

    here we created MemberDataModule class to provide dependency of MemberDataManager , and annotate class with @Module as it contain dependency provider method.

   for that,we create method that returns MemberDataManager object and we need to annotate that method with @Provides as it going to provide dependency.we also annotate it with singleton as we need single object across the project.

   

5. `MemberAppComponent`

   All the modules which are going to use through this interface, we need to define them.

   ```java
   @Singleton
   @Component(modules = MemberDataModule.class)
   public interface MemberAppComponent {
   
       void inject(MainActivity mainActivity);
   }
   ```

   we need create interface and annotate it with @Component and inside we have list down all modules which are going use through this interface.

   After that, we define abstract method and pass parameter of that class in which we are going to use it.

   so, here we are using it in mainActivity i.e we define mainActivity parameter.

6. `Application class`

   we need do some settings in application class, application class runs at very first when app is launching

   so at very first we setup some dagger settings.

   ```java
   public class App extends Application {
   
       private static App app;
       private MemberAppComponent memberAppComponent;
   
       @Override
       public void onCreate() {
           super.onCreate();
           app=App.this;
   
           memberAppComponent=DaggerMemberAppComponent.builder()
                               .memberDataModule(new MemberDataModule())
                               .build();
       }
       
       public static App getApp() {
           return app;
       }
       
       public MemberAppComponent getMemberAppComponent() {
           return memberAppComponent;
       }
   }
   ```

   first we create application class static instance and provide getter method to access that.

   then define MemberAppComponent globally.

   now, override onCreate method initialize application instance.After that we initialize memberAppComponent object. for that,

   we use builder which dagger is going to provide, generally dagger generate it with suffix Dagger

   so in our case `Dagger + MemberAppComponent`  . using this builder method we build our all module related to that component in our case MemberDataModule and finally build method.

7.  Use Dependency 

   To use dependency we just need declare dependency and add annotation @Inject

   ```java
   public class MainActivity extends AppCompatActivity {
   
       @Inject
       MemberDataManager memberDataManager;
   
       private EditText memberId;
       private Button submitButton;
       @Override
       protected void onCreate(Bundle savedInstanceState) {
       	...
       }
   ```

   7.1	Link dependency inside on create 

   ```java
   	App.getApp().getMemberAppComponent().inject(this);
   ```

   7.2 Call dependency

   ```java
   public class MainActivity extends AppCompatActivity {
   
       // Inject dependency
       @Inject
       MemberDataManager memberDataManager;
   
       private EditText memberId;
       private Button submitButton;
   
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
   
           memberId = (EditText) findViewById(R.id.etMemberId);
           submitButton = (Button) findViewById(R.id.btnSubmit);
   
           // Link dependency
           App.getApp().getMemberAppComponent().inject(this);
   
           submitButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
   
   
                   if ((memberId.getText().toString().equals(""))) {
                       Toast.makeText(this, "Member ID is empty",Toast.LENGTH_SHORT)
                           .show();
                   } else {
   
                       String input = memberId.getText().toString();
   				  //	String result=new MemberDataManager().checkMemberStatus(input);
                      //  	  Instead of creating MemberDataManager object here we use
                      // 	  dependency injection
                       String result = memberDataManager.checkMemberStatus(input);
                       Toast.makeText(this, result, Toast.LENGTH_SHORT)
                           .show();
                   }
   
   
               }
           });
       }
   }
   ```



Done with simple application.



# Multiple Dependency

> Instantiating dependency which requires another dependency.

here we are going to store access count in sharePreference for that we need context object.  So here we have multiple dependency we need memberDataManger dependency, memberDataManger need sharePreference dependency and sharePreference need context dependency.



```c++
App  => memberDataManager ->  sharePreference -> Context
```

So, let's make changes in MemberDataManager

1. Define 3 objects in memberDataManger

   ```java
   private SharedPreferences sharedPreferences;
   private static final String COUNT_KEY="countKey";
   private int currentCount;
   ```

2. Add sharePreference parameter in constructer and assign it to class member object.

   ```java
   public MemberDataManager(SharedPreferences sharedPreferences) {
   
       sharedPreferences=this.sharedPreferences;
       populateData();
   
   }
   ```

3. To manager count let's create `updateCount` method and `getCount` method.

```java
private void updateAccessCount() {
    sharedPreferences.edit()
            .putInt(COUNT_KEY,getCurrentCount()+1)
            .commit();
}

public int getCurrentCount() {
    currentCount=sharedPreferences.getInt(COUNT_KEY,0);
    return currentCount;
}
```

4. Use these method when access granted to show current access count and store it inside checkMemberStatus method in memberDataManger.

   ```java
   if ((m.getMemberId().equals(userInput))) {
   
       updateAccessCount();
       memberStatus = "Access Granted : count "+getCurrentCount();
   
   }
   ```

   Done with changes in memberDataManger.



**MemberDataModule**

In previous app we have `provideMemberDataManager` method, but here we have sharePreference dependency need for `memberDataManager `  So, need to provide sharePreference to `provideMemberDataManager`  by constructor. 

```java
@Provides
@Singleton
MemberDataManager provideMemberDataManager(SharedPreferences sharedPreferences){

    return new MemberDataManager(sharedPreferences);
}   
```

**wait** but from where that (SharedPreferences sharedPreferences) parameter object is coming from.

for that we need add one more provider which provide  sharedPreferences dependency.

```java
@Singleton @Provides
public SharedPreferences provideSharedPreferences(Context context){
    return PreferenceManager.getDefaultSharedPreferences(context);
}		
```

**wait** for `provideSharedPreferences` we again need context dependency. lets provide context dependency.

```java
@Singleton
@Provides
public Context provideContext(){
    return context;
}
```

here we cant not create context objet so we need to get it from somewhere lets add context as a class member and add constructor to `memberDataModule`  with context parameter .

```java
@Module
public class MemberDataModule {

    private Context context;

    public MemberDataModule(Context context) {
        this.context = context;
    }
    ...
 } 
```

Done with memberDataModule.

As memberDataModule accepts context object as parameter in constructor we need to do small change in our **application class**. we need to provide context object by using `getApplicationContext`

```java
memberAppComponent = DaggerMemberAppComponent.builder()
        .memberDataModule(new MemberDataModule(getApplicationContext()))
        .build();
```

now we can use dependency in  MainActivity  

```java
public class MainActivity extends AppCompatActivity {
    @Inject
    MemberDataManager memberDataManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getApp().getMemberAppComponent().inject(this);
         String input = etMemberId.getText().toString();
  		String result = memberDataManager.checkMemberStatus(input);

    }
}
```

### Code snippets

`MemberDataManager.java`

```
public class MemberDataManager {

    private static final String COUNT_KEY = "count";
    private String memberStatus;
    private ArrayList<Member> members = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int currentCount;

    public MemberDataManager(SharedPreferences pref) {
        sharedPreferences = pref;
        populateData();
    }

    public String checkMemberStatus(String userInput) {

        memberStatus = "Access Denied";
        for (Member m : members) {
			 if ((m.getMemberId().equals(userInput))) {  
               	 updateAccessCount();
               	 memberStatus = " Access Granted: " + " Access Count is  " + getCurrentCount();
            }
        }
        return memberStatus;
    }

    private void updateAccessCount() {
        sharedPreferences.edit()
                .putInt(COUNT_KEY, getCurrentCount() + 1)
                .apply();
    }

    private int getCurrentCount() {
        currentCount = sharedPreferences.getInt(COUNT_KEY, 0);
        return currentCount;

    }

    private void populateData() {

        members.add(new Member("123", "Tom", "tom@gmail.com"));
        ...
    }


}
```

`MemberDataModule.java`

```java
public class MemberDataModule {

    private Context context;

    public MemberDataModule(Context context) {
        this.context = context;
    }


    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    MemberDataManager provideMemberDataManager(SharedPreferences sharedPreferences){

        return new MemberDataManager(sharedPreferences);
    }
    
}
```

`Application.java`

```java
public class App extends Application{
    private static App app;
    private MemberAppComponent memberAppComponent;
    
    public static App getApp(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app=this;
        memberAppComponent = DaggerMemberAppComponent.builder()
                .memberDataModule(new MemberDataModule(getApplicationContext()))
                .build();
    }
    
    public MemberAppComponent getMemberAppComponent(){
        return this.memberAppComponent;
    }

}
```

No change in `MemberAppComponent` interface

```
@Singleton
@Component(modules =MemberDataModule.class)
public interface MemberAppComponent {
    void inject(MainActivity mainActivity);
}
```

`MainActivity.java`

```java
public class MainActivity extends AppCompatActivity {
    @Inject
    MemberDataManager memberDataManager;
    @BindView(R.id.etMemberId)
    EditText etMemberId;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvResult)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
		// Inject
        App.getApp().getMemberAppComponent().inject(this);
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
        if ((etMemberId.getText().toString().equals(""))) {
            Toast.makeText(context, "Member ID is empty", Toast.LENGTH_SHORT).show();
        } else {

            String input = etMemberId.getText().toString();
            
            // String result=new MemberDataManager().checkMemberStatus(input);
            // can be replace by
            String result = memberDataManager.checkMemberStatus(input);
            tvResult.setText(result);
        }

    }
}
```


# Custom Scope

#### Singleton scope 

This  is default annotation provided by dagger for scopes. Dagger will construct dependency object only one and it exist during the entire lifecycle of the application.

#### Custom scope annotations

It allow us to create our own scopes.

Some times we don't need all dependencies to exist during the entire lifecycle of the application.

we should also provide Retention policy using @Retention annotation  

we also need to create a subcomponent or dependent component with the same scope 	when we are using custom annotation.

```java
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLevelScope
{
	// code
}
```

# Dependent Component

Dagger2 has provided the facility to leverage scope allowing us to create either dependent components or subcomponents.

we can create dependent component that are tied to the lifecycle of an activity or fragment.



Dependent component require the parent component to explicitly list out what dependencies can be injected downstream.

Two dependent components cannot share the same scope.



Lets start with the coding

**Model** : now we create one simple class MessageGenerator which is going provide a message.

```java
public class MessageGenerator {
    private String welcomeMessage;

    public String getWelcomeMessage() {
        welcomeMessage="Welcome Back";
        return welcomeMessage;
    }
} 
```

**Module :** let's create module class which is going to provide MessageGenerator dependency.

```java
@Module
public class MessagesModule {
    
    @Singleton
    @Provides
    public MessageGenerator provideMessageGenrater(){
        return new MessageGenerator();
    }
}
```



**Component :**  create a component WelcomeActivityComponent which dependent component of MemberAppComponent.

```java
@ActivityScope
@Component(dependencies = MemberAppComponent.class, modules = MessagesModule.class)
public interface WelcomeActivityComponent {

    void inject(WelcomeActivity welcomeActivity);

}
```

New thing  here is as `WelcomeActivityComponent` is dependent component we need to add main component as a dependency in @Component section.



In this simple we don't  have any actual dependency from memberAppComponent. If we have such requirement we need add Inject line memberAppComponent.

Example:

```java
@Singleton
@Component(modules ={MemberDataModule.class,DateAndTimeModule.class, MessagesModule.class})
public interface MemberAppComponent {

    void inject(MainActivity mainActivity);
    void inject(MessageGenerator messageGenerator);
    
}
```

Small changes in **application class**

Declare component

```java
private WelcomeActivityComponent welcomeActivityComponent;
```

Create getter method for component

```java
public WelcomeActivityComponent getWelcomeActivityComponent() {
    return welcomeActivityComponent;
}
```

Inside onCreate 

```java
welcomeActivityComponent = DaggerWelcomeActivityComponent.builder()
        .memberAppComponent(memberAppComponent)
        .messagesModule(new MessagesModule())
        .build();
```

full Application will look like

```java
public class App extends Application{


    private static App app;
    private MemberAppComponent memberAppComponent;
    private WelcomeActivityComponent welcomeActivityComponent;


    public static App getApp(){
        return app;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();

        app=this;
        memberAppComponent = DaggerMemberAppComponent.builder()
                .memberDataModule(new MemberDataModule(getApplicationContext()))
                .build();

        welcomeActivityComponent = DaggerWelcomeActivityComponent.builder()
                .memberAppComponent(memberAppComponent)
                .messagesModule(new MessagesModule())
                .build();
        
    }


    public MemberAppComponent getMemberAppComponent(){
        return this.memberAppComponent;
    }

        public WelcomeActivityComponent getWelcomeActivityComponent() {
            return welcomeActivityComponent;
        }
}
```

And Last **welcome activity** will use dependency

```java
public class WelcomeActivity extends AppCompatActivity {
	// View declare
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        App.getApp().getMemberAppComponent().inject(this);

        String message=messageGenerator.getWelcomeMessage();
        tvMessage.setText(message);
    }
}
```
# Sub Components

##### When you should use dependent components?

- When you want to keep two component much independent from each other.
- You need to explicitly show what dependencies from one component is used by the other.
- Dependent component have access to only types which are declared in parent component.

##### when you should use sub-component?

- you may not care to explicitly show what dependencies from one component is used by the other.
- Sub component have access to all objects declared in its parent  component's module.



 Let's do it in practically

1. First we will create new scope for welcome activity

   `WelcomeActivityScope` interface will look like

   ```java
   @Scope
   @Retention(RetentionPolicy.RUNTIME)
   public @interface WelcomeActivityScope  {
   
   }
   ```

2. let's create new sub-component

   ```java
   @WelcomeActivityScope
   @Subcomponent(modules = MessagesModule.class)
   public interface WelcomeActivityComponent {
   
       void inject(WelcomeActivity welcomeActivity);
   
       MessageGenerator myMessageGenerator();
   }		
   ```

   we added WelcomeActivityScope scope to our subComponent. To make it subComponent we need add annotation @SubComponent and list out module inside it. After that we have our normal inject method  

   >  MessageGenerator myMessageGenerator();

   This is a little bit special, this is the dependency.

   When we are using subComponent we have to declare dependencies we are going to use within the SubComponent.

   

   3. Let't make some changes in application class

   ```java
   memberAppComponent = DaggerMemberAppComponent.builder()
           .memberDataModule(new MemberDataModule(getApplicationContext()))
           .dateAndTimeModule(new DateAndTimeModule())
           //.messagesModule(new MessagesModule())  remove this 
           .build();
   ```

   we don't need to add messageModule here because we want messageModule at activity start time and destroy at activity destroy and it should available at welcomeActivity only so we can do it using WelcomeActivityScope scope.

   4. Make WelcomeActivityComponent as a subComponent of memberAppComponent

      ```java
      @Singleton
      @Component(modules ={MemberDataModule.class,DateAndTimeModule.class})
      public interface MemberAppComponent {
      
          void inject(MainActivity mainActivity);
      //    void inject(MessageGenerator messageGenerator);
      
          // adding as sub-component
          WelcomeActivityComponent newWelcomeActivityComponent(MessagesModule messagesModule);
      
      }
      ```

      here we remove past inject method for dependency as we are moving messageModule to WelcomeActivityComponent.

      we add WelcomeActivityComponent dependency as sub-component in memberAppComponent. 

   

   5. WelcomeActivity we will refer to dependency like below

      ```java
      public class WelcomeActivity extends AppCompatActivity {
          @Inject
          MessageGenerator messageGenerator;
          @BindView(R.id.tvMessage)
        
          @Override
          protected void onCreate(Bundle savedInstanceState) {
      
              App.getApp().getMemberAppComponent()
                      .newWelcomeActivityComponent(new MessagesModule())
                      .inject(this);
      
              tvMessage.setText(message);
          }
      }
      ```



At End project files are

# project structure tree

![DaggerAppTree](E:\notes\DaggerAppTree.PNG)

#### 1. Model 

Member.java

```java
public class Member {

    private String memberId;
    private String memberName;
    private String memberEmail;


    public Member() {
    }

    public Member(String memberId, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;

    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }


}
```

MemberDataManager.java

```java
public class MemberDataManager {

    private static final String COUNT_KEY = "count";
    private String memberStatus;
    private ArrayList<Member> members = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int currentCount;

    public MemberDataManager(SharedPreferences pref) {
        sharedPreferences = pref;
        populateData();
    }

    public MemberDataManager(SharedPreferences pref, NetworkManager networkManager) {
        sharedPreferences = pref;
        populateData();
    }


    public String checkMemberStatus(String userInput) {
        memberStatus = "Access Denied";
        for (Member m : members) {
            
            if ((m.getMemberId().equals(userInput))) {
                updateAccessCount();
                memberStatus = " Access Granted: " + " Access Count is  " + getCurrentCount();
            }
        }
        return memberStatus;
    }

    private void updateAccessCount() {
        sharedPreferences.edit()
                .putInt(COUNT_KEY, getCurrentCount() + 1)
                .apply();
    }

    private int getCurrentCount() {
        currentCount = sharedPreferences.getInt(COUNT_KEY, 0);
        return currentCount;

    }


    private void populateData() {

        members.add(new Member("123", "Tom", "tom@gmail.com"));
        members.add(new Member("127", "Sam", "sam@gmail.com"));
        members.add(new Member("670", "Jack", "jack@gmail.com"));
        members.add(new Member("230", "Frank", "frank@gmail.com"));
        members.add(new Member("118", "Mary", "mary@gmail.com"));
        members.add(new Member("602", "Sara", "sara@gmail.com"));


    }

}
```

MessageGenerator.java

```java
public class MessageGenerator {


    private String welcomeMessage;

    public String getWelcomeMessage() {

        welcomeMessage="Welcome Back";

        return welcomeMessage;
    }
}
```

#### 2 . modules

DateAndTimeModule.java

```java
@Module
public class DateAndTimeModule {

    @Singleton
    @Provides
    public Calendar provideCalendar(){
        return Calendar.getInstance();
    }

    @Singleton
    @Named("dd-MMM-yyyy")
    @Provides
    public String provideDateType1(Calendar c){
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());

    }

    @Singleton
    @Named("dd-MMM-yy")
    @Provides
    public String provideDateType2(Calendar c){
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());

    }

    @Singleton
    @Named("yyyy-MM-dd HH:mm:ss")
    @Provides
    public String provideDateType3(Calendar c){
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());
    }
}
```

MemberDataModule.java

```java
@Module
public class MemberDataModule {

    private Context context;

    public MemberDataModule(Context context) {
        this.context = context;
    }


    @Singleton @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Singleton @Provides
    public NetworkManager provideNetworkManager(){

        return new NetworkManager();
    }

    @Provides
    @Named("local")
    @Singleton
    MemberDataManager provideMemberDataManagerLocal(SharedPreferences sharedPreferences){

        return new MemberDataManager(sharedPreferences);
    }

    @Provides
    @Named("online")
    @Singleton
    MemberDataManager provideMemberDataManagerOnline(SharedPreferences sharedPreferences,NetworkManager networkManager){

        return new MemberDataManager(sharedPreferences,networkManager)

    }
    
}
```

MessagesModule.java

```java
@Module
public class MessagesModule {

    @Provides
    @WelcomeActivityScope
    public MessageGenerator provideMessageGenerator() {

        return new MessageGenerator();
    }

}
```

#### 3. scopes

WelcomeActivityScope.java

```java
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface WelcomeActivityScope {
}
```

#### 4 . components

MemberAppComponent.java

```java
@Singleton
@Component(modules = {MemberDataModule.class, DateAndTimeModule.class})
public interface MemberAppComponent {

    void inject(MainActivity mainActivity);
    
    WelcomeActivityComponent newWelcomeActivityComponent(MessagesModule messagesModule);
    
}
```

WelcomeActivityComponent.java

```java
@WelcomeActivityScope
@Subcomponent(modules = MessagesModule.class)
public interface WelcomeActivityComponent {

    void inject(WelcomeActivity welcomeActivity);
    
    MessageGenerator myMessageGenerat();
}
```

#### 5 . App

```java
public class App extends Application {

    private static App app;
    private MemberAppComponent memberAppComponent;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        memberAppComponent = DaggerMemberAppComponent.builder()
                .memberDataModule(new MemberDataModule(getApplicationContext()))
                .dateAndTimeModule(new DateAndTimeModule())
                .build();
    }

    public MemberAppComponent getMemberAppComponent() {
        return this.memberAppComponent;
    }

}
```

#### 6 . Activity

MainActivity.java

```java
public class MainActivity extends AppCompatActivity {
    @Inject
    @Named("local")
    MemberDataManager memberDataManager;

    @Inject
    @Named("dd-MMM-yyyy")
    String currentDate;


    @BindView(R.id.etMemberId)
    EditText etMemberId;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvResult)
    TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        App.getApp().getMemberAppComponent().inject(this);

        tvResult.setText(currentDate);


    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
        if ((etMemberId.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(), "Member ID is empty", Toast.LENGTH_SHORT).show();
        } else {

            String input = etMemberId.getText().toString();


            // String result=new MemberDataManager().checkMemberStatus(input);
            String result = memberDataManager.checkMemberStatus(input);


            if (result.equals("Access Denied")) {
                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                tvResult.setText(result);

            } else {

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        }

    }
}
```

WelcomeActivity.java

```java
public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.tvResultAtWelcome)
    TextView tvResultAtWelcome;

    @Inject
    MessageGenerator messageGenerator;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        (App.getApp().getMemberAppComponent())
                .newWelcomeActivityComponent(new MessagesModule())
                .inject(this);

        tvResultAtWelcome.setText(getIntent().getStringExtra("result"));

        tvMessage.setText(messageGenerator.getWelcomeMessage());
        
    }
}
```



# Butter Knife

Android butter knife is open source **view injection library**.

##### Why we should use butter knife?

- Butter Knife is a simple and lightweight library, It supports to write clean, optimized code. 
- Butter Knife only uses one line to populate a View field, whereas normally you'd have to use two three. And you can even generate that one line using an Android Studio Plugin(Zelezny).    



To setup butter knife library add following lines to build.gradle file dependency section

```groovy
dependencies {
    implementation 'com.jakewharton:butterknife:8.8.1'
	annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
}
```

## Zelezny

zelezny is a simple android studio plug-in which allow developers to generate butter knife view injections.

Using zelezny we jsut need to right click on activity layout from java file and select generate option there we will see one new oprion **Generate butterKnife injections** . Then just one pop-up will open and we have select component which we need to bind and click ok. 

```java
@BindView(R.id.tvTitle)
TextView tvTitle;
@BindView(R.id.ivMember)
ImageView ivMember;
@BindView(R.id.etMemberId)
EditText etMemberId;
@BindView(R.id.btnSubmit)
Button btnSubmit;
```

That's it, it will automatically generate code for us. And also add unbind line in onDistroy method to unbind views. 

```java
// onCreate
binder = ButterKnife.bind(this);
// onDestroy
binder.unbind();
```