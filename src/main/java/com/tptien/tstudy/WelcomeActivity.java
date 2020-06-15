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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.tptien.tstudy.Fragment.CourseFragment.AllCourseFragment.JoinedCoursesFragment;
import com.tptien.tstudy.Fragment.CourseFragment.CreatedCoursesFragment;
import com.tptien.tstudy.Fragment.CourseFragment.SuggestedCourseFragment;
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
    TextInputLayout textInput_Username, textInputPassword;
    Vibrator v;
    ProgressBar progressBar;
    private  SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox saveLoginCheckbox;
    private Boolean isSaveAccount;
    final String FINAL_RESPONSE_OK ="ok";
    final String FINAL_RESPONSE_FAILED ="failed";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //
        edt_username =(EditText) findViewById(R.id.edt_username);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login =(Button)findViewById(R.id.btn_login);
        saveLoginCheckbox=(CheckBox)findViewById(R.id.checkbox_login);
        //
        textInput_Username=(TextInputLayout)findViewById(R.id.text_input_username);
        textInputPassword=(TextInputLayout)findViewById(R.id.text_input_password);
        //
        sharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        editor =sharedPreferences.edit();
        //
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
        isSaveAccount=sharedPreferences.getBoolean("saveAccount",true);
        if(isSaveAccount){
            edt_username.setText(sharedPreferences.getString("username",null));
            edt_password.setText(sharedPreferences.getString("password",null));
            saveLoginCheckbox.setChecked(true);
        }
        //
        progressBar =(ProgressBar)findViewById(R.id.pbHeaderProgress);
        progressBar.setVisibility(View.GONE);



    }
    private boolean validateUsername(){
        String usernameInput=textInput_Username.getEditText().getText().toString().trim();
        if(usernameInput.isEmpty()){
            textInput_Username.setError("Không thể bỏ trống!");
            return false;
        }
//        else if(usernameInput.length()>20){
//            textInput_Username.setError("Tên quá dài");
//            return false;
//        }
        else {
            textInput_Username.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String passwordInput=textInputPassword.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            textInputPassword.setError("Không thể bỏ trống!");
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }
    private void userLogin(){
        String username;
        String password;
        username =edt_username.getText().toString();
        password=edt_password.getText().toString();
        if(!validateUsername() | !validatePassword()){
            Toast.makeText(this,R.string.pleaseTypeFull,Toast.LENGTH_LONG).show();
            return;
        }
        else {

            btn_login.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            DataService dataService = APIService.getService();
            Call<User> callback= dataService.Login(username,password);
            callback.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body().getResponse().equals(FINAL_RESPONSE_OK)){
                        Toast.makeText(WelcomeActivity.this,R.string.loginSuccess,Toast.LENGTH_LONG).show();
                        User user=response.body();
                        String nameDisplay=user.getDisplayName().toString();
                        String idUser =user.getIdUser().toString();
                        String username=user.getUserName();
                        String password=user.getPassword();
                        String role=user.getRole();
                        //
                        if(saveLoginCheckbox.isChecked()){
                            editor.putBoolean("saveAccount",true);
                            editor.putString("userNameDisplay",nameDisplay);
                            editor.putString("idUser",idUser);
                            editor.putString("username",username);
                            editor.putString("password",password);
                            editor.putString("role",role);
                            editor.commit();

                        }
                        else {
                            editor.putBoolean("saveAccount",false);
                            editor.apply();
                        }
                        //send data to main activity
                        Intent intent =new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putExtra("DisplayName", nameDisplay);
                        intent.putExtra("idUser",idUser);

                        startActivity(intent);

                        overridePendingTransition(R.anim.anim_slide_up,R.anim.no_animation);
                        finish();
                        Log.v("bbb", nameDisplay);
                        Log.v("id",idUser);
                    }
                    if(response.body().getResponse().equals(FINAL_RESPONSE_FAILED)){
                        Toast.makeText(WelcomeActivity.this,R.string.incorrectAccount,Toast.LENGTH_LONG).show();
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
