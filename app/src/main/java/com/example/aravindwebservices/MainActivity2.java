package com.example.aravindwebservices;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    EditText cityName;
    String getkey;
    String Baseurl,CityStr;
    RequestQueue queue;
    ProgressDialog progressDialog;
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cityName=findViewById(R.id.cityname);
        responseText=findViewById(R.id.txt_response);

        queue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

    }

    public void submit(View view) {
        progressDialog.show();
        progressDialog.setMessage("Checking Current Weather..");
        getkey=getResources().getString(R.string.weather_api_key);
        CityStr=cityName.getText().toString();

        if(CityStr.trim().length()<0)
        {
            Toast.makeText(this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else
        {
            Baseurl="https://api.openweathermap.org/data/2.5/weather?q="+CityStr+"&appid="+getkey;
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Baseurl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();

                    try {
                        JSONObject coord=response.getJSONObject("coord");

                        double lat=coord.getDouble("lat");
                        double lng=coord.getDouble("lon");

                        JSONArray weather=response.getJSONArray("weather");
                        JSONObject weather_object= weather.getJSONObject(0);
                        String main=weather_object.getString("main");
                        String description=weather_object.getString("description");

                        JSONObject sys=response.getJSONObject("sys");
                        String countryCode=sys.getString("country");
                        long sunrise=sys.getLong("sunrise");
                        long sunset=sys.getLong("sunset");

                        /*@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy = HH:mm");
                        String sunriseString=simpleDateFormat.format(new Date(sunrise));
                        String sunSetString=simpleDateFormat.format(new Date(sunset));*/

                        Date timeSunrise = new Date(sunrise * 1000);
                        Date timeSunset = new Date(sunset * 1000);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy = HH:mm:ss");

                        String  sunriseString= sdf.format(timeSunrise);
                        String  sunSetString= sdf.format(timeSunset);


                        responseText.setText("LAT:- "+lat+"\n"+"LON:- "+lng+"\n"+
                                "Main:- "+main+"\n"+"Description:- "+description+"\n"+
                                "Country :- "+countryCode+"\n"+"Sunrise:- "+sunriseString+"\n"+
                                "Sunset:- "+sunSetString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity2.this, "api key \n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    // Toast.makeText(MainActivity2.this, ""+response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();

                  //  Toast.makeText(MainActivity2.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                }
            });

            queue.add(jsonObjectRequest);




        }

    }
}