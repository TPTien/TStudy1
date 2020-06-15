package com.tptien.tstudy.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tptien.tstudy.R;
import com.tptien.tstudy.User;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> implements Filterable {
    private ArrayList<User> mFriendList;
    private ArrayList<User>mFriendListFiltered;
    private OnItemCheckListener mOnItemCheckListener;
    private User currentUser;

    @Override
    public Filter getFilter() {
        return friendFilter;
    }
    private Filter friendFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint ==null ||constraint.length()==0){
                mFriendListFiltered = mFriendList;
            }else {
                ArrayList<User>filteredFriend= new ArrayList<>();
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(User user:mFriendList){
                    if(user.getDisplayName().toLowerCase().contains(filterPattern) ||
                        user.getUserName().toLowerCase().contains(filterPattern)){
                        filteredFriend.add(user);
                    }
                }
                mFriendListFiltered=filteredFriend;
            }
            FilterResults results =new FilterResults();
            results.values=mFriendListFiltered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values!=null){
                mFriendListFiltered=(ArrayList<User>)results.values;

            }
            notifyDataSetChanged();
        }
    };

    public interface OnItemCheckListener{
        void ItemCheck(String id);
        void ItemUnCheck(String id);
    }


    public FriendAdapter(ArrayList<User> mFriendList,@NonNull OnItemCheckListener mOnItemCheckListener) {
        this.mFriendList = mFriendList;
        this.mOnItemCheckListener =mOnItemCheckListener;
        this.mFriendListFiltered=mFriendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_friend_course,parent,false);
        ViewHolder viewHolder= new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        currentUser =mFriendListFiltered.get(position);
        final String idUser =currentUser.getIdUser();
        holder.tv_nameFriend.setText(currentUser.getDisplayName());
        holder.tv_usernameFriend.setText(currentUser.getUserName());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.cb_addFriend.isChecked()){
                    holder.cb_addFriend.setChecked(false);
                    mOnItemCheckListener.ItemUnCheck(idUser);
                }else {
                    holder.cb_addFriend.setChecked(true);
                    mOnItemCheckListener.ItemCheck(idUser);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nameFriend;
        private  TextView tv_usernameFriend;
        private CheckBox cb_addFriend;
        private OnItemCheckListener mOnItemCheckListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameFriend =(TextView)itemView.findViewById(R.id.tv_nameFriend);
            cb_addFriend=(CheckBox)itemView.findViewById(R.id.cb_addFriend);
            cb_addFriend.setClickable(false);
            tv_usernameFriend=(TextView) itemView.findViewById(R.id.tv_userNameFriend);


        }
        public void setOnClickListener(View.OnClickListener onClickListener){
            itemView.setOnClickListener(onClickListener);
        }
    }
}
