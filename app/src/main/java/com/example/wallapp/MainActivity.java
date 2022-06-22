package com.example.wallapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{
    private EditText searchedt;
    private ImageView searchIV;
    private RecyclerView categoryRV, wallpaperRV;
    private ProgressBar loadingPB;
   private ArrayList<String>  wallpaperArrayList;
   private ArrayList<CategoryRVModel>  categoryRVModelArrayList;
   private CategoryRVAdapter categoryRVAdapter;
   private  WallpaperRVAdapter wallpaperRVAdapter;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchedt = findViewById(R.id.idEditSearch);
        searchIV = findViewById(R.id.idIVSearch);
        categoryRV = findViewById(R.id.idRVCategory);
        wallpaperRV = findViewById(R.id.idRVWallpapers);
        loadingPB = findViewById(R.id.idPBLoading);
        wallpaperArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL,false);
       categoryRV.setLayoutManager(linearLayoutManager);
       categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this, this::onCategoryClick);
       categoryRV.setAdapter(categoryRVAdapter);

       GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
       wallpaperRV.setLayoutManager(gridLayoutManager);
       wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList, this);
       wallpaperRV.setAdapter(wallpaperRVAdapter);
        //563492ad6f917000010000013482789d103e481a82c39c05ea7bc9cd
       getCategories();

       getwallpapers();
       wallpaperArrayList.clear();
       loadingPB.setVisibility(View.VISIBLE);


       searchIV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            String searchStr = searchedt.getText().toString();
            if (searchStr.isEmpty()){
                Toast.makeText(MainActivity.this,"Please Enter Your Search Query!!", Toast.LENGTH_SHORT).show();
            }else {
                getWallpapersByCategory(searchStr);
            }
           }
       });

    }

    private void getWallpapersByCategory(String category){
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query="+ category + "&per_page=30&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photoArray = null;
                try {
                    photoArray = response.getJSONArray("photos");
                    for (int i = 0; i< photoArray.length(); i++){
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to load wallpapers.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization","563492ad6f917000010000013482789d103e481a82c39c05ea7bc9cd");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void getwallpapers() {
       wallpaperArrayList.clear();
       loadingPB.setVisibility(View.VISIBLE);
       String url = "https://api.pexels.com/v1/curated?per_page=30&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               loadingPB.setVisibility(View.GONE);
               try {
                   JSONArray photoArray = response.getJSONArray("photos");
                 for (int i = 0; i< photoArray.length(); i++){
                     JSONObject photoObj = photoArray.getJSONObject(i);
                     String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                     wallpaperArrayList.add(imgUrl);
                 }
                 wallpaperRVAdapter.notifyDataSetChanged();
               }catch (JSONException e){
                   e.printStackTrace();
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Fail to load wallpapers.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization","563492ad6f917000010000013482789d103e481a82c39c05ea7bc9cd");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Programing", "https://images.unsplash.com/photo-1587620962725-abab7fe55159?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1031&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Nature", "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Travel", "https://images.unsplash.com/photo-1500835556837-99ac94a94552?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=987&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Architecture", "https://images.unsplash.com/photo-1485627941502-d2e6429a8af0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=870&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Arts", "https://images.unsplash.com/photo-1597423244037-519742d0a9f0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=774&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Musics", "https://images.unsplash.com/photo-1611607546163-86e495caac68?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1008&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Abstract", "https://images.unsplash.com/photo-1567095761054-7a02e69e5c43?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=987&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Cars", "https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=928&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Flowers", "https://images.unsplash.com/photo-1508610048659-a06b669e3321?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=870&q=80"));
        categoryRVAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCategoryClick(int position) {
    String category = categoryRVModelArrayList.get(position).getCategory();
    getWallpapersByCategory(category);
    }
}