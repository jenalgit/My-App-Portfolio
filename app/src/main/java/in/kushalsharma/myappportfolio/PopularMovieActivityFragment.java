package in.kushalsharma.myappportfolio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMovieActivityFragment extends Fragment {


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


    public PopularMovieActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popular_movie, container, false);

        // Initialize Variables
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_popular_movies);
        mLayoutManager = new LinearLayoutManager(getActivity());

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY;
        getDataFromApi(url);

        // Specify Adapter
        mAdapter = new MovieTitleListAdapter(mTitleList, mImageList, mDateList, mOverviewList, getActivity());

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
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
