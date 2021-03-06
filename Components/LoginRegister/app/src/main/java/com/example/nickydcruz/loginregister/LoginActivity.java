package com.example.nickydcruz.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox chRem ;
    DBaseHelper Mydb;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean checkFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mydb = new DBaseHelper(getApplicationContext());


        Response.Listener<String> responseListenerc = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    int key = jsonResponse.getInt("key");
                    if (success) {
                        for (int i = 0; i < key; i = i + 6)
                            Mydb.insertData(jsonResponse.getString(i + ""), jsonResponse.getString((i + 1) + ""), jsonResponse.getString((i + 2) + ""), jsonResponse.getString((i + 3) + ""), jsonResponse.getString((i + 4) + ""), jsonResponse.getString((i + 5) + ""));
//
//                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                        int i=1000;
                    } else {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        CustomRequest customRequest = new CustomRequest(responseListenerc);
        RequestQueue queuec = Volley.newRequestQueue(LoginActivity.this);
        queuec.add(customRequest);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword =(EditText) findViewById(R.id.etPassword);

        final Button btLogin = (Button) findViewById(R.id.btLogin);

        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterhere);
        chRem =(CheckBox) findViewById(R.id.chrem);
        checkFlag = chRem.isChecked();


        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);


        editor = pref.edit();

        chRem.setOnCheckedChangeListener(this);

        //Calling SharedPref variables
        final String pUsername=pref.getString("username","");
        final String pPassword=pref.getString("password","");
        final String pCF = pref.getString("cf","0");

        //Checking users last session
        if(!((pUsername.equals("") && pPassword.equals("") )|| pCF.equals("0"))){
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            String dob = jsonResponse.getString("dob");
                            String basicDone = jsonResponse.getString("basicDone");
                            int age = new FormulaClass().calculateAge(dob);
                            editor.putInt("age",age);
                            editor.commit();

                            if(basicDone.equals("1")) {
                                Intent intent;
                                Calendar cal = Calendar.getInstance();
                                if(!(pref.getString("SystemDate",0+"").equals(cal.get(Calendar.YEAR) + "-"
                                        + (cal.get(Calendar.MONTH)+1)
                                        + "-" + cal.get(Calendar.DAY_OF_MONTH)))){
                                     intent = new Intent(LoginActivity.this, ResultPage.class);
                                    editor.commit();
                                }
                                else {
                                     intent = new Intent(LoginActivity.this, ResultPage.class);
                                }
                                intent.putExtra("dob", dob);

                                String height = jsonResponse.getString("height");
                                String weight = jsonResponse.getString("weight");
                                String gender = jsonResponse.getString("gender");
                                String type = jsonResponse.getString("type");

                                editor.putString("height",height);
                                editor.putString("weight",weight);
                                editor.putString("gender",gender);
                                editor.putString("type",type);

                                SharedPreferences prefere = getSharedPreferences(pUsername+"update.conf",Context.MODE_PRIVATE);
                                if(prefere.getInt("dayoftheyear",0 ) == 0){
                                    Calendar cal123 = Calendar.getInstance();
                                    int dayoftheyear = cal123.get(Calendar.DAY_OF_YEAR);
                                    prefere.edit().putInt("dayoftheyear",dayoftheyear).commit();
                                }

                                editor.commit();

                                LoginActivity.this.startActivity(intent);
                                //LoginActivity.this.finish();

                            }
                            else {

                                Intent intent1   =new Intent(LoginActivity.this, BasicSurvey1.class);
                                intent1.putExtra("dob", dob);
                                LoginActivity.this.startActivity(intent1);
                                //LoginActivity.this.finish();

                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Login Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };


            LoginRequest loginRequest = new LoginRequest(pUsername,pPassword,responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this,Register.class);
                LoginActivity.this.startActivity(registerIntent);
                LoginActivity.this.finish();

            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username =etUsername.getText().toString();
                final String password =etPassword.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                String dob = jsonResponse.getString("dob");
                                String basicDone = jsonResponse.getString("basicDone");
                                int age = new FormulaClass().calculateAge(dob);


                                //to check if user clicked remember me
                                if(checkFlag){
                                    //Setting Shared Preferences to maintain session
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.putInt("age",age);
                                    editor.putString("cf","1");

                                    editor.apply();
                                }
                                else
                                {
                                    //Setting Shared Preferences
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.putInt("age",age);
                                    //Ensuring that session is not maintained
                                    editor.putString("cf","0");
                                    editor.apply();
                                }

                                //To check if basic survey already done
                                if(basicDone.equals("1")) {
                                    Intent intent;
                                    Calendar cal = Calendar.getInstance();
                                    if(!(pref.getString("SystemDate",0+"").equals(cal.get(Calendar.YEAR) + "-"
                                            + (cal.get(Calendar.MONTH)+1)
                                            + "-" + cal.get(Calendar.DAY_OF_MONTH)))){
                                        intent = new Intent(LoginActivity.this, ResultPage.class);
                                        editor.commit();
                                    }
                                    else {
                                        intent = new Intent(LoginActivity.this, ResultPage.class);
                                    }
                                    intent.putExtra("username", username);
                                    intent.putExtra("dob", dob);
                                    String height = jsonResponse.getString("height");
                                    String weight = jsonResponse.getString("weight");
                                    String gender = jsonResponse.getString("gender");
                                    String type = jsonResponse.getString("type");
                                    editor.putString("height",height);
                                    editor.putString("weight",weight);
                                    editor.putString("gender",gender);
                                    editor.putString("type",type);

                                    SharedPreferences prefere = getSharedPreferences(username+"update.conf",Context.MODE_PRIVATE);
                                    if(prefere.getInt("dayoftheyear",0 ) == 0){
                                        Calendar cal123 = Calendar.getInstance();
                                        int dayoftheyear = cal123.get(Calendar.DAY_OF_YEAR);
                                        prefere.edit().putInt("dayoftheyear",dayoftheyear).commit();
                                    }
                                    editor.apply();
                                    LoginActivity.this.startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                                else {
                                    Intent intent1   =new Intent(LoginActivity.this, BasicSurvey1.class);
                                    intent1.putExtra("username", username);
                                    intent1.putExtra("dob", dob);
                                    LoginActivity.this.startActivity(intent1);
                                    LoginActivity.this.finish();

                                }
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                LoginRequest loginRequest = new LoginRequest(username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkFlag = isChecked;
    }
}

