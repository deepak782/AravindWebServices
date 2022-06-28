package com.example.aravindwebservices.officeApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {

    EditText mail,name,password,mobile;
    Button Signup;
    TextView output;
    String baseurl_register;
    RequestQueue queue;
    ProgressDialog progressDialog;

    String nameStr,mobileStr,mailStr,passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mail=findViewById(R.id.email);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        mobile=findViewById(R.id.mobile);
        Signup=findViewById(R.id.signup_btn);
        output=findViewById(R.id.txt_response1);

        queue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);



        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signupMethod();
            }
        });
    }

    private void signupMethod() {
        progressDialog.show();
        progressDialog.setMessage("Creating User!!");
        baseurl_register=getResources().getString(R.string.signup_api_office);

        nameStr=name.getText().toString();
        mobileStr=mobile.getText().toString();
        mailStr=mail.getText().toString();
        passwordStr=password.getText().toString();

        HashMap<String,Object>  hashMap=new HashMap<>();
        hashMap.put("Name",nameStr);
        hashMap.put("Mobile",mobileStr);
        hashMap.put("Email",mailStr);
        hashMap.put("Password",passwordStr);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, baseurl_register, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    String code=response.getString("code");
                    String description=response.getString("description");
                    String message=response.getString("message");

                    output.setText(""+code+"\n"+description+"\n"+message);

                } catch (JSONException e) {
                    e.printStackTrace();
                    output.setText("Params keys Wrong\n"+e.getLocalizedMessage());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                output.setText(""+error.getLocalizedMessage());

            }
        })
        /*{
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        }*/;
        queue.add(jsonObjectRequest);


    }
}