# AndroidRoomSample-Java Tutorial

## DMIT2504 Final Project Tutorial

> This is a tutorial for using Room on Android created using Java in Android Studio. This tutorial will demo insert using Android Room methods.

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

    @Delete
    void deleteBin(Bin bin);
}
```

## Step 5
