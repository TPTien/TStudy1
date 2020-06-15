package com.tptien.tstudy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.tptien.tstudy.Course;
import com.tptien.tstudy.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PropertyPermission;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements Filterable {
    private  ArrayList<Course> mCoursesList;
    private ArrayList<Course> mCoursesListFull;
    public Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnNoteListener mOnNoteListener;
    public CourseAdapter(Context mContext,ArrayList<Course> coursesItem){
        this.mContext =mContext;
        this.mCoursesList =coursesItem;
//        mCoursesListFull =new ArrayList<>(coursesItem);
        this.mCoursesListFull=coursesItem;
        mLayoutInflater =LayoutInflater.from(mContext);
    }
    public void setUpRecycleView(RecyclerView mRecyclerView, LinearLayoutManager mLayoutManager, CourseAdapter mCourseAdapter){
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager= new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCourseAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCourseAdapter);
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carview_itemcourse,parent,false);
        CourseViewHolder courseViewHolder =new CourseViewHolder(v,mOnNoteListener);
        return  courseViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourseItem= mCoursesListFull.get(position);
        holder.tv_courseName.setText(currentCourseItem.getCourseName());
        holder.tv_courseHost.setText(currentCourseItem.getNameHostCourse());
        if(currentCourseItem.getNumberVoca()==null){
            holder.tv_numberVoca.setText("0 từ");
        }
        else {
            holder.tv_numberVoca.setText(currentCourseItem.getNumberVoca()+" từ");
        }
        if (currentCourseItem.getNumberMember()== null){
            holder.tv_numberMember.setText("0 thành viên");
        }
        else {
            holder.tv_numberMember.setText(currentCourseItem.getNumberMember()+" thành viên");
        }
    }

    @Override
    public int getItemCount() {
        return mCoursesListFull.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_courseName,tv_courseHost,tv_numberMember,tv_numberVoca;
        private  OnNoteListener onNoteListener;
        public CourseViewHolder(@NonNull View itemView, final OnNoteListener onNoteListener) {
            super(itemView);
            tv_courseName=itemView.findViewById(R.id.tv_courseName);
            tv_courseHost=itemView.findViewById((R.id.tv_courseHost));
            tv_numberMember=itemView.findViewById(R.id.tv_numMember);
            tv_numberVoca=itemView.findViewById(R.id.tv_numWord);
            //
            this.onNoteListener =onNoteListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoteListener !=null){
                        int position =getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION) {
                            onNoteListener.onNoteClick(position);
                        }
                    }
                }
            });
        }

    }
    public void removeItem(int position){
        mCoursesList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Course mCourse, int position){
        mCoursesList.add(position,mCourse);
        notifyItemInserted(position);
    }
    public ArrayList<Course>getData(){
        return mCoursesList;
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
    public void setOnClickListener(OnNoteListener onNoteListener){
        mOnNoteListener =onNoteListener;
    }

    @Override
    public Filter getFilter() {
        return courseFilter;
    }
    private Filter courseFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if(constraint ==null ||constraint.length()==0){
                mCoursesListFull=mCoursesList;
            }
            else {
                ArrayList<Course> filteredListCourse = new ArrayList<>();
//
                String filterPattern =constraint.toString().toLowerCase().trim();
                Log.v("filter",filterPattern);
                for (Course item : mCoursesList){
                    if(item.getTopicCourse() ==null){
                        if(item.getCourseName().toLowerCase().contains(filterPattern)
                                ||item.getNameHostCourse().toLowerCase().contains(filterPattern)){
                            filteredListCourse.add(item);
                        }
                    }
                    else if(item.getCourseName().toLowerCase().contains(filterPattern)
                        ||item.getNameHostCourse().toLowerCase().contains(filterPattern)
                        ||item.getTopicCourse().toLowerCase().contains(filterPattern)){
                        filteredListCourse.add(item);
                        Log.v("name",item.getCourseName());
                    }
                }
                mCoursesListFull = filteredListCourse;
            }
            FilterResults results =new FilterResults();
            results.values=mCoursesListFull;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //mCoursesListFull.clear();
//            mCoursesList.addAll((Collection<? extends Course>) results.values);
            if(results.values !=null){
                mCoursesListFull=((ArrayList<Course>) results.values);
                notifyDataSetChanged();
            }

        }
    };
}
