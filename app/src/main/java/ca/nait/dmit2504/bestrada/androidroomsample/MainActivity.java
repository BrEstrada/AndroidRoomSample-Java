package ca.nait.dmit2504.bestrada.androidroomsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BinViewModel mBinViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add recycler view
        RecyclerView recyclerView = findViewById(R.id.activity_main_recyclerview);
        final BinListAdapter adapter = new BinListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBinViewModel = new ViewModelProvider(this).get(BinViewModel.class);
        mBinViewModel.getAllBins().observe(this, new Observer<List<Bin>>() {
            @Override
            public void onChanged(List<Bin> bins) {
                adapter.setBins(bins);
            }
        });
    }
}