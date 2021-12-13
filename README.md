# AndroidRoomSample-Java

Tutorial for using Room on Android created using Java in Android Studio. This tutorial will demo insert using Android Room methods.

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

Create Entity Class with Android Room annotations. More info can be found [here.](https://developer.android.com/training/data-storage/room/defining-data.html)

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
