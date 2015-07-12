package in.kushalsharma.myappportfolio;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.kushalsharma.adapters.MovieTitleListAdapter;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.TmdbUrls;


public class PopularMoviesActivity extends AppCompatActivity {


    // Recycler View
    private RecyclerView mRecyclerView;

    // Recycler View Adapter
    private RecyclerView.Adapter mAdapter;

    // Recycler View Layout Manager
    private RecyclerView.LayoutManager mLayoutManager;


    // Array List of String
    private ArrayList<String> mTitleList = new ArrayList<>();


    // Array List of String
    private ArrayList<String> mImageList = new ArrayList<>();


    // Array List of String
    private ArrayList<String> mDateList = new ArrayList<>();


    // Array List of String
    private ArrayList<String> mOverviewList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.popular_movies_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getResources().getString(R.string.title_activity_popular_movies));
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Variables
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_popular_movies);
        mLayoutManager = new LinearLayoutManager(this);

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY;
        getDataFromApi(url);

        // Specify Adapter
        mAdapter = new MovieTitleListAdapter(mTitleList, mImageList, mDateList, mOverviewList, PopularMoviesActivity.this);

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDataFromApi(String url) {


        JsonObjectRequest getListData = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mResultArray = response.getJSONArray("results");
                    for (int i = 0; i < mResultArray.length(); i++) {
                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        mTitleList.add(mResultObject.getString("title"));
                        mImageList.add(mResultObject.getString("poster_path"));
                        mDateList.add("Release Date - " + mResultObject.getString("release_date"));
                        mOverviewList.add(mResultObject.getString("overview"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(getListData);
    }

}
