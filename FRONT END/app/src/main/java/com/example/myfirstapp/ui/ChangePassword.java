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

public class ChangePassword extends AppCompatActivity {

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

        String authToken = getIntent().getStringExtra("token");
        Toast.makeText(this, authToken, Toast.LENGTH_SHORT).show();
        // warn
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtedt1P = edt1P.getText().toString() ;
                String txtedt2P = edt2P.getText().toString() ;
                String txtedt3P = edt3P.getText().toString() ;

                if((txtedt1P.isEmpty() && txtedt2P.isEmpty() && txtedt3P.isEmpty())||
                        (txtedt2P.isEmpty() && txtedt3P.isEmpty())) {
                    txtWarnP.setText("Please fill out all fields !!!");
                }
                if(!txtedt2P.equals(txtedt3P)) {
                    txtWarnP.setText("Passwords are not the same !!!");
                }
            }
        });

        btnbackP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("authToken",authToken );
                setResult(ChangePassword.RESULT_OK, resultIntent);
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
}