package com.example.turnierplaner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*



class Register : AppCompatActivity() {

    lateinit var mName: EditText;
    lateinit var mEmail: EditText;
    lateinit var mPassword: EditText;
    lateinit var mRegisterButton: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mName                = findViewById(R.id.Name);
        mEmail               = findViewById(R.id.Email);
        mPassword            = findViewById(R.id.Password);
        mRegisterButton      = findViewById(R.id.registerBtn);



        mRegisterButton.setOnClickListener(object: View.OnClickListener {
            @Override
            public override fun onClick(v: View) {
                var email: String = mEmail.text.toString().trim();
                var password: String = mPassword.text.toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;

                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;

                }

                if (password.length < 8){
                    mPassword.setError("Password Must be at least 8 Characters")
                    return;
                }


            }
        });






    }



}