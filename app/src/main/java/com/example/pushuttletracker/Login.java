package com.example.pushuttletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Login extends AppCompatActivity {
     EditText e1,e2;
     Button b1;
     //Spinner sp;
     DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.editText);
        e2 = (EditText)findViewById(R.id.editText2);
        //sp =(Spinner) findViewById(R.id.spinner);
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.SelectUserType,R.layout.support_simple_spinner_dropdown_item);
        //sp.setAdapter(adapter);

        b1= (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean Chkemailpass= db.emailpassword(email,password);
                //String item = sp.getSelectedItem().toString();

                if(Chkemailpass==true) {
                    Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this,HomePage.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Wrong email or password",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
