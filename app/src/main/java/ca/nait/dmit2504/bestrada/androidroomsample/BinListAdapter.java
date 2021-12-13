package ca.nait.dmit2504.bestrada.androidroomsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BinListAdapter extends RecyclerView.Adapter<BinListAdapter.BinViewHolder> {

    private final LayoutInflater mInflater;
    private List<Bin> mBins; // cached copy of bins

    BinListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public BinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                mInflater.inflate(R.layout.recyclerview_item,parent,false);
        return new BinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BinViewHolder holder, int position) {
        if (mBins != null) {
            Bin current = mBins.get(position);
            holder.binItemView.setText(current.getBin());
        } else {
            holder.binItemView.setText("No Bin Name");
        }
    }

    /**
     * Associates a list of storage Bins with this adapter.
     * @param bins
     */
    void setBins(List<Bin> bins) {
        mBins = bins;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBins != null)
            return mBins.size();
        else return 0;
    }

    public Bin getBinAtPosition(int position) { return mBins.get(position); }

    class BinViewHolder extends RecyclerView.ViewHolder {
        private final TextView binItemView;

        private BinViewHolder(View itemView) {
            super(itemView);
            binItemView =
                    itemView.findViewById(R.id.recyclerview_item_bin_name_textview);
        }
    }
}
