package com.tptien.tstudy.Fragment.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tptien.tstudy.Adapter.CourseAdapter;
import com.tptien.tstudy.Course;
import com.tptien.tstudy.CourseActivity;
import com.tptien.tstudy.R;
import com.tptien.tstudy.Service.APIRetrofitClient;
import com.tptien.tstudy.Service.APIService;
import com.tptien.tstudy.Service.DataService;
import com.tptien.tstudy.ViewPagerCards.CardItem;
//import com.tptien.tstudy.ViewPagerCards.SliderAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements CourseAdapter.OnNoteListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager mlLayoutManager;
    //private SliderAdapter sliderAdapter;
    private CourseAdapter mCourseAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Handler sliderHandler =new Handler();
    CourseAdapter.OnNoteListener mOnNoteListener;
    private String idUser;
    private SharedPreferences mSharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);

//        viewPager2 =(ViewPager2)v.findViewById(R.id.viewPagerCard);
//        final List<CardItem> cardItems =new ArrayList<>();
//        cardItems.add(new CardItem(R.drawable.ic_course,"test","kiem tra"));
//        cardItems.add(new CardItem(R.drawable.ic_password,"test","kiem tra"));
//        cardItems.add(new CardItem(R.drawable.ic_profile,"test","day la doang test ngan de test do hien thi cua cardView"));
//        //
//        sliderAdapter=new SliderAdapter(cardItems,viewPager2);
//        viewPager2.setAdapter(sliderAdapter);
//
//        viewPager2.setClipToPadding(false);
//        viewPager2.setClipChildren(false);
//        viewPager2.setOffscreenPageLimit(3);
//
//        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//        CompositePageTransformer compositePageTransformer= new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r =1 -Math.abs(position);
//                page.setScaleY(0.85f +r * 0.15f);
//                float myOffset = position * -(2 * 30 + 30);
//                if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    if (ViewCompat.getLayoutDirection(viewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                        page.setTranslationX(-myOffset);
//                    } else {
//                        page.setTranslationX(myOffset);
//                    }
//                } else {
//                    page.setTranslationY(myOffset);
//                }
//
//            }
//        });
//        viewPager2.setPageTransformer(compositePageTransformer);
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                sliderHandler.removeCallbacks(runnable);
//                sliderHandler.postDelayed(runnable,10000);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//            }
//        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView =view.findViewById(R.id.reView_recentCourse);
        recyclerView.setHasFixedSize(true);
        //
        mSharedPreferences = this.getActivity().getSharedPreferences("loginAccount", MODE_PRIVATE);
        idUser = mSharedPreferences.getString("idUser", null);
        //

        getDataRecentCourses(idUser);
        mSwipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.swipe_recentCourse);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataRecentCourses(idUser);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDataRecentCourses(String idUser){
        DataService dataService = APIService.getService();
        Call<List<Course>> call= dataService.getRecentCourses(idUser);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                ArrayList<Course> mCoursesList=(ArrayList<Course>)response.body();
                Log.v("bb",mCoursesList.get(0).getCourseName());
                mCourseAdapter= new CourseAdapter(getActivity(),mCoursesList);
                mCourseAdapter.setUpRecycleView(recyclerView,mlLayoutManager,mCourseAdapter);
                mCourseAdapter.setOnClickListener(new CourseAdapter.OnNoteListener() {
                    @Override
                    public void onNoteClick(int position) {
                        Intent intent=new Intent(getActivity(), CourseActivity.class);
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
    public void onNoteClick(int position) {

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    //    private  Runnable runnable= new Runnable() {
//        @Override
//        public void run() {
//            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        sliderHandler.removeCallbacks(runnable);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        sliderHandler.postDelayed(runnable,10000);
//    }

}
