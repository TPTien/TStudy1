package com.tptien.tstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.tptien.tstudy.Adapter.ViewPagerAdapter;
import com.tptien.tstudy.Fragment.CourseFragment;
import com.tptien.tstudy.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    Toolbar mToolbar;
    TextView tv_nameDisplay;
    TabLayout tabLayout;
    ViewPager viewPager;
    private  int[] tabIcons ={
            R.drawable.outline_home_24,
            R.drawable.outline_course
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =(Toolbar)findViewById(R.id.nav_toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent =getIntent();
        String nameDisplay= intent.getStringExtra("DisplayName");

        NavigationView navigationView =(NavigationView)findViewById(R.id.nav_view);
        View headerView =navigationView.getHeaderView(0);
        //
        viewPager =(ViewPager)findViewById(R.id.viewPaper);
        setupViewPaper(viewPager);

        tabLayout =(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        tv_nameDisplay =(TextView) headerView.findViewById(R.id.tv_userName);
        Log.v("Result", nameDisplay);
        tv_nameDisplay.setText(nameDisplay);




    }
    private void setupViewPaper( ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment( new HomeFragment(),"Trang chủ");
        viewPagerAdapter.addFragment(new CourseFragment(),"Học phần");
        viewPager.setAdapter(viewPagerAdapter);
    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(0).setText("Trang chủ");
        tabLayout.getTabAt(1).setText("Học phần");
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);


//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tabs, null);
//        tabOne.setText("Trang chủ");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.outline_home_24, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne);
//        //
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tabs, null);
//        tabTwo.setText("Học phần");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.outline_course, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
