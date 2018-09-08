package clingan_siverly.germanflashcards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CardListFragment extends Fragment implements MyFrags {
    public final static String tag = "listFrag";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.word_list_layout, container, false);
    }


    @Override
    public void showFrag(ShowsMyFrags caller) {
        caller.showFrag(this);
    }
}
