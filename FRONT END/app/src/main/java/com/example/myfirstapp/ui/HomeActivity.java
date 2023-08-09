package com.example.myfirstapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String NAME_KEY = "name_key" ;

    public static final String EMAIL_KEY = "email_key";

    public static final String AUTH_TOKEN_KEY = "auth_token_key" ;

    private String name,email,authToken , editNametxt , editEmailtxt;

    SharedPreferences sharedPreferences ;
    private TextView txtLogOut, txtNameH,txtEmailH ;
    private EditText editName, editEmail ;

    private Button editProfile , editSave ,editCancel ,editPassword;

    private ImageView iconEditName,iconEditEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(NAME_KEY,null);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        authToken = sharedPreferences.getString(AUTH_TOKEN_KEY,null);


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

        txtNameH.setText(name);
        txtEmailH.setText(email);

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
                        editName.setHint(name);

                        txtNameH.setText("");
                    }
                });

                iconEditEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editEmail.setVisibility(view.VISIBLE);
                        iconEditEmail.setVisibility(view.GONE);

                        editEmail.setHint(email);
                        editEmail.requestFocus();

                        txtEmailH.setText("");
                    }
                });

                editSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initialClick();

                     if(!(txtNameH.getText().equals(name) && txtEmailH.getText().equals(email))){
                         editNametxt = editName.getText().toString() ;
                         editEmailtxt =  editEmail.getText().toString();

                         if(editNametxt.equals("") && editEmailtxt.equals("") || editNametxt.equals(name) && editEmailtxt.equals(email)
                         || editNametxt.equals(name) && editEmailtxt.equals("") || editNametxt.equals("") && editEmailtxt.equals(email)) {
                             txtNameH.setText(name);
                             txtEmailH.setText(email);
                         }
                         else {
                             txtNameH.setText(editNametxt.equals("") ? name : editNametxt);
                             txtEmailH.setText(editEmailtxt.equals("") ? email : editEmailtxt);
                             name = txtNameH.getText().toString() ;
                             email = txtEmailH.getText().toString() ;
                             updateUser(authToken,editNametxt,editEmailtxt);
                         }

                     }
                    }
                });

                // if user choose cancel
                editCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtNameH.setText(name);
                        txtEmailH.setText(email);
                        initialClick();
                    }
                });
            }
        });
        // editPassword
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ChangePassword.class);
                // intent.putExtra("authToken",authToken);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(AUTH_TOKEN_KEY,null);
//                editor.apply();
                startActivity(intent);
                CustomIntent.customType(HomeActivity.this,"left-to-right");
                finish();
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
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        CustomIntent.customType(HomeActivity.this, "right-to-left");
                        finish();
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
        User user1 = new User(name,email, null);
        Api.getClient().updateUser(authToken,user1)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            Message m = response.body();
                            Toast.makeText(HomeActivity.this, m.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Message error = Api.parserError(response);
                                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "500 error !!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println(data);
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                authToken = data.getStringExtra("tokenback");
//                Toast.makeText(this, authToken, Toast.LENGTH_SHORT).show();
//            }
//            if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}