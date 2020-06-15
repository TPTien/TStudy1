package com.tptien.tstudy;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.AppCompatImageButton;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.ItemTouchHelper;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.DocumentsContract;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.Toast;

        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.gson.Gson;
        import com.google.gson.JsonArray;
        import com.google.gson.JsonElement;
        import com.google.gson.JsonParser;
        import com.google.gson.reflect.TypeToken;
        import com.tptien.tstudy.Adapter.AddWordAdapter;
        import com.tptien.tstudy.Adapter.FriendAdapter;

        import com.tptien.tstudy.Fragment.DialogFragment.DialogListFriend;
        import com.tptien.tstudy.Service.APIService;
        import com.tptien.tstudy.Service.DataService;

        import org.json.JSONArray;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.List;

        import io.reactivex.Observable;
        import io.reactivex.Observer;
        import io.reactivex.android.schedulers.AndroidSchedulers;
        import io.reactivex.disposables.Disposable;
        import io.reactivex.schedulers.Schedulers;
        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class NewCourseActivity extends AppCompatActivity implements DialogListFriend.DialogListFriendListener {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private AddWordAdapter addWordAdapter;
    private FriendAdapter mFriendAdapter;
    private SharedPreferences mSharedPreferences;
    private String idHost;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<User> mFriendList =new ArrayList<>();
    private  ArrayList<String> mIdFriendListSelected;
    private ArrayList<Vocabulary> mVocalist =new ArrayList<>();
    private static final int SELECT_IMAGE=101;
    private FloatingActionButton fabCreateNewCoures;
    private TextInputLayout mCourseName,mCourseTopic;
    private Uri filePath =null;
    private int positionImage =-1;
    private String realPath ="";
    private String dirPath="https://tstudy.000webhostapp.com/TStudy/image/";
    private String fullPath ="";

    //private ImageButton imgWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        mToolbar =(Toolbar)findViewById(R.id.toolbar_newCourse);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView =(RecyclerView)findViewById(R.id.addCourse_recyclerViewFriends);
        //
        mSharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        idHost=mSharedPreferences.getString("idUser",null);
        Log.v("idHost",idHost);
        //
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //addWordAdapter=new AddWordAdapter(mVocalist);
        //getDataListFriend(idHost);
        //
        mIdFriendListSelected =new ArrayList<>();
        mCourseName =(TextInputLayout) findViewById(R.id.textInput_courseName);
        mCourseTopic =(TextInputLayout) findViewById(R.id.textInput_topic);
        fabCreateNewCoures =(FloatingActionButton)findViewById(R.id.newCourse_btn_createCourse);
        addWordAdapter=new AddWordAdapter(mVocalist,NewCourseActivity.this);
        mVocalist.add(new Vocabulary("","",null,null,null));
        mRecyclerView.setAdapter(addWordAdapter);
        fabCreateNewCoures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVocalist.size()>0){
                    int prePosition =mVocalist.size()-1;
                    Log.d("prePos", String.valueOf(prePosition));
                    if(mVocalist.get(prePosition).getMeaning().isEmpty() || mVocalist.get(prePosition).getWord().isEmpty()){
                        Toast.makeText(NewCourseActivity.this,R.string.pleaseTypeFull,Toast.LENGTH_SHORT).show();
                    }else {
                        mVocalist.add(new Vocabulary("","",null,null,""));
                        Log.d("error", String.valueOf(mVocalist.size()));
                        addWordAdapter.notifyDataSetChanged();
                        addWordAdapter.notifyItemInserted(mVocalist.size()-1);
                    }
                }else {
                        mVocalist.add(new Vocabulary("","",null,null,""));
                        Log.d("error", String.valueOf(mVocalist.size()));
                        addWordAdapter.notifyDataSetChanged();
                        addWordAdapter.notifyItemInserted(mVocalist.size()-1);
                }




            }
        });
        addWordAdapter.setOnClickListener(new AddWordAdapter.ImageOnClick() {
            @Override
            public void onImageClick(int position) {
                Intent intent = new Intent();
                positionImage =position;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_IMAGE);
            }
        });
        enableSwipeToDeleteAndUndo();
        //imgWord= (ImageButton)findViewById(R.id.img_word);





//        fabCreateNewCoures.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String courseName = mCourseName.getEditText().getText().toString();
//                String topic = mCourseTopic.getEditText().getText().toString();
//                Toast.makeText(getApplicationContext(),String.valueOf(mIdFriendListSelected.size()),Toast.LENGTH_LONG).show();
//                if(courseName.isEmpty()){
//                    Toast.makeText(getApplicationContext(),R.string.pleaseTypeFull,Toast.LENGTH_LONG).show();
//                }
//                else{
//                    //Toast.makeText(getApplicationContext(),courseName,Toast.LENGTH_LONG).show();
//                    DataService dataService=APIService.getService();
//                    Call<Void> call = dataService.inviteFriend(mIdFriendListSelected.toArray(),idHost,courseName,topic);
//                    call.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//
//                        }
//                    });
//                }
//
//            }
//        });



    }
