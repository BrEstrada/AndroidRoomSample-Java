package ca.nait.dmit2504.bestrada.androidroomsample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bin bin);

    @Query("SELECT * FROM bin_table")
    LiveData<List<Bin>> getAllBins();

    @Delete
    void deleteBin(Bin bin);
}
