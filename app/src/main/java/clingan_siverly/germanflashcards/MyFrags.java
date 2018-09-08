package clingan_siverly.germanflashcards;

import android.support.v4.app.Fragment;

interface MyFrags {
    void showFrag(ShowsMyFrags caller);

    interface ShowsMyFrags{
        void showFrag(CardFragment frag);
        void showFrag(CardListFragment frag);
        void dismissFrag(Fragment frag);
    }
}
