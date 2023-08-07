package com.example.myfirstapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.api.model.Token;
import com.example.myfirstapp.api.service.Api;
import com.example.myfirstapp.api.model.Message;
import com.example.myfirstapp.R;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private TextView txtLogin,txtLogin2,txtWarnNameS,txtWarnEmailS,txtWarnPassS,txtWarnPass2S ;

    private EditText edNameS,edEmailS,edPassS,edPass2S ;
    private Button btnSignUp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtLogin = findViewById(R.id.txtLogin);
        txtLogin2 = findViewById(R.id.txtLogin2);
        btnSignUp = findViewById(R.id.btnSignUp);
        edNameS = findViewById(R.id.edNameS);
        edEmailS = findViewById(R.id.edEmailS);
        edPassS = findViewById(R.id.edPassS);
        edPass2S = findViewById(R.id.edPass2S);

        txtWarnNameS = findViewById(R.id.txtWarnNameS);
        txtWarnEmailS = findViewById(R.id.txtWarnEmailS);
        txtWarnPassS = findViewById(R.id.txtWarnPassS);
        txtWarnPass2S = findViewById(R.id.txtWarnPass2S);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                CustomIntent.customType(SignUpActivity.this,"right-to-left");
            }
        });

        txtLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
               CustomIntent.customType(SignUpActivity.this,"right-to-left");
            }
        });


        // register
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edNameSt = edNameS.getText().toString();
                String edEmailSt = edEmailS.getText().toString();
                String edPassSt = edPassS.getText().toString();
                String edPassS2t = edPass2S.getText().toString();

                txtWarnNameS.setVisibility(view.GONE);
                txtWarnEmailS.setVisibility(view.GONE);
                txtWarnPassS.setVisibility(view.GONE);
                txtWarnPass2S.setVisibility(view.GONE);

                if(edNameSt.isEmpty() || edEmailSt.isEmpty() || edPassSt.isEmpty() || edPassS2t.isEmpty()) {
                    if(edNameSt.isEmpty()) {
                        txtWarnNameS.setVisibility(view.VISIBLE);
                    }
                    if(edEmailSt.isEmpty()) {
                        txtWarnEmailS.setVisibility(view.VISIBLE);
                    }
                    if(edPassSt.isEmpty()) {
                        txtWarnPassS.setVisibility(view.VISIBLE);
                    }
                    if(edPassS2t.isEmpty()) {
                        txtWarnPass2S.setVisibility(view.VISIBLE);
                    }
                    return;
                }

                // check password
                if(edPassSt.equals(edPassS2t)) {
                    createUser(edNameSt,edEmailSt,edPassSt);
                }
                else {
                    txtWarnPass2S.setText("Please make sure passwords match");
                    txtWarnPass2S.setVisibility(view.VISIBLE);
                    edPass2S.setText("");
                }
            }
        });

    }

    private void createUser(String edNameSt,String edEmailSt,String edPassSt) {
        Api.getClient().createUser(edNameSt,edEmailSt,edPassSt)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if(response.isSuccessful()) {
                            Token tk = response.body();
                            Toast.makeText(SignUpActivity.this, tk.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else  {
                            Message error =  Api.parserError(response);
                            if(response.code() == 400) {
                                txtWarnEmailS.setText(error.getMessage());
                                txtWarnEmailS.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {

                        Toast.makeText(SignUpActivity.this, "On Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}