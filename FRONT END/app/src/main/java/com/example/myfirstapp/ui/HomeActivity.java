package com.example.myfirstapp.ui;

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

    private String editNametxt , editEmailtxt ,txtnameHt , txtemailHt;
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


        txtnameHt = getIntent().getStringExtra("name");
        txtemailHt = getIntent().getStringExtra("email");
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
                         editNametxt = editName.getText().toString() ;
                         editEmailtxt =  editEmail.getText().toString();

                         if(editNametxt.equals("") && editEmailtxt.equals("") || editNametxt.equals(txtnameHt) && editEmailtxt.equals(txtemailHt)
                         || editNametxt.equals(txtnameHt) && editEmailtxt.equals("") || editNametxt.equals("") && editEmailtxt.equals(txtemailHt)) {
                             txtNameH.setText(txtnameHt);
                             txtEmailH.setText(txtemailHt);
                         }
                         else {
                             txtNameH.setText(editNametxt.equals("") ? txtnameHt : editNametxt);
                             txtEmailH.setText(editEmailtxt.equals("") ? txtemailHt : editEmailtxt);
                             txtnameHt = txtNameH.getText().toString() ;
                             txtemailHt = txtEmailH.getText().toString() ;
                             updateUser(authToken,editNametxt,editEmailtxt);
                         }

                     }
                    }
                });

                // if user choose cancel
                editCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtNameH.setText(txtnameHt);
                        txtEmailH.setText(txtemailHt);
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
                intent.putExtra("authToken",authToken);
//                startActivityForResult(intent,1);
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