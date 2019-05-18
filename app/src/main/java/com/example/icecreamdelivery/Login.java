package com.example.icecreamdelivery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private RequestQueue loginQueue;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        View backgroundImage = findViewById(R.id.loginBackground);
        Drawable loginBackground = backgroundImage.getBackground();
        loginBackground.setAlpha(70);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        loginQueue = Volley.newRequestQueue(Login.this);

    }

    public void onLoginClick(View view){

        if (!TextUtils.isEmpty(edtUsername.getText())){
            if (!TextUtils.isEmpty(edtPassword.getText())){

                if(haveNet()){
                    jsonParse();
                }else if(!haveNet()){
                    Toast.makeText(Login.this,"Network connectin is not available",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Login.this,"Enter Password", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(Login.this,"Enter Username", Toast.LENGTH_LONG).show();
        }

    }

    private void jsonParse(){
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "http://icd.infinisolutionslk.com/checkLoginJSON.php?uName="+edtUsername.getText()+"&uPass="+edtPassword.getText();

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            Integer status = response.getInt("satus");
                            String message = response.getString("Messege");
                            Integer vehicleId = response.getInt("vehicleId");

                            if (status == 1){

                                progressDialog.hide();

                                //Check if this user is available in cache

                                if (lookForUsername(Login.this).equals(edtUsername.getText())){

                                    //update login status

                                    SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Login.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putInt("loginStatus", 1);
                                    editor.putInt("vehicleId", vehicleId);
                                    editor.apply();

                                }else{

                                    //create cache

                                    SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Login.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("uName", edtUsername.getText().toString());
                                    editor.putInt("loginStatus", 1);
                                    editor.putInt("vehicleId", vehicleId);
                                    editor.apply();

                                }

                                SharedPreferences sharedPreferences02 = getSharedPreferences("loginInfo", Login.MODE_PRIVATE);
                                MainActivity.loggedAccount = sharedPreferences02.getString("uName", "");

                                Intent intentMain = new Intent();
                                intentMain.setClass(Login.this, MainActivity.class);
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intentMain);

                                //make the progress bar invisible before go to the home screen
                                //progressBar.setVisibility(View.GONE);


                            }else if (status == 0){

                                progressDialog.hide();

                                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        loginQueue.add(loginRequest);

    }

    public String lookForUsername(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("uName", "");
    }

    private  boolean haveNet(){
        boolean haveWifi = false;
        boolean haveMobileData = false;
        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected()){
                    haveWifi  = true;
                }

            }
            if(info.getTypeName().equalsIgnoreCase("MOBILE")){
                if(info.isConnected()){
                    haveMobileData = true;
                }

            }
        }
        return haveMobileData || haveWifi;
    }

    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(), "Press again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
