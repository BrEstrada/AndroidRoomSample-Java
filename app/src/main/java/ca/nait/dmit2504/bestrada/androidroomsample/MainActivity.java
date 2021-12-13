package ca.nait.dmit2504.bestrada.androidroomsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_BIN_REQUEST_CODE = 1;

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
    }

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
}