package com.tptien.tstudy.Fragment.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tptien.tstudy.Adapter.FriendAdapter;
import com.tptien.tstudy.R;
import com.tptien.tstudy.Service.APIService;
import com.tptien.tstudy.Service.DataService;
import com.tptien.tstudy.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogListFriend extends AppCompatDialogFragment {
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private ArrayList<User>listFriend =new ArrayList<>();
    private ArrayList<String> idFriendCheckedList =new ArrayList<>();
    private FriendAdapter mFriendAdapter;
    private ImageButton imageButton;
    private Button mButton;
    private DialogListFriendListener dialogListFriendListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_invite_friend,container,false);
        getDialog().setTitle("Bạn bè của bạn");
        mRecyclerView=(RecyclerView)v.findViewById(R.id.recyclerView_listFriend);
        searchView=(SearchView)v.findViewById(R.id.searchView_findFriend);
        mButton=(Button)v.findViewById(R.id.dialog_btn_invite);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListFriendListener.getList(idFriendCheckedList);
                dismiss();

            }
        });
        imageButton=(ImageButton)v.findViewById(R.id.dialog_close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Bundle bundle=getArguments();
        String idHost =bundle.getString("idHost");
        Log.d("idHost in dialog",idHost);

//        listFriend.add(new User("tiến","phuong"));
//        listFriend.add(new User("sơn","Sĩ phương"));
//        listFriend.add(new User("thịnh","Phước Tâm"));
//        listFriend.add(new User("hậu","thanh thịnh"));
//        listFriend.add(new User("sĩ","Hoàng Sơn"));
//        listFriend.add(new User("tâm","Phước Tiến"));


        searchView.setQueryHint("Search..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mFriendAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFriendAdapter.getFilter().filter(newText);
                return false;
            }
        });
        getDataListFriend(idHost);
        return v;
    }

    @SuppressLint("CheckResult")
    private void getDataListFriend(String idHost){
        DataService dataService=APIService.getService();
        Observable<List<User>> observable =dataService.getFriendCourse(idHost,"a");
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        if(users!=null){
                            listFriend = (ArrayList<User>) users;
                            Log.d("size ", String.valueOf(listFriend.size()));
                            mFriendAdapter = new FriendAdapter(listFriend, new FriendAdapter.OnItemCheckListener() {
                                @Override
                                public void ItemCheck(String id) {
                                    //listFriend.add(id);
                                    idFriendCheckedList.add(id);
                                    Log.d(" id size list", String.valueOf(idFriendCheckedList.size()));
                                }

                                @Override
                                public void ItemUnCheck(String id) {
                                    idFriendCheckedList.remove(id);
                                    Log.d(" id size list remove", String.valueOf(idFriendCheckedList.size()));
                                }
                            });
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            mRecyclerView.setAdapter(mFriendAdapter);

                        }
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListFriendListener= (DialogListFriendListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }

    }

    public interface DialogListFriendListener{
        void getList(ArrayList<String> listIDFriend);
    }
}
