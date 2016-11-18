package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 18.11.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EntryViewHolder> {
    static final String TAG = ConstantManager.TAG_PREFIX + "RVAdapter";

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        static final String TAG = ConstantManager.TAG_PREFIX + "EntryViewHolder";

        CardView cardView;
        TextView cardName;
        TextView cardSize;
        TextView cardSeeders;

        EntryViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "onCreateEntryViewHolder");
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardName = (TextView) itemView.findViewById(R.id.card_name);
            cardSize = (TextView) itemView.findViewById(R.id.card_size);
            cardSeeders = (TextView) itemView.findViewById(R.id.card_seeders);
        }
    }

    List<EntrysFromSite> siteList;

    RVAdapter(List<EntrysFromSite> siteList) {
        Log.d(TAG, "onCreate");
        this.siteList = siteList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Log.d(TAG, "onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        EntryViewHolder viewHolder = new EntryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EntryViewHolder entryViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder");
        entryViewHolder.cardName.setText(siteList.get(i).getName());
        entryViewHolder.cardSeeders.setText(String.valueOf(siteList.get(i).getSeeders()));
        entryViewHolder.cardSize.setText(siteList.get(i).getSize());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return siteList.size();
    }
}
