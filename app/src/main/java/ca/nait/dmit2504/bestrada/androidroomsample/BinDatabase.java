package ca.nait.dmit2504.bestrada.androidroomsample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
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
}