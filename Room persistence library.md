# Room persistence library

```
The room persistence library 

	provide an abstraction layer over SQLIte 

		to allow more fluent,robust database.

```



**Major problem with SQLite usage is**

- There is no compile time verification of row-queries.
- As you schema changes you need to update the affected SQL queries.
- you need to use lots of boilerplate code to convert between SQL queries and java data object 



All above concerns are taken care by Room persistence library that was introduced last month in Google I/O 2017. 

Lets start with the setup Room persistence library

### step 1 : Add dependency in build.gradle

```groovy
implementation "android.arch.persistence.room:runtime:1.0.0-beta2"

annotationProcessor "android.arch.persistence.room:compiler:1.0.0-beta2"
```

**Note:** The reason why annotation processor is needed is because all operations like Insert, Delete, Update etc are annotated. 



**There are three major components in Room:** 

- **Entity**

  Entity represent data for a single row, class need to be annotate with @Entity

- **DAO** 

  Data Access Object defines the method that access the database 

- **Database**

  Database is a holder class that use annotation to define the list of entities and database version. this class content define the list of DAO

### Step 2: <u>Entity class</u>  *Component 1*  

Entity class is nothing but a model class annotated with @Entity where all the variables become column name for a table and name of class become name of table.

```java
@Entity
public class Note{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;


    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    public Note(int id,String Title, String discription){
        this.id=id;
        this.Title=Title;
        this.discription=discription;
    }
    
    // to ignore anything by room just add @ignor annotation
    	@Ignore
        public Note(int id){
            this.id=id;
            this.Title="Default Title";
            this.discription="default discription";
    }
  
}
```

To specify field primary key we need to annotate that field with `@PrimaryKey` and for auto increment `(autoGenerate = true)`

we can specify different name for column rather than field name by annotation

 `@ColumnInfo(name = "created_at")`

As SQLite does not support date data-type we need to specify how that (date) object should going store in database by providing definition `@TypeConverters({TimestampConverter.class})`



### Step 3: <u>DAO class</u>  *Component 2* 

DAO interface acts is an intermediary between the user and the database. All the operation to be performed on a table has to be define here.

```java
// NoteDao.java
@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(Note note);


    @Query("SELECT * FROM Note ORDER BY created_at desc")
    LiveData<List<Note>> fetchAllTasks();


    @Query("SELECT * FROM Note WHERE id =:taskId")
    LiveData<Note> getTask(int taskId);


    @Update
    void updateTask(Note note);


    @Delete
    void deleteTask(Note note);
}	
```

### Step 3: <u>Database  class</u>  *Component 3*

Database is abstract class where you define all the entities that means all the table that you want to create for that database.

```java
@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NoteDatabase extends RoomDatabase {
    
    public static final String DATABASE_NAME = "my_database_name";
    private static AppDatabase INSTANCE;

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), 						 								AppDatabase.class, DATABASE_NAME)
									//.fallbackToDestructiveMigration()
                    				.allowMainThreadQueries()
                    				.build();
    
    // for migration from version 1 to 2
        public static final Migration MIGRATION_1_2 = new Migration(1,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE note "
                    + " ADD COLUMN last_update INTEGER NOT NULL default 101 ");
        }
    };
       
    // list of all dao
     public abstract NoteDao noteDao();
}
```

Here in this small example we only have one table which is note we have add it in entities section inside database annotation, after that we have mention database version

 **For Complex data we can use converter**

```java
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
```

### Let's start with the operations

Sample Insert code: 

```java
new Thread(new Runnable() {
 @Override
 public void run() {
     Note note = new Note(0,"study Title", "Desc This naote is all about study");
     Appdatabase.getInstance().noteDao().insert(note);
 	}
 }) .start();
```



### To implement relation

let's take owner and business example 

#### Many-to-Many relation

```java
// OwnerEntity.java
@Entity
public class OwnerEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int oid;
    
    private String ownerName;

    public OwnerEntity(int oid, String ownerName) {
        this.oid = oid;
        this.ownerName = ownerName;
    }
}

// BusinessEntity.java
@Entity
public class BusinessEntity {

    @PrimaryKey(autoGenerate = true)
    private int bid;
    
    private String businessName;

    public BusinessEntity(int bid, String businessName) {
        this.bid = bid;
        this.businessName = businessName;
    }

}
```

Here we need to create third table with ownerId and bussinessId

```java
//Business_OwnerEntity 
@Entity(
        primaryKeys = { "businessId", "ownerId" },
        foreignKeys = {
                @ForeignKey(entity = BusinessEntity.class,
                        parentColumns = "bid",
                        childColumns = "businessId",
                        onDelete = CASCADE),
                @ForeignKey(entity = OwnerEntity.class,
                        parentColumns = "oid",
                        childColumns = "ownerId",
                        onDelete = CASCADE)    // cascade - remove all related entries
        })
public class BusinessOwnerEntity {

    private int businessId,ownerId;

    public BusinessOwnerEntity(int businessId, int ownerId) {
        this.businessId = businessId;
        this.ownerId = ownerId;
    }
}
```

#### One-to-One relation

ContactEntity class can represent like 

```java
@Entity(foreignKeys = @ForeignKey(entity = BusinessEntity.class,
        parentColumns = "bid",
        childColumns = "businessId",
        onDelete = CASCADE))
public class ContactEntity {

    @PrimaryKey(autoGenerate = true)
    private int cid;
    private int phone;
    
    public ContactEntity(int cid, int businessId,int phone) {
        this.cid = cid;
        this.businessId = businessId;
        this.phone = phone;
    }
    
    
```

#### One-to-Many

feedbackEntity.java   [ one's key in many table]

```
@Entity(foreignKeys = @ForeignKey(entity = BusinessEntity.class,
        parentColumns = "bid",
        childColumns = "businessId",
        onDelete = CASCADE))
public class FeedbackEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int fid;

    private int businessId;
    private String feedback;
    
    public FeedbackEntity(int fid, int businessId,String feedback) {
        this.fid = fid;
        this.businessId = businessId;
        this.feedback = feedback;
     }
  }
```
