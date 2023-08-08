package com.example.myfirstapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.api.model.Message;
import com.example.myfirstapp.api.model.User;
import com.example.myfirstapp.api.service.Api;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    private String authToken ;
    private TextView txt2P,txt3P,txtWarnP ;
    private EditText edt1P,edt2P,edt3P ;

    private Button btnP ;

    private ImageView btnbackP ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        View password1 = findViewById(R.id.password1);
        View password2 = findViewById(R.id.password2);
        View password3 = findViewById(R.id.password3);
        txt2P = (TextView) password2.findViewById(R.id.textPasswordP);
        txt3P = (TextView) password3.findViewById(R.id.textPasswordP);
        txtWarnP = findViewById(R.id.txtWarnP);

        btnP = findViewById(R.id.btnP);
        btnbackP = findViewById(R.id.btnbackP);

        edt1P = (EditText) password1.findViewById(R.id.editTextPasswordP);
        edt2P = (EditText) password2.findViewById(R.id.editTextPasswordP);
        edt3P = (EditText) password3.findViewById(R.id.editTextPasswordP);

        txt2P.setText("New Password");
        txt3P.setText("Current New Password");

        intialClick(edt1P);
        intialClick(edt2P);
        intialClick(edt3P);

        authToken = getIntent().getStringExtra("authToken");

        // warn
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtWarnP.setText("");
                String txtedt1P = edt1P.getText().toString().trim() ;
                String txtedt2P = edt2P.getText().toString().trim() ;
                String txtedt3P = edt3P.getText().toString().trim() ;

                if((txtedt1P.isEmpty() && txtedt2P.isEmpty() && txtedt3P.isEmpty())||
                        (txtedt2P.isEmpty() && txtedt3P.isEmpty())) {
                    txtWarnP.setText("Please fill out all fields !!!");
                    return;
                }
                if(!txtedt2P.equals(txtedt3P)) {
                    txtWarnP.setText("Passwords are not the same !!!");
                    return;
                }
                checkPass(txtedt1P,txtedt2P);

            }
        });

        btnbackP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("tokenback", authToken );
//                setResult(RESULT_OK, resultIntent);
//                CustomIntent.customType(ChangePassword.this, "right-to-left");
                finish();

            }
        });


    }


    private void intialClick(EditText edtP) {
        edtP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    edtP.setText("");
                }
            }
        });
    }

    private void checkPass(String txtedt1P,String txtedt2P) {
        Api.getClient().checkPass(authToken,txtedt1P)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(ChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            updatePassword(txtedt2P);
                        }
                        else {
                            Message error =  Api.parserError(response);
                            if(response.code() == 400) {
                               txtWarnP.setText(error.getMessage());
                            }
                            else {
                                Toast.makeText(ChangePassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(ChangePassword.this, "On Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassword(String txtedt2P) {
        User user1 = new User(null,null, txtedt2P);
        Api.getClient().updateUser(authToken,user1)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            txtWarnP.setText("");
                            Message m = response.body();
                            Toast.makeText(ChangePassword.this, m.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Message error = Api.parserError(response);
                            Toast.makeText(ChangePassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(ChangePassword.this, "500 error !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}