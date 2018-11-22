# MVP Architecture

Why does our Android apps need a good architecture

1. Testability
2. Simplicity 
3. Readability
4. Maintainability
5. Expandability
6. Cost Efficiency



MVP Architecture divides our application in three parts.

- Model
- View
- presenter

![mvp](D:\Notes\img\mvp.png)



**Model**

- This handles the data part of our application.
- The model hold the business logic of the application. It control how data is created, stored and modified.

**View**

- this is responsible for laying out views with the relevant data as instructed by the presenter.
- The view is passive interface that displays data and routes user action to the presenter.



**Presenter**

Its acts as a bridge that connect a model and a view. It retrieves data from the model and returns it formatted to the view. But unlike the typical MVC, it also decides what happens when user interact with the user.



MVP makes views independent form our data source. We divide the the application into at least three different layers, which let us test them independently. With MVP we are able to take most of logic out from activities so that we can test it without using instrumentation tests.



Guidelines

1. Activity, Fragment and custom view act as the view part  of the application.
2. The presenter is responsible  for listening to the use interaction (om the view) and model updates. (Firebase and API).
3. Usually, a view and presenter are in a one to one relationship. One presenter class manages one view at a time.
4. Presenter is responsible for handling all the background task. 



## Example

Let's create a simple application which contains two editText, where user can enter firstname and lastname and after a submit button clicked it shows welcome message with firstname and lastname.



##### Main Interface - WelcomeContract

Let's create main interface which is going to hold nested interfaces for the view, presenter, model of our simple MVP application. 

Main interface hold different interfaces. let start with interface View.

View interface we will declare `showWelcomeMessage` method which will actually going set message to textview.

```java
public interface WelcomeContract {
    
    interface view{
        void showWelcomeMessage();
    }
}
```

next create module interface, it contains two methods to manipulate message set and get.

```java
interface model{
    void setDetails(String firstname,String lastname);
    String getWelcomeMessage();
}
```

next create interface for presenter, model and view deals with the presenter.

When the user add the firstname and lastname and clicked on submit button we need to inform presenter about it, so presenter should have method for that.  View need to load welcomeMessage from the presenter so lets add `loadWelcomeMessage` method.

```java
interface presenter{

    void loadWelcomeMessage();

    void submitName(String firstname,String lastname);
}
```

WelcomeContract interface look likes

```java
public interface WelcomeContract {

    interface view{
        void showWelcomeMessage();
    }

    interface model{
        void setDetails(String firstname,String lastname);
        String getWelcomeMessage();
    }

    interface presenter{

        void loadWelcomeMessage();

        void submitName(String firstname,String lastname);
    }

}
```

##### User.java - Create Model class

```java
public class User {
    String userName;
    String lastName;

    public User(String userName, String lastName) {
        this.userName = userName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```

##### UserDataManager.java

let's create userDataManager java class which implement model interface from welcomeContract

```java
public class UserDataManager implements WelcomeContract.model {
    
    private User user;

    public UserDataManager() {
    }

    @Override
    public void setDetails(String firstname, String lastname) {
        user=new User(firstname,lastname);
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome "+user.getUserName()+" "+user.getLastName();
    }
}
```

##### View - MainActivity

Let's make MainActivity implements view interface

```java
public class MainActivity extends AppCompatActivity implements WelcomeContract.View{
private EditText etLastName;
private TextView tvWelcomeMessage;
private EditText etFirstName;
private Button btnSubmit;

private WelcomeContract.Presenter presenter;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etLastName=(EditText)findViewById(R.id.etLasttName);
    etFirstName=(EditText)findViewById(R.id.etFirtstName);
    tvWelcomeMessage=(TextView)findViewById(R.id.tvWelcomeMessage);
    btnSubmit=(Button)findViewById(R.id.btnSubmit);
    
     presenter=new WelcomePresenter(this);

    btnSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(TextUtils.isEmpty(etFirstName.getText().toString())){
                Toast.makeText(context," Pleas enter first name ",Toast.LENGTH_LONG).show();
            }else{
        presenter.submitName(etFirstName.getText().toString(),etLastName.getText().toString());
        presenter.loadWelcomeMessage();

                }
            }
        });
    }

    @Override
    public void showWelcomeMessage(String welcomeMessage) {
        tvWelcomeMessage.setText(welcomeMessage);
    }
}
```
##### Presenter - WelcomePresenter.java

```java
public class WelcomePresenter implements WelcomeContract.presenter{

    private WelcomeContract.view view;
    private WelcomeContract.model model;

    public WelcomePresenter(WelcomeContract.view view) {
        this.view = view;
        model=new UserDataManager();
    }

    @Override
    public void loadWelcomeMessage() {
        view.showWelcomeMessage(model.getWelcomeMessage());

    }
    
    @Override
    public void submitName(String firstname, String lastname) {
        model.setDetails(firstname,lastname);
    }
}
```

as we are implementing  `WelcomeContract.presenter` interface we need to override `loadWelcomeMessage`, `submitName`



we have violate SOLID principle by creating object using new keyword inside object let's fix it by using butter knife and Dagger2 Dependency injection

### Add Dagger2 Dependency 

create WelcomeModule class to provide dependency

```java
@Module
public class WelcomeModule {

    @Provides
    @Singleton
    private WelcomeContract.model provideWelcomeContractModule(){
        return new UserDataManager();
    }

    @Provides
    @Singleton
     WelcomeContract.presenter provideWelcomeContractPresenter(WelcomeContract.model model){
        return new WelcomePresenter(model);
    }

}
```

we need to do some changes in previous code 

WelcomeContract.java we added one more method in presenter interface `setView`

```java
public interface WelcomeContract {

    interface view{
        void showWelcomeMessage(String message);
    }

    interface model{
        void setDetails(String firstname,String lastname);
        String getWelcomeMessage();
    }

    interface presenter{

        void setView(WelcomeContract.view view);

        void loadWelcomeMessage();

        void submitName(String firstname,String lastname);
    }

}
```

WelcomePresenter.java added model in constructer and need to override method `setView`

```
public class WelcomePresenter implements WelcomeContract.presenter{

    private WelcomeContract.view view;
    private WelcomeContract.model model;


    public WelcomePresenter(WelcomeContract.model model) {
        this.model=model;
    }

    @Override
    public void setView(WelcomeContract.view view) {
       this.view=view;
    }

    @Override
    public void loadWelcomeMessage() {
        view.showWelcomeMessage(model.getWelcomeMessage());

    }

    @Override
    public void submitName(String firstname, String lastname) {
        model.setDetails(firstname,lastname);
    }
}
```