//    private void getDataListFriend(final String idHost){
//        DataService dataService= APIService.getService();
//        Call<List<User>> callback=dataService.getFriendCourse(idHost,"a");
//        callback.enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                mFriendList =(ArrayList<User>) response.body();
//                //Log.v("123",mFriendList.get(0).getIdUser());
//                if (mFriendList==null){
//                    Toast.makeText(getApplicationContext(),"abc null",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    //Toast.makeText(getApplicationContext(),mFriendList.get(0).getDisplayName(),Toast.LENGTH_LONG).show();
//                    mFriendAdapter =new FriendAdapter(mFriendList, new FriendAdapter.OnItemCheckListener() {
//                        @Override
//                        public void ItemCheck(String id) {
//                            mIdFriendListSelected.add(id);
//
//                        }
//
//                        @Override
//                        public void ItemUnCheck(String id) {
//                            mIdFriendListSelected.remove(id);
//
//                        }
//                    });
//                    mRecyclerView.setAdapter(mFriendAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//
//            }
//        });
//    }
    public void enableSwipeToDeleteAndUndo(){
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Vocabulary item = addWordAdapter.getData().get(position);

                addWordAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container_createCourse), "Bạn vừa xóa thành công từ vựng.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addWordAdapter.restoreItem(item, position);
                        //mRecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }
    private void uploadVocabToServer(){
        ArrayList<String> word =new ArrayList<>();
        ArrayList<String>meaning = new ArrayList<>();
        ArrayList<String>fullPath = new ArrayList<>();
        String courseName = mCourseName.getEditText().getText().toString();
        String topic = mCourseTopic.getEditText().getText().toString();
        //list muitlpart for image file
        List<MultipartBody.Part> list = new ArrayList<>();
        for(int i=0; i<mVocalist.size();i++){
            mVocalist.get(i).setBitmapImage(null);
            mVocalist.get(i).setImageWord(null);

            word.add(mVocalist.get(i).getWord());
            meaning.add(mVocalist.get(i).getMeaning());
//            if(mVocalist.get(i).getFullPathFile() ==null){
//                fullPath.add("");
//            }
            fullPath.add(mVocalist.get(i).getFullPathFile());

            if(mVocalist.get(i).getRealPathFile() !=null){
                list.add(prepareFilePart("upload[]",mVocalist.get(i).getRealPathFile()));
            }
            mVocalist.get(i).setRealPathFile(null);

        }
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(mVocalist, new TypeToken<List<Vocabulary>>() {}.getType());

        if (! element.isJsonArray() ) {
            // fail appropriatelythrow new SomeException();
        }
        JsonArray jsonArray = element.getAsJsonArray();
        Log.d("json",jsonArray.toString());
        DataService dataService =APIService.getService();
//        Call<String>call =dataService.uploadImages(list,mVocalist);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response!=null){
//                    String message =response.body();
//                    Log.d("response upload",message);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("response upload fail",t.getMessage());
//            }
//        });
//        Call<JsonElement> call =dataService.uploadImages(list,mVocalist);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.d("response upload",response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Log.d("response failed",t.getMessage());
//            }
//        });
        Log.d("full path size", String.valueOf(fullPath.size()));
//        Call<String>call=dataService.createNewCourse(list,word,meaning,fullPath,mIdFriendListSelected);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                Log.d("response upload", response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("response failed",t.getMessage());
//            }
//        });
        Observable<String>observable =dataService.createNewCourse(list,word,meaning,fullPath,mIdFriendListSelected,courseName,topic,idHost);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("response new course",s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private MultipartBody.Part prepareFilePart(String partName,String filePath){
        File file=new File(filePath);
        String file_path =file.getAbsolutePath();
        RequestBody requestBody= RequestBody.create(MediaType.parse("multipart/form-data"),file);
        return MultipartBody.Part.createFormData(partName,file_path,requestBody);
    }
    public String getRealPathFromURI (Uri contentUri) {
//        String filePath = "";
//        String wholeID = DocumentsContract.getDocumentId(contentUri);
//
//        // Split at colon, use second item in the array
//        String id = wholeID.split(":")[1];
//
//        String[] column = { MediaStore.Images.Media.DATA };
//
//        // where id is equal to
//        String sel = MediaStore.Images.Media._ID + "=?";
//
//        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                column, sel, new String[]{ id }, null);
//
//        int columnIndex = cursor.getColumnIndex(column[0]);
//
//        if (cursor.moveToFirst()) {
//            filePath = cursor.getString(columnIndex);
//        }
//        cursor.close();
//        return filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==SELECT_IMAGE && resultCode ==RESULT_OK && data!=null&& data.getData()!=null){
            filePath=data.getData();
            realPath=getRealPathFromURI(filePath);
            //get image'name in path file
            String imageName = realPath.substring(realPath.lastIndexOf("/")+1);
            //get full path
            fullPath=dirPath.concat(imageName);

            Log.d("filepath",filePath.toString());
            Log.d("realpath",realPath);
            Log.d("full path",fullPath);
            try {
                InputStream inputStream =getContentResolver().openInputStream(filePath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                if(bitmap==null){
                    Log.e("bitmap","no");
                }
                else {
                    Log.d("bitmap",bitmap.toString());
                    //imgWord.setImageBitmap(bitmap);

                    Log.d("position", String.valueOf(positionImage));
                    mVocalist.get(positionImage).setBitmapImage(bitmap);
                    mVocalist.get(positionImage).setImageWord(filePath);
                    mVocalist.get(positionImage).setRealPathFile(realPath);
                    mVocalist.get(positionImage).setFullPathFile(fullPath);
                    addWordAdapter.notifyDataSetChanged();
                }

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_newcourse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.confirm_addCourse:
                Log.d("list size Vocab", String.valueOf(mVocalist.size()));
                uploadVocabToServer();
                break;
            case R.id.inviteFriend:
                Bundle bundle=new Bundle();
                bundle.putString("idHost",idHost);
                DialogListFriend dialogListFriend= new DialogListFriend();
                dialogListFriend.setArguments(bundle);
                dialogListFriend.show(getSupportFragmentManager(),"Friend Dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getList(ArrayList<String> listIDFriend) {
        mIdFriendListSelected=listIDFriend;
        Log.d("new course id list size", String.valueOf(mIdFriendListSelected.size()));
    }
}
