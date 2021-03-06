package com.tptien.tstudy.Fragment.CourseFragment.AllCourseFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tptien.tstudy.Adapter.CourseAdapter;
import com.tptien.tstudy.Course;
import com.tptien.tstudy.CourseActivity;
import com.tptien.tstudy.R;
import com.tptien.tstudy.Service.APIService;
import com.tptien.tstudy.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoursesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private  CourseAdapter mCourseAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Course> mCoursesList= new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_course_all, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView =view.findViewById(R.id.reView_allCourse);
//        mProgressBar=getActivity().findViewById(R.id.main_progress);
//        mProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swipe_allCourses);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataAllCourses();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        getDataAllCourses();

    }
    private void getDataAllCourses() {
        DataService dataService = APIService.getService();
        Call<List<Course>> call = dataService.getAllCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                mCoursesList=(ArrayList<Course>) response.body();
                Log.v("bb",mCoursesList.get(0).getCourseName());
                mCourseAdapter= new CourseAdapter(getActivity(),mCoursesList);
                mCourseAdapter.setUpRecycleView(mRecyclerView,mLayoutManager,mCourseAdapter);
                mCourseAdapter.setOnClickListener(new CourseAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent=new Intent(getActivity(),CourseActivity.class);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_course,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView)searchItem.getActionView();
        searchView.setMaxWidth(450);
        searchView.setQueryHint(getString(R.string.searchCourse));
        searchView.setIconified(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCourseAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCourseAdapter.getFilter().filter(newText);
                return  false;
            }
        });

    }
}
