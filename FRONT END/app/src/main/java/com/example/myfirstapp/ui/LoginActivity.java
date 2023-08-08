package com.example.myfirstapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private static final String TAG = "LoginActivity";
    private TextView txtSignUp,txtSignUp2,txtWarnEmailL,txtWarnPassL ;

    private EditText edEmailL,edPassL ;
    private Button btnLogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            }
        });
        txtSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
               CustomIntent.customType(LoginActivity.this,"left-to-right");
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
                            intent.putExtra("name", user1.getName());
                            intent.putExtra("email", user1.getEmail());
                            intent.putExtra("token", authToken);
                            startActivity(intent);
                            CustomIntent.customType(LoginActivity.this, "left-to-right");
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


}