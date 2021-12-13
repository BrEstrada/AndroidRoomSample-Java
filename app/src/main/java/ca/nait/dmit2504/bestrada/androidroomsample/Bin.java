package ca.nait.dmit2504.bestrada.androidroomsample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity class - represents a Bin in the database.
 */

@Entity(tableName = "bin_table")
public class Bin {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String mBin;

    public Bin(@NonNull String bin) {
        this.mBin = bin;
    }

    @Ignore
    public Bin(int id, @NonNull String bin) {
        this.id = id;
        this.mBin = bin;
    }

    public String getBin() {
        return this.mBin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
