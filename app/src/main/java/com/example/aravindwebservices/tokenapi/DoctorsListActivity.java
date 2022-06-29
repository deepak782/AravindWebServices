package com.example.aravindwebservices.tokenapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravindwebservices.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorsListActivity extends AppCompatActivity {

    String baseUrl,tokenStr,apikeyStr;
    RequestQueue queue;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        queue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    public void checkDocList(View view) {
        progressDialog.show();
        progressDialog.setMessage("Doctors List!!!");
        baseUrl=getResources().getString(R.string.doctor_api)+"/listdoctors";
        apikeyStr=getResources().getString(R.string.doctor_key);
        tokenStr=getIntent().getStringExtra("tokenStr");

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                Toast.makeText(DoctorsListActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                Log.i("DoctorsInfo",""+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorsListActivity.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("api_key",apikeyStr);
                hashMap.put("token",tokenStr);

                return hashMap;
            }
        };

        queue.add(jsonObjectRequest);


    }
}