package com.example.myfirstapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myfirstapp.R;
import com.example.myfirstapp.api.model.Message;
import com.example.myfirstapp.api.model.Token;
import com.example.myfirstapp.api.model.User;
import com.example.myfirstapp.api.service.Api;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    public static final String SHARED_PREFS = "shared_prefs";

    public static final String NAME_KEY = "name_key" ;

    public static final String EMAIL_KEY = "email_key";

    public static final String AUTH_TOKEN_KEY = "auth_token_key" ;

    private String name,email,authToken;

    SharedPreferences sharedPreferences ;

    private static final String TAG = "LoginActivity";
    private TextView txtSignUp,txtSignUp2,txtWarnEmailL,txtWarnPassL ;

    private EditText edEmailL,edPassL ;
    private Button btnLogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        name = sharedPreferences.getString(NAME_KEY,null);
        email = sharedPreferences.getString(EMAIL_KEY,null);
        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY,null);

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp2 = findViewById(R.id.txtSignUp2);
        btnLogin = findViewById(R.id.btnLogin);
        edEmailL = findViewById(R.id.edEmailL);
        edPassL = findViewById(R.id.edPassL);
        txtWarnEmailL = findViewById(R.id.txtWarnEmailL);
        txtWarnPassL = findViewById(R.id.txtWarnPassL);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                CustomIntent.customType(LoginActivity.this,"left-to-right");
                finish();
            }
        });
        txtSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
               CustomIntent.customType(LoginActivity.this,"left-to-right");
                finish();
            }
        });


        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edEmailLt = edEmailL.getText().toString();
                String edPassLt = edPassL.getText().toString();

                txtWarnEmailL.setVisibility(view.GONE);
                txtWarnPassL.setText("This Field is required");
                txtWarnPassL.setVisibility(view.GONE);
                if(edEmailLt.isEmpty() || edPassLt.isEmpty()) {
                    if(edEmailLt.isEmpty()) {
                        txtWarnEmailL.setVisibility(view.VISIBLE);
                    }
                    if(edPassLt.isEmpty()) {
                        txtWarnPassL.setVisibility(view.VISIBLE);
                    }
                    return ;
                }
               // login
               loginUser(edEmailLt,edPassLt);
            }
        });

    }

    private void loginUser(String edEmailLt,String edPassLt) {
        Api.getClient().loginUser(edEmailLt,edPassLt)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if(response.isSuccessful()) {
                            Token tk = response.body();
                            Toast.makeText(LoginActivity.this, tk.getMessage(), Toast.LENGTH_SHORT).show();
                            getUser("Bearer " + tk.getToken());
                        }
                        else  {
                            Message error =  Api.parserError(response);
                            if(response.code() == 400) {
                                txtWarnPassL.setText(error.getMessage());
                                txtWarnPassL.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "404 Not Found !!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void getUser(String authToken) {
        Api.getClient().getUser(authToken)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()) {
                            User user1 = response.body() ;
                            // Toast.makeText(LoginActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                            // show information
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

//                            intent.putExtra("name", user1.getName());
//                            intent.putExtra("email", user1.getEmail());
//                            intent.startActivity("token", authToken);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(NAME_KEY,user1.getName());
                            editor.putString(EMAIL_KEY,user1.getEmail());
                            editor.putString(AUTH_TOKEN_KEY,authToken);

                            editor.apply();

                            startActivity(intent);
//                          CustomIntent.customType(LoginActivity.this, "left-to-right");
                            finish();
                        }
                        else {
                            Message error = Api.parserError(response);
                            if(response.code() == 400) {
                                txtWarnPassL.setText(error.getMessage());
                                txtWarnPassL.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error Server !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
         if(authToken != null) {
           getUser(authToken);
         }
    }

}