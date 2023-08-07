package com.example.myfirstapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private String authToken ;
    private TextView txtLogOut, txtNameH,txtEmailH ;
    private EditText editName, editEmail ;

    private Button editProfile , editSave ,editCancel ,editPassword;

    private ImageView iconEditName,iconEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtLogOut = findViewById(R.id.txtLogOut);
        txtNameH = findViewById(R.id.txtNameH);
        txtEmailH = findViewById(R.id.txtEmailH);
        editPassword = findViewById(R.id.editPassword);
        editProfile = findViewById(R.id.editProfile);
        editSave = findViewById(R.id.editSave);
        editCancel = findViewById(R.id.editCancel);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        iconEditName = findViewById(R.id.iconEditName);
        iconEditEmail = findViewById(R.id.iconEditEmail);


        String txtnameHt = getIntent().getStringExtra("name");
        String txtemailHt = getIntent().getStringExtra("email");
        authToken = getIntent().getStringExtra("token");
        txtNameH.setText(txtnameHt);
        txtEmailH.setText(txtemailHt);

        // editprofile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconEditName.setVisibility(view.VISIBLE);
                iconEditEmail.setVisibility(view.VISIBLE);
                editSave.setVisibility(view.VISIBLE);
                editCancel.setVisibility(view.VISIBLE);

                editProfile.setVisibility(view.GONE);



                iconEditName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editName.setVisibility(view.VISIBLE);
                        iconEditName.setVisibility(view.GONE);

                        editName.requestFocus();
                        editName.setHint(txtnameHt);

                        txtNameH.setText("");
                    }
                });

                iconEditEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editEmail.setVisibility(view.VISIBLE);
                        iconEditEmail.setVisibility(view.GONE);

                        editEmail.setHint(txtemailHt);
                        editEmail.requestFocus();

                        txtEmailH.setText("");
                    }
                });

                editSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initialClick();

                     if(!(txtNameH.getText().equals(txtnameHt) && txtEmailH.getText().equals(txtemailHt))){
                         String editNametxt = editName.getText().toString() ;
                         String editEmailtxt =  editEmail.getText().toString();

                         if(editNametxt.equals("") && editEmailtxt.equals("") || editNametxt.equals(txtnameHt) && editEmailtxt.equals(txtemailHt)
                         || editNametxt.equals(txtnameHt) && editEmailtxt.equals("") || editNametxt.equals("") && editEmailtxt.equals(txtemailHt)) {
                             txtNameH.setText(txtnameHt);
                             txtEmailH.setText(txtemailHt);
                         }
                         else {
                             txtNameH.setText(editNametxt.equals("") ? txtnameHt : editNametxt);
                             txtEmailH.setText(editEmailtxt.equals("") ? txtemailHt : editEmailtxt);

                             updateUser(authToken,editNametxt,editEmailtxt);
                         }

                     }
                    }
                });

                // if user choose cancel
                editCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initialClick();

                        txtNameH.setText(txtnameHt);
                        txtEmailH.setText(txtemailHt);

                    }
                });
            }
        });

        // editPassword
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ChangePassword.class);
                intent.putExtra("token",authToken);
                startActivity(intent);
                CustomIntent.customType(HomeActivity.this,"left-to-right");
            }
        });
        // logout
        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser(authToken);
            }
        });


    }

    private void initialClick() {
        editProfile.setVisibility(View.VISIBLE);
        editSave.setVisibility(View.GONE);
        editCancel.setVisibility(View.GONE);
        editName.setVisibility(View.GONE);
        editEmail.setVisibility(View.GONE);
        iconEditName.setVisibility(View.GONE);
        iconEditEmail.setVisibility(View.GONE);
    }
    private void logoutUser(String authToken) {
        Api.getClient().logoutUser(authToken)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Message m = response.body();
                        Toast.makeText(HomeActivity.this, m.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        CustomIntent.customType(HomeActivity.this, "right-to-left");
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "500 error !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUser(String authToken,String name,String email ) {
        if(name.equals("")) name = null;
        if(email.equals("")) email = null;
        User user1 = new User(name,email,null);
        Api.getClient().updateUser(authToken,user1)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Message m = response.body();
                        Toast.makeText(HomeActivity.this, m.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "500 error !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}