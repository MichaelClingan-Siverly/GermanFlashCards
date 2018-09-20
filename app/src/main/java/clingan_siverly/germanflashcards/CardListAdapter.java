package clingan_siverly.germanflashcards;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mike.utils.WordPair;

public class CardListAdapter extends android.support.v7.widget.RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<WordPair> pairs;
    private final MainActivity mActivity;

    public CardListAdapter(List<WordPair> wordPairs, MainActivity activity){
        pairs = wordPairs;
        mActivity = activity;
    }

    @NonNull
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_item, parent, false);
        return new ViewHolder(v, mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListAdapter.ViewHolder holder, int position) {
        holder.getEnglishTextView().setText(pairs.get(position).getEnglishWord());
        holder.getGermanTextView().setText(pairs.get(position).getGermanWord());
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView gTextView;
        private TextView eTextView;
        public ViewHolder(View v, final MainActivity activity) {
            super(v);
            gTextView = v.findViewById(R.id.rowGermanText);
            eTextView = v.findViewById(R.id.rowEnglishText);
            gTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startCardFrag(getAdapterPosition(), false);
                }
            });
            eTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startCardFrag(getAdapterPosition(), true);
                }
            });
        }
        public TextView getGermanTextView(){
            return gTextView;
        }
        public TextView getEnglishTextView(){
            return eTextView;
        }
    }


}
