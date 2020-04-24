package com.tptien.tstudy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tptien.tstudy.Service.APIRetrofitClient;
import com.tptien.tstudy.Service.APIService;
import com.tptien.tstudy.Service.DataService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

public class WelcomeActivity extends AppCompatActivity{
    private EditText edt_username,edt_password;
    private Button  btn_login,btn_signUp;
    Vibrator v;
    ProgressBar progressBar;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //
        edt_username =(EditText) findViewById(R.id.edt_username);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login =(Button)findViewById(R.id.btn_login);
        btn_signUp=(Button)findViewById(R.id.btn_signup);
        v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =new Intent(WelcomeActivity.this,RegistrationActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_out,R.anim.no_animation);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        //
        progressBar =(ProgressBar)findViewById(R.id.pbHeaderProgress);
        progressBar.setVisibility(View.GONE);



    }
    private void userLogin(){
        String username;
        String password;
        username =edt_username.getText().toString();
        password=edt_password.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_LONG).show();
        }
        else {
            btn_login.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            DataService dataService = APIService.getService();
            Call<User> callback= dataService.Login(username,password);
            callback.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body().getResponse().equals("ok")){
                        Toast.makeText(WelcomeActivity.this,"Đăng nhâp thành công.",Toast.LENGTH_LONG).show();
                        User user=response.body();
                        String nameDisplay=user.getDisplayName().toString();
                        Intent intent =new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putExtra("DisplayName", nameDisplay);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_up,R.anim.no_animation);
                        finish();
                        Log.v("bbb", nameDisplay);
                    }
                    if(response.body().getResponse().equals("failed")){
                        Toast.makeText(WelcomeActivity.this,"Thông tin tài khoản không chính xác!",Toast.LENGTH_LONG).show();
                    }
                    btn_login.setClickable(true);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(WelcomeActivity.this,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    btn_login.setClickable(true);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
