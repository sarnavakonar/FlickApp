package com.sarnava.flickrapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private List<ImageModel> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageAdapter mAdapter;
    private ImageView search;
    private int page_no, total_pages;
    private final String API_KEY = "d9940b3a4589c2244f4a950e3e27b611";
    private final String root = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + API_KEY + "&format=json&nojsoncallback=1&page=";
    private final String end = "&text=liverpool&extras=url_o";
    private final String URL_PRODUCTS = root + 1 + end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        search = (ImageView) findViewById(R.id.toolbar_search);

        mAdapter = new ImageAdapter(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //when end of recyclerview is reached new content is loaded from next page
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                if(page_no < total_pages){
                    String new_URL = root + (page_no+1) + end;
                    loadImages(new_URL, totalItemsCount);
                }

            }
        });

        //load 1st page
        loadImages(URL_PRODUCTS, 0);

    }

    private void loadImages(String URL, final int index){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject object = new JSONObject(response);
                            JSONObject jsonobject = object.getJSONObject("photos");
                            JSONArray array = jsonobject.getJSONArray("photo");

                            page_no = (Integer.parseInt(jsonobject.getString("page")));
                            total_pages = (Integer.parseInt(jsonobject.getString("pages")));
                            //traversing through all the object
                            for (int i = 0 ; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject image = array.getJSONObject(i);

                                if(image.has("url_o") && image.has("title") && image.has("width_o") && image.has("height_o")){
                                    //adding the product to itemlist
                                    itemList.add(itemList.size(), new ImageModel(
                                            image.getString("url_o"),
                                            image.getString("title"),
                                            image.getString("width_o"),
                                            image.getString("height_o")
                                    ));
                                }

                            }

                            //creating adapter object and setting it to recyclerview
                            if(index==0){
                                mAdapter.notifyDataSetChanged();
                            }else {
                                mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), itemList.size());
                            }

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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    //search voice
    public void search(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Some problem occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = result.get(0);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                try{
                    itemList.clear();
                    mAdapter.notifyDataSetChanged();

                    String url = root + 1 + "&text="+text+"&extras=url_o";
                    loadImages(url, 0);
                }catch (Exception e){}
            }
        }
    }

}
