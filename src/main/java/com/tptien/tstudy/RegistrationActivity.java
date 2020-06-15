package com.tptien.tstudy;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.tptien.tstudy.Service.APIService;
        import com.tptien.tstudy.Service.DataService;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    EditText edt_nameDisplay, edt_username, edt_password;
    Button btn_signUp, btn_login;
    ProgressBar pb_registerLoading;
    final static String FINAL_RESPONSE_OK ="ok";
    final static String FINAL_RESPONSE_EXIST ="exist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //
        edt_nameDisplay=(EditText)findViewById(R.id.edt_nameDisplay);
        edt_username=(EditText)findViewById(R.id.edt_username);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_signUp=(Button)findViewById(R.id.btn_signUp1);
        btn_login=(Button)findViewById(R.id.btn_login1);
        pb_registerLoading =(ProgressBar)findViewById(R.id.pbRegisterLoading);
        pb_registerLoading.setVisibility(View.GONE);
        //
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =new Intent(RegistrationActivity.this,WelcomeActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in,R.anim.no_animation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in,R.anim.no_animation);
    }
    public void userRegistration(){
        String name,password,displayName;
        name= edt_username.getText().toString();
        password=edt_password.getText().toString();
        displayName =edt_nameDisplay.getText().toString();
        if(name.isEmpty()|| password.isEmpty() || displayName.isEmpty()){
            Toast.makeText(this,R.string.pleaseTypeFull,Toast.LENGTH_LONG).show();
        }
        else {
            //
            btn_signUp.setClickable(false);
            pb_registerLoading.setVisibility(View.VISIBLE);
            //Call retrofit
            DataService dataService= APIService.getService();
            Call<User> call= dataService.Register(name,password,displayName);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body().getResponse().equals(FINAL_RESPONSE_OK)){
                        Toast.makeText(RegistrationActivity.this,R.string.registerSuccess,Toast.LENGTH_LONG).show();
                    }
                    else if(response.body().getResponse().equals(FINAL_RESPONSE_EXIST)){
                        Toast.makeText(RegistrationActivity.this,R.string.userExist,Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this,R.string.somethingWrong,Toast.LENGTH_LONG).show();
                    }
                    pb_registerLoading.setVisibility(View.GONE);
                    btn_signUp.setClickable(true);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegistrationActivity.this,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
            edt_username.setText("");
            edt_password.setText("");
            edt_nameDisplay.setText("");
        }

    }
}
