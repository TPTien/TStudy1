package com.tptien.tstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class Vocabulary {
    private String word;
    private String meaning;
    private Uri imageWord;
    private Bitmap bitmapImage;
    private String realPathFile;
    private String fullPathFile;

    public Vocabulary(String word, String meaning, Uri imageWord,String realPathFile,String fullPathFile) {
        this.word = word;
        this.meaning = meaning;
        this.imageWord = imageWord;

        this.realPathFile= realPathFile;
        this.fullPathFile =fullPathFile;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Uri getImageWord() {
        return imageWord;
    }

    public void setImageWord(Uri imageWord) {
        this.imageWord = imageWord;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getRealPathFile() {
        return realPathFile;
    }

    public void setRealPathFile(String realPathFile) {
        this.realPathFile = realPathFile;
    }

    public String getFullPathFile() {
        return fullPathFile;
    }

    public void setFullPathFile(String fullPathFile) {
        this.fullPathFile = fullPathFile;
    }
}
