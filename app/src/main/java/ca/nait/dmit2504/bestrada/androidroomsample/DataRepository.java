package ca.nait.dmit2504.bestrada.androidroomsample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

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
