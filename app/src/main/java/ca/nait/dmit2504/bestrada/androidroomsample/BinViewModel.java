package ca.nait.dmit2504.bestrada.androidroomsample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

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