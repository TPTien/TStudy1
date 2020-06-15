package com.tptien.tstudy.Fragment.CourseFragment;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tptien.tstudy.Adapter.CourseAdapter;
import com.tptien.tstudy.Course;
import com.tptien.tstudy.CourseActivity;
import com.tptien.tstudy.R;
import com.tptien.tstudy.Service.APIService;
import com.tptien.tstudy.Service.DataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CreatedCoursesFragment extends Fragment implements CourseAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    private CourseAdapter mCourseAdapter ;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String idUser;
    private SharedPreferences mSharedPreferences;
    private ArrayList<Course> mCoursesList;
    private boolean isClicked =false;
    private FloatingActionButton mFloatingActionButton;
    private NestedScrollView nsv;
    CourseAdapter.OnNoteListener mOnNoteListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_course_created, container, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.reView_createdCourse);
        mCoursesList = new ArrayList<>();
        mCourseAdapter =new CourseAdapter(getActivity(),mCoursesList);

        //
        mFloatingActionButton=(FloatingActionButton) view.findViewById(R.id.fab_main_addNewCourse);
        nsv=(NestedScrollView)view.findViewById(R.id.nestsViewCreate);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_createdCourses);
        mSharedPreferences = this.getActivity().getSharedPreferences("loginAccount", MODE_PRIVATE);
        idUser = mSharedPreferences.getString("idUser", null);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataCreatedCourses(idUser);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        getDataCreatedCourses(idUser);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager= new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCourseAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCourseAdapter);


        //
        enableSwipeToDeleteUnDo(view);
        //


    }

    private void getDataCreatedCourses(final String idUser) {
        DataService dataService = APIService.getService();
        Call<List<Course>> call = dataService.getCreatedCourses(idUser);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                ArrayList<Course> mCoursesListResponse= (ArrayList<Course>) response.body();
                if(mCoursesListResponse.isEmpty()){
                    Toast.makeText(getActivity(),"Bạn chưa tạo phần !",Toast.LENGTH_LONG).show();
                }
                else {
                    mCoursesList.addAll(mCoursesListResponse);
//                    Log.v("bb", idUser);
//                    Log.v("bb", mCoursesList.get(0).getDateCreate());
//                    mCourseAdapter = new CourseAdapter(getActivity(), mCoursesList);
                    mCourseAdapter.notifyDataSetChanged();
//                    mCourseAdapter.setUpRecycleView(mRecyclerView,linearLayoutManager,mCourseAdapter);
//                    mCourseAdapter.setUpFABHideWhenScroll(mRecyclerView,mExtendedFloatingActionButton);
                    mCourseAdapter.setOnClickListener(new CourseAdapter.OnNoteListener() {
                        @Override
                        public void onNoteClick(int position) {
                            Intent intent=new Intent(getActivity(), CourseActivity.class);
                            startActivity(intent);
                        }
                    });
                }



            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void enableSwipeToDeleteUnDo(final View v) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                isClicked =false;
                final int position = viewHolder.getAdapterPosition();
                final Course course = mCourseAdapter.getData().get(position);
                final String idCourse=course.getIdCourse();
                mCourseAdapter.removeItem(position);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.container_coorlayout), "Bạn vừa xóa học phần khỏi danh sách", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isClicked=true;
                        mCourseAdapter.restoreItem(course, position);
                        //mRecyclerView.scrollToPosition(position);

                    }

                });

                final Handler handler =new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deleteCourse(isClicked ,idCourse);
                    }

                },4000);


                snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
                snackbar.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();


            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorGreen))
                        .addSwipeLeftLabel("Xóa")
                        .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                        .addSwipeRightBackgroundColor(getResources().getColor(R.color.colorGreen))
                        .addSwipeRightLabel("Xóa")
                        .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                        .addSwipeRightActionIcon(R.drawable.baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onNoteClick(int position) {

    }
    private void deleteCourse(Boolean isOkay, String idCourse){
        if(!isOkay)
        {
            DataService dataService = APIService.getService();
            Call<Void> call=dataService.deleteCourse(idCourse);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_course,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView)searchItem.getActionView();
        searchView.setMaxWidth(450);
        searchView.setQueryHint(getString(R.string.searchCourseCreate));
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
