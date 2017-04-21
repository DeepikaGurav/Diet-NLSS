package com.example.nickydcruz.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Update_wtht extends AppCompatActivity {
    SharedPreferences pref;
    String updatedwt;
    String updatedht;
    String updatedage;
    SharedPreferences.Editor editor;
    DietContract.DietEntry de;
    DBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wtht);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        de = new DietContract.DietEntry(pref.getString("username",""));
        myDb = new DBHelper(this,pref.getString("username",""));
        final EditText upwt = (EditText) findViewById(R.id.etupwt);
        final EditText upht = (EditText) findViewById(R.id.etupht);
        final EditText upage = (EditText) findViewById(R.id.etupage);
        final Button updatewt = (Button) findViewById(R.id.btupwt);
        Button updateht = (Button) findViewById(R.id.btupht);
        Button updateage = (Button) findViewById(R.id.btupage);


        String weight = pref.getString("weight","0");
        final String username = pref.getString("username","");

        //FOR UPDATING WEIGHT//

        updatewt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedwt = upwt.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Calendar cal = Calendar.getInstance();
                                editor.putString("weight",updatedwt+"");
                                editor.commit();
                                myDb.insertMeasureData(de.MeasureTable,pref.getString("weight",updatedwt),pref.getString("height","5.5"),cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH));
//                                for (int i = 0; i < key; i = i + 6)
//                                    Mydb.insertData(jsonResponse.getString(i + ""), jsonResponse.getString((i + 1) + ""), jsonResponse.getString((i + 2) + ""), jsonResponse.getString((i + 3) + ""), jsonResponse.getString((i + 4) + ""), jsonResponse.getString((i + 5) + ""));

                                Toast.makeText(Update_wtht.this, "Success", Toast.LENGTH_LONG).show();
//                                int i=1000;
                            } else {
                                Toast.makeText(Update_wtht.this, "Error", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Update_wtRequest update_wtRequest = new Update_wtRequest(username,updatedwt,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Update_wtht.this);
                queue.add(update_wtRequest);
            }
        });

        //FOR UPDATING HEIGHT//

        updateht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedht = upht.getText().toString();
                //Float b = 0.3048f * Float.parseFloat(a);
                //updatedht = Float.toString(b);

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Calendar cal = Calendar.getInstance();
                                editor.putString("height",updatedht+"");
                                editor.commit();
                                myDb.insertMeasureData(de.MeasureTable,pref.getString("weight","60"),pref.getString("height",updatedht),cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH));
//                                for (int i = 0; i < key; i = i + 6)
//                                    Mydb.insertData(jsonResponse.getString(i + ""), jsonResponse.getString((i + 1) + ""), jsonResponse.getString((i + 2) + ""), jsonResponse.getString((i + 3) + ""), jsonResponse.getString((i + 4) + ""), jsonResponse.getString((i + 5) + ""));
//                                for (int i = 0; i < key; i = i + 6)
//                                    Mydb.insertData(jsonResponse.getString(i + ""), jsonResponse.getString((i + 1) + ""), jsonResponse.getString((i + 2) + ""), jsonResponse.getString((i + 3) + ""), jsonResponse.getString((i + 4) + ""), jsonResponse.getString((i + 5) + ""));

                                Toast.makeText(Update_wtht.this, "Success", Toast.LENGTH_LONG).show();
//                                int i=1000;
                            } else {
                                Toast.makeText(Update_wtht.this, "Error", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Update_htRequest update_htRequest = new Update_htRequest(username,updatedht,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Update_wtht.this);
                queue.add(update_htRequest);

            }
        });

        updateage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedage = upage.getText().toString();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_homescreen_actions,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(),Homescreen.class);
        switch (item.getItemId()) {
            case R.id.foodcravings: i = new Intent(getApplicationContext(), FoodCravings.class);
                break;

            case R.id.superfood: i = new Intent(getApplicationContext(), superfood_main.class);
                break;

            case R.id.excal: i = new Intent(getApplicationContext(), ExerciseCalculator.class);
                break;

            case R.id.diet: i = new Intent(getApplicationContext(), Homescreen.class);
                break;

            case R.id.advanceSurvey: i = new Intent(getApplicationContext(), Advanced_Survey.class);
                break;

            case R.id.activity_update_wtht: i = new Intent(getApplicationContext(), Update_wtht.class);
                break;

            case R.id.Graphs: i = new Intent(getApplicationContext(), Graphs.class);
                break;

            case R.id.logout: {
                pref.edit().clear().commit();
                i = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            }

        }
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}
