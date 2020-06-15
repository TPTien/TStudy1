package com.tptien.tstudy.Fragment.CourseFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tptien.tstudy.Adapter.CustomViewPager;
import com.tptien.tstudy.Adapter.ViewPagerAdapter;
import com.tptien.tstudy.Fragment.CourseFragment.AllCourseFragment.AllCoursesFragment;
import com.tptien.tstudy.Fragment.CourseFragment.AllCourseFragment.JoinedCoursesFragment;
import com.tptien.tstudy.R;

public class CourseFragment extends Fragment {
    private TabLayout mtabTabLayoutCourse;
    private CustomViewPager mViewPagerCourse;
    private AllCoursesFragment allCoursesFragment;
    private CreatedCoursesFragment createdCoursesFragment;
    private JoinedCoursesFragment joinedCoursesFragment;
    private SuggestedCourseFragment suggestedCourseFragment;
    private ExtendedFloatingActionButton mExtendedFloatingActionButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_course, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPagerCourse =(CustomViewPager) view.findViewById(R.id.viewpager_course);
        mtabTabLayoutCourse=(TabLayout)view.findViewById(R.id.tabLayout_course);
        allCoursesFragment = new AllCoursesFragment();
        suggestedCourseFragment= new SuggestedCourseFragment();
        createdCoursesFragment= new CreatedCoursesFragment();
        joinedCoursesFragment= new JoinedCoursesFragment();
        mtabTabLayoutCourse.setupWithViewPager(mViewPagerCourse);

        ViewPagerAdapter mvViewPagerAdapter =new ViewPagerAdapter(getChildFragmentManager(),0);
        mvViewPagerAdapter.addFragment(allCoursesFragment,"Tất cả");
        mvViewPagerAdapter.addFragment(createdCoursesFragment,"Đã tạo");
        mvViewPagerAdapter.addFragment(joinedCoursesFragment,"Đã tham gia");
        mvViewPagerAdapter.addFragment(suggestedCourseFragment,"Đề xuất");
        mViewPagerCourse.setAdapter(mvViewPagerAdapter);
        mViewPagerCourse.setSwipeable(false);
        mViewPagerCourse.setOffscreenPageLimit(3);
        //



    }

}
