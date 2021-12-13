# AndroidRoomSample-Java Tutorial

## DMIT2504 Final Project Tutorial

> This is a tutorial for using Room on Android created using Java in Android Studio. This tutorial will demo insert using Android Room methods.
> For more information see the [Android Developer Reference.](https://developer.android.com/training/data-storage/room)

## Step 1

Create a new Android Studio Project using the **_Empty Activity_** template.

## Step 2

Update the Gradle files to include dependencies for Room.
Add the following to `build.gradle` (**Module: app**):

```groovy
dependencies {
    // Room components
    implementation "android.arch.persistence.room:runtime:$rootProject.roomVersion"
    annotationProcessor "android.arch.persistence.room:compiler:$rootProject.roomVersion"

    // Lifecycle components
    implementation "android.arch.lifecycle:extensions:$rootProject.archLifecycleVersion"
    annotationProcessor "android.arch.lifecycle:compiler:$rootProject.archLifecycleVersion"
}
```

Add the following to the `build.gradle` (**Project: app**) file:

```groovy
ext {
    roomVersion = '1.1.1'
    archLifecycleVersion = '1.1.1'
}
```

> **_Remember To Sync the Build.Gradle Files_**

## Step 3

Create an Entity Class with Android Room annotations. More info can be found [here.](https://developer.android.com/training/data-storage/room/defining-data.html)

```java
/**
 *  Entity class - represents a Bin in the database.
 */

@Entity(tableName = "bin_table")
public class Bin {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String mBin;

    public Bin(@NonNull String bin) { this.mBin = bin; }

    @Ignore
    public Bin(int id, @NonNull String bin) {
        this.id = id;
        this.mBin = bin;
    }

    public String getBin() { return this.mBin; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
```

## Step 4

Create Data Access Object [(DAO)](https://developer.android.com/training/data-storage/room/accessing-data.html) interface class with Room annotations. The DAO validates the SQL and associates it with methods.

> (_NOTE:_ This demo only uses the insert and query method, other CRUD operations can be implemented here too. A delete method has also been included.)

```java
@Dao
public interface BinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bin bin);

    @Query("SELECT * FROM bin_table")
    LiveData<List<Bin>> getAllBins();

    @Query("SELECT * FROM bin_table LIMIT 1")
    Bin[] getAnyBin();

    @Delete
    void deleteBin(Bin bin);
}
```

## Step 5

Add a Room Database class.

```java
@Database(entities = {Bin.class}, version = 1, exportSchema = false)
public abstract class BinDatabase extends RoomDatabase {
    public abstract BinDao binDao();

    private static BinDatabase INSTANCE;

    public static BinDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BinDatabase.class) {
                if (INSTANCE == null) {
                    // Create the database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BinDatabase.class, "bin_database")
                            .fallbackToDestructiveMigration()
                            // .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
```

## Step 6

Create Repository Class.

```java
public class DataRepository {
    private BinDao mBinDao;
    private LiveData<List<Bin>> mAllBins;

    DataRepository(Application application) {
        BinDatabase db = BinDatabase.getDatabase(application);
        mBinDao = db.binDao();
        mAllBins = mBinDao.getAllBins();
    }

    LiveData<List<Bin>> getAllBins() {return mAllBins;}

    public void insert(Bin bin) { new insertAsyncTask(mBinDao).execute(bin);}
    public  void deleteBin(Bin bin) { new deleteBinAsyncTask(mBinDao).execute(bin); }

    /**
     * Insert a bin into the database.
    */
    private static class insertAsyncTask extends AsyncTask<Bin, Void, Void> {
        private BinDao mAsyncTaskDao;

        insertAsyncTask(BinDao dao) {mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Bin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     *  Delete a single Bin from the database.
     */
    private static class deleteBinAsyncTask extends AsyncTask<Bin, Void, Void> {
        private BinDao mAsyncTaskDao;

        deleteBinAsyncTask(BinDao dao) { mAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Bin... bins) {
            mAsyncTaskDao.deleteBin(bins[0]);
            return null;
        }
    }
}
```

## Step 7

Create ViewModel Class.

```java
public class BinViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    private LiveData<List<Bin>> mAllBins;

    public BinViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllBins = mRepository.getAllBins();
    }

    LiveData<List<Bin>> getAllBins() {return mAllBins;}

    public void insert(Bin bin) {mRepository.insert(bin);}

    public void deleteBin(Bin bin) { mRepository.deleteBin(bin); }
}
```

## Step 8

Update XML. This tutorial assumes basic knowledge of xml and can be configured however the user likes.

### 8.1 Recycler View Item layout

Add a new resource layout file for a Recycler View Item.

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android=
      "http://schemas.android.com/apk/res/android"
   android:orientation="vertical" android:layout_width="match_parent"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_height="wrap_content">

   <TextView
       android:id="@+id/recyclerview_item_bin_name_textview"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       tools:text="placeholder text"
       android:elevation="8dp"
       android:padding="16dp"
       android:textAppearance="@style/TextAppearance.AppCompat.Large" />
</LinearLayout>
```

### 8.2 Recycler View and Floating Action Button

In the main layout xml file replace the TextView with a RecyclerView and add a floating action button to the bottom corner.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">


    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/activity_main_recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" >

    </androidx.recyclerview.widget.RecyclerView>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/floatingActionButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="16dp"
        android:clickable="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:srcCompat="@android:drawable/ic_input_add" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 9

Create an Adapter class for the Recycler view.

```java
public class BinListAdapter extends RecyclerView.Adapter<BinListAdapter.BinViewHolder> {

    private final LayoutInflater mInflater;
    private List<Bin> mBins; // cached copy of bins

    BinListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public BinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
        mInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new BinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BinViewHolder holder, int position) {
        if (mBins != null) {
            Bin current = mBins.get(position);
            holder.binItemView.setText(current.getBin());
        } else {
            holder.binItemView.setText("No Bin Name");
        }
    }

    /**
     * Associates a list of storage Bins with this adapter.
     * @param bins
     */
    void setBins(List<Bin> bins) {
        mBins = bins;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBins != null)
            return mBins.size();
        else return 0;
    }

    public Bin getBinAtPosition(int position) { return mBins.get(position); }

    class BinViewHolder extends RecyclerView.ViewHolder {
        private final TextView binItemView;

        private BinViewHolder(View itemView) {
            super(itemView);
            binItemView =
            itemView.findViewById(R.id.recyclerview_item_bin_name_textview);
        }
    }
}
```

## Step 10

Add the recycler view to the main activity.

```java
    RecyclerView recyclerView = findViewById(R.id.activity_main_recyclerview);
    final BinListAdapter adapter = new BinListAdapter(this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
```

> _The application can now be run to test for errors._

## Step 11

Populate the database with data to test read functionality.
Add the following to the BinDatabase class:

```java
    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BinDao binDao;

        // Initial test bins
        private static String [] bins = {"bin1", "bin2", "bin3"};

        PopulateDbAsync(BinDatabase db) {
            binDao = db.binDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
        // If we have no words, then create the initial list of words.
            if (binDao.getAnyBin().length < 1) {
                for (int i = 0; i <= bins.length - 1; i++) {
                    Bin bin = new Bin(bins[i]);
                    binDao.insert(bin);
                }
            }
            return null;
        }
    }
```

### 11.1

Uncomment the `.addCallback(sRoomDatabaseCallback)` before the `.build()` in the database class.

## Step 12

Connecting the Adapter to the UI

### 12.1 Create a member variable for the ViewModel

In the main activity create a member variable for the ViewModel Class.

```java
private BinViewModel mBinViewModel;
```

### 12.2 Get a ViewModel from ViewModelProvider method

In the onCreate of the Main Activity add the following:

```java
    mBinViewModel = new ViewModelProvider(this).get(BinViewModel.class);
    mBinViewModel.getAllBins().observe(this, new Observer<List<Bin>>() {
        @Override
        public void onChanged(List<Bin> bins) {
            adapter.setBins(bins);
        }
    });
```

> The applicatoin should now demo data added to the database and the reading of data from the local database.

## Step 13

### 13.1 Create a new empty activity for adding bins.

This is the class file:

```java
public class NewBinActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.brestrada.binventory.REPLY";

    private EditText mEditTextBinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bin);

        mEditTextBinView = findViewById(R.id.activity_new_bin_edittext);

        final Button saveButton = findViewById(R.id.activity_new_bin_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditTextBinView.getText())) {
                    // No bin name has been entered, set the result as cancelled.
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    // get the name of the bin the user has entered.
                    String binName = mEditTextBinView.getText().toString();
                    // put the new bin in the extras for the reply Intent
                    replyIntent.putExtra(EXTRA_REPLY, binName);
                    // set result status
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
```

And this is the XML for the activity:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".NewBinActivity">

    <EditText
        android:id="@+id/activity_new_bin_edittext"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="16dp"
        android:ems="10"
        android:hint="Bin Name"
        android:minHeight="48dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/activity_new_bin_save_button"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="16dp"
        android:text="Save"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/activity_new_bin_edittext" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 13.2 Update the Main Activity

Add the following to the MainActivity.class.

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BIN_REQUEST_CODE && resultCode == RESULT_OK) {
            Bin bin = new Bin(data.getStringExtra(NewBinActivity.EXTRA_REPLY));
            // insert into the database.
            mBinViewModel.insert(bin);
        } else {
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
```

Define the request code at the top:

```java
public static final int NEW_BIN_REQUEST_CODE = 1;
```

And add the logic for the Floating Action Button in the onCreate() method:

```java
// setup for Floating Action Button for adding.
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() { // Set on click listener for fab.
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, NewBinActivity.class);
               // Starts a new activity with request code for new Bin, in this case it is 1
               startActivityForResult(intent, NEW_BIN_REQUEST_CODE);
           }
        });
```

## Comeplete! The Appication should now Run!
