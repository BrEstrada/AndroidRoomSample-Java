package ca.nait.dmit2504.bestrada.androidroomsample;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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