//package com.tptien.tstudy.ViewPagerCards;
//
//import android.content.ClipData;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.google.android.material.card.MaterialCardView;
//import com.tptien.tstudy.R;
//
//import java.util.List;
//
//public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
//    private List<CardItem> cardItems;
//    private ViewPager2 viewPager2;
//
//    public SliderAdapter(List<CardItem> cardItems, ViewPager2 viewPager2) {
//        this.cardItems = cardItems;
//        this.viewPager2 = viewPager2;
//    }
//
//    @NonNull
//    @Override
//    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new SliderViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.item_carview,
//                        parent,
//                        false
//                )
//        );
//    }
//    @Override
//    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {
//        holder.setContentCard(cardItems.get(position));
//        if(position==cardItems.size()-2){
//            //viewPager2.post(runnable);
//        }
////        CardItem cardItem =cardItems.get(position);
////        materialCardView = holder.findViewById(R.id.cardView);
////        materialCardView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(toggleButton.isClickable()){
////                    toggleButton.setTextOn(cardItem.getTxtMeaning());
////                }
////                else{
////                    toggleButton.setTextOff(cardItem.getTxtWord());
////                }
////
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cardItems.size();
//    }
//
//    public static class SliderViewHolder extends RecyclerView.ViewHolder{
//        private ImageView imageView;
//        private TextView textView;
//        //private ToggleButton toggleButton;
//        private static int count =0;
//        private MaterialCardView materialCardView;
//
//        SliderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView =itemView.findViewById(R.id.img_imageCard);
//            //toggleButton =itemView.findViewById(R.id.tob_wordMean);
//            materialCardView =itemView.findViewById(R.id.cardView);
//            textView =itemView.findViewById(R.id.textWordMean);
//        }
//        void setContentCard(final CardItem cardItem){
//            imageView.setImageResource(cardItem.getImageView());
//            textView.setText(cardItem.getTxtWord());
////            toggleButton.setTextOff(cardItem.getTxtWord());
////            toggleButton.setTextOn(cardItem.getTxtMeaning());
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //setToggleButtonOnClick();
//                    count++;
//                    if(count %2 !=0){
//                        textView.setText(cardItem.getTxtMeaning());
//                    }
//                    else{
//
//                        textView.setText(cardItem.getTxtWord());
//                    }
//                }
//            });
//
//        }
//
//    }
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            cardItems.addAll(cardItems);
//            notifyDataSetChanged();
//        }
//    };
//}
