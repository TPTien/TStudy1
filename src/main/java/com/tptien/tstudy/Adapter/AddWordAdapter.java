package com.tptien.tstudy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tptien.tstudy.MainActivity;
import com.tptien.tstudy.R;
import com.tptien.tstudy.Vocabulary;

import java.io.IOException;
import java.util.ArrayList;

public class AddWordAdapter extends RecyclerView.Adapter<AddWordAdapter.AddWordViewHolder> {
    private Context mContext;
    private ArrayList<Vocabulary> mlistVoca ;
    private ImageOnClick imageOnClick;
    public interface ImageOnClick{
        void onImageClick(int position);
    }
    public void setOnClickListener(ImageOnClick imageOnClick){
        this.imageOnClick=imageOnClick;
    }
    public AddWordAdapter(ArrayList<Vocabulary> mlistVoca,Context mContext) {
        this.mlistVoca = mlistVoca;
        this.mContext= mContext;
    }

    @NonNull
    @Override
    public AddWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_add_word,parent,false);
        AddWordViewHolder viewHolder = new AddWordViewHolder(v,imageOnClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddWordViewHolder holder, final int position) {
        Vocabulary currentItem= mlistVoca.get(position);
        holder.tilayout_word.setText(currentItem.getWord());
        holder.tilayout_meaning.setText(currentItem.getMeaning());
        holder.imgWord.setImageBitmap(currentItem.getBitmapImage());
    }

    @Override
    public int getItemCount() {
        return mlistVoca.size();
    }
    public void removeItem(int position){
        mlistVoca.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Vocabulary vocabulary,int position){
        mlistVoca.add(position,vocabulary);
        notifyItemInserted(position);
    }
    public ArrayList<Vocabulary> getData(){
        return mlistVoca;
    }


    public class AddWordViewHolder extends RecyclerView.ViewHolder {
        public EditText tilayout_word,tilayout_meaning;
        public ImageButton imgWord;
        private ImageOnClick imageOnClick;
        public AddWordViewHolder(@NonNull View itemView , final ImageOnClick  imageOnClick) {
            super(itemView);
            this.imageOnClick =imageOnClick;
            tilayout_word =itemView.findViewById(R.id.newCourse_tilWord);
            tilayout_meaning=itemView.findViewById(R.id.newCourse_tilMeaning);
            imgWord=itemView.findViewById(R.id.img_word);

            tilayout_word.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mlistVoca.get(getAdapterPosition()).setWord(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tilayout_meaning.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mlistVoca.get(getAdapterPosition()).setMeaning(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            imgWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent();
////                    Bundle bundle= new Bundle();
////                    bundle.putInt("position",getAdapterPosition());
////                    intent.putExtras(bundle);
//                    //intent1.putExtra("position",getAdapterPosition());
//                    Log.d("pos", String.valueOf(getAdapterPosition()));
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"),101);
//                    //((Activity)mContext).setResult(Activity.RESULT_OK,intent1);
//                    //((Activity)mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"),101 );
//                    //((Activity)mContext).finish();
                    int position= getAdapterPosition();
                    if(position !=RecyclerView.NO_POSITION) {
                        imageOnClick.onImageClick(position);
                    }
                }
            });
        }
    }
}
