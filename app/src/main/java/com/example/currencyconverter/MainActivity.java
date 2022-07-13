package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner etCur1,etCur2;
    EditText etAmount;
    TextView tvDisplay;
    Button btnSubmit;
    String Cur1,Cur2,Amount;
    Float Converted_Amount;
    HashMap<String, Float> ExchangeRates = new HashMap<>() ;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCur1 = findViewById(R.id.etCur1);
        etCur2 = findViewById(R.id.etCur2);
        etAmount = findViewById(R.id.etAmount);
        tvDisplay = findViewById(R.id.tvDisplay);
        btnSubmit = findViewById(R.id.btnSubmit);
        Collections.addAll(arrayList,getResources().getStringArray(R.array.country_array));
        getOnlineData();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cur1 = etCur1.getSelectedItem().toString().trim().toUpperCase().substring(0,3);
                Cur2 = etCur2.getSelectedItem().toString().trim().toUpperCase().substring(0,3);
                Amount = etAmount.getText().toString().trim().toUpperCase();
                ConvertMoney();
                tvDisplay.setText(Converted_Amount+"");
                tvDisplay.setVisibility(View.VISIBLE);
            }
        });

    }

    private void ConvertMoney()
    {
        Converted_Amount = Float.parseFloat(Amount)*(ExchangeRates.get(Cur2)/ExchangeRates.get(Cur1));
    }

    private void getOnlineData()
    {
        String url = "http://data.fixer.io/api/latest?access_key=ddbcd303d6d41230529cf2a681335b46";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject Rate = response.getJSONObject("rates");
                    for(int i=0;i<arrayList.size();i++)
                    {
                        ExchangeRates.put(arrayList.get(i).substring(0,3),Float.parseFloat(Rate.getString(arrayList.get(i).substring(0,3))));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

}