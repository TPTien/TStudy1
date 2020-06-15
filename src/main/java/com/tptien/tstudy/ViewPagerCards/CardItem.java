package com.tptien.tstudy.ViewPagerCards;

import android.widget.ImageView;
import android.widget.ToggleButton;

public class CardItem {
    private int image;
    private String txtWord;
    private String txtMeaning;
    public CardItem(int image, String word, String meaning){
        this.image=image;
        this.txtWord=word;
        this.txtMeaning=meaning;
    }

    public int getImageView() {
        return image;
    }

    public String getTxtWord() {
        return txtWord;
    }

    public String getTxtMeaning() {
        return txtMeaning;
    }
}
