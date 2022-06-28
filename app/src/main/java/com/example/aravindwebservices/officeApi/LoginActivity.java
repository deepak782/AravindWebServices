package com.example.aravindwebservices.officeApi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravindwebservices.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText emailormobile,password;
    TextView signup,output;
    Button login;

    String baseUrl_login;
    RequestQueue queue;
    ProgressDialog progressDialog;

    String emStr,passStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailormobile=findViewById(R.id.email_mobile);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.txt_signup);
        output=findViewById(R.id.txt_response1);
        login=findViewById(R.id.login_btn);

        queue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginMethod();
            }
        });
    }

    private void loginMethod() {
        progressDialog.show();
        progressDialog.setMessage("Validating Login");

        baseUrl_login=getResources().getString(R.string.login_api_office);

        emStr=emailormobile.getText().toString();
        passStr=password.getText().toString();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Email",emStr);
        hashMap.put("Mobile",emStr);
        hashMap.put("Password",passStr);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, baseUrl_login, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                //output.setText(""+response);

                try {
                    String message=response.getString("message");

                    if(message.equals("success"))
                    {
                        String description=response.getString("description");
                        JSONArray Info=response.getJSONArray("Info");
                        JSONObject jsonObject=Info.getJSONObject(0);
                        String Name=jsonObject.getString("Name");
                        String Email=jsonObject.getString("Email");
                        String Mobile=jsonObject.getString("Mobile");

                        output.setText(""+description+"\n"+Name+"\n"+Email+"\n"+Mobile);

                    }
                    else if(message.equals("fail")){

                        String description=response.getString("description");
                        output.setText(""+description);

                    }
                    else
                    {

                        output.setText(""+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                output.setText(""+error.getLocalizedMessage());


            }
        });
        queue.add(jsonObjectRequest);
    }
}