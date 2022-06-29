package com.example.aravindwebservices.tokenapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravindwebservices.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TokenLoginActivity extends AppCompatActivity {

    EditText username,password;
    RequestQueue queue;
    ProgressDialog progressDialog;

    String usernameStr,passwordStr,baseurl,apikeyStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_login);
        username=findViewById(R.id.edt_username);
        password=findViewById(R.id.edt_password);

        queue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    public void login(View view) {
        progressDialog.show();
        progressDialog.setMessage("Validating the login...");

        baseurl=getResources().getString(R.string.doctor_api)+"/login";
        apikeyStr=getResources().getString(R.string.doctor_key);

        usernameStr=username.getText().toString();
        passwordStr=password.getText().toString();


        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("username",usernameStr);
        hashMap.put("password",passwordStr);
        hashMap.put("type","2");


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, baseurl, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    String status=response.getString("status_message");

                    if(status.equals("success"))
                    {
                        JSONObject jsonObject=response.getJSONObject("data");
                        String token=jsonObject.getString("token");
                        String name=jsonObject.getString("name");

                        //Explicit intent
                        startActivity(new Intent(TokenLoginActivity.this,DoctorsListActivity.class).putExtra("tokenStr",token));

                        Toast.makeText(TokenLoginActivity.this, ""+token+"\n"+name, Toast.LENGTH_SHORT).show();
                        Log.i("ResponseInfo",""+token+"\n"+name);
                    }
                    else
                    {

                        Log.i("ResponseInfo",""+status);

                        Toast.makeText(TokenLoginActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(TokenLoginActivity.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_key",""+apikeyStr);
                return hashMap;
            }
        };

        queue.add(jsonObjectRequest);
    }
}