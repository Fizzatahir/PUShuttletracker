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

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
     EditText e1,e2,e3;
     Button b1,b2;
     Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        e1 =(EditText)findViewById(R.id.email);
        e2 = (EditText)findViewById(R.id.pass);
        e3 =(EditText) findViewById(R.id.cpass);
        sp =(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.SelectUserType,R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        b1 =(Button)findViewById(R.id.register);
        b2 =(Button)findViewById(R.id.login);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                String item = sp.getSelectedItem().toString();

                if(s1.equals("") || s2.equals("") || s3.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (s2.equals(s3))
                    {
                        Boolean chkemail = db.chkemail(s1);
                        if(chkemail==true)
                        {
                            Boolean insert = db.insert(s1,s2);
                            if(insert==true)
                            {
                                Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                            }


                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Email Already exists",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }
}
