package clingan_siverly.germanflashcards;

interface MyFrags {
    void showFrag(ShowsMyFrags caller);

    interface ShowsMyFrags{
        void showFrag(CardFragment frag);
    }
}
