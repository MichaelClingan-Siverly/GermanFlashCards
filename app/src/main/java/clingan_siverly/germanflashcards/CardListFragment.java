package clingan_siverly.germanflashcards;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CardListFragment extends Fragment implements MyFrags {
    public final static String tag = "listFrag";
    private RecyclerView mView;
    private WordModel mWordModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if(activity != null) {
            mWordModel = ViewModelProviders.of(activity).get(WordModel.class);
            if(mWordModel.getNumWordPairs() == 0){
                Toast.makeText(activity, "No words saved. Try again later.",Toast.LENGTH_SHORT).show();
                activity.dismissFrag(this);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.word_list_layout, container, false);
        mView = v.findViewById(R.id.wordListRecyclerView);
        mView.setHasFixedSize(true);

        setLayoutManager();
        RecyclerView.Adapter adapter = new CardListAdapter(mWordModel.getWordPairs(), (MainActivity)getActivity());
        mView.setAdapter(adapter);

        return v;
    }

    private void setLayoutManager(){
        int firstPositionShown = 0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        if(mView.getLayoutManager() != null){
            firstPositionShown = ((LinearLayoutManager)mView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mView.setLayoutManager(layoutManager);
        mView.scrollToPosition(firstPositionShown);
    }


    @Override
    public void showFrag(ShowsMyFrags caller) {
        caller.showFrag(this);
    }
}
