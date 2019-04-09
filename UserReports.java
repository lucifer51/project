package com.example.lucifer.androidappforcrimereporting;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserReports extends AppCompatActivity {


String data;
    //a list to store all the products
    List<reports> productList;

    //the recyclerview
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences =UserReports.this.getSharedPreferences("Login",MODE_PRIVATE);
        data= sharedPreferences.getString("username","");
        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadProducts();
    }

    private void loadProducts() {
        String URL_PRODUCTS = "http://192.168.43.254/crimereporting/fetchingreportsdataincident.php?cnic="+data;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                           // Toast.makeText(UserReports.this,response.toString(),Toast.LENGTH_LONG).show();

                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject reportdata = array.getJSONObject(i);

                                Toast.makeText(UserReports.this,reportdata.getString("date").toString(),Toast.LENGTH_LONG).show();
                                Toast.makeText(UserReports.this,reportdata.getString("Status").toString(),Toast.LENGTH_LONG).show();

                                //adding the product to product list
                                productList.add(new reports(
                                        reportdata.getString("date"),
                                        reportdata.getString("time"),
                                        reportdata.getString("Status"),
                                        reportdata.getString("ImageofCrime"),
                                        reportdata.getString("details")

                                ));

                            }

                            //creating adapter object and setting it to recyclerview
                            ReportsAdapter adapter = new ReportsAdapter(UserReports.this, productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(UserReports.this).add(stringRequest);
    }

}
