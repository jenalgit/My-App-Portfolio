package in.kushalsharma.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.TmdbUrls;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMovieActivityFragment extends Fragment {


    /**
     * Variables for Endless Recycler View
     */
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    private int pageCount = 1;

    // Recycler View
    private RecyclerView mRecyclerView;

    // Recycler View Adapter
    private RecyclerView.Adapter mAdapter;

    // Recycler View Layout Manager
    private LinearLayoutManager mLayoutManager;

    // Array List of String
    private ArrayList<String> mTitleList = new ArrayList<>();

    // Array List of String
    private ArrayList<String> mImageList = new ArrayList<>();

    // Array List of String
    private ArrayList<String> mDateList = new ArrayList<>();

    // Array List of String
    private ArrayList<String> mOverviewList = new ArrayList<>();

    // Array List of String
    private ArrayList<String> mIDList = new ArrayList<>();

    public PopularMovieActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mTitleList = savedInstanceState.getStringArrayList("mTitleList");
            mImageList = savedInstanceState.getStringArrayList("mImageList");
            mDateList = savedInstanceState.getStringArrayList("mDateList");
            mOverviewList = savedInstanceState.getStringArrayList("mOverviewList");
            mIDList = savedInstanceState.getStringArrayList("mIDList");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            visibleItemCount = savedInstanceState.getInt("visibleItemCount");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            loading = savedInstanceState.getBoolean("loading");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mTitleList", mTitleList);
        outState.putStringArrayList("mImageList", mImageList);
        outState.putStringArrayList("mDateList", mDateList);
        outState.putStringArrayList("mOverviewList", mOverviewList);
        outState.putStringArrayList("mIDList", mIDList);
        outState.putInt("pageCount", pageCount);
        outState.putInt("previousTotal", previousTotal);
        outState.putInt("firstVisibleItem", firstVisibleItem);
        outState.putInt("visibleItemCount", visibleItemCount);
        outState.putInt("totalItemCount", totalItemCount);
        outState.putBoolean("loading", loading);
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

        if (pageCount == 1) {
            String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY;
            getDataFromApi(url);
        } else {
            mRecyclerView.scrollToPosition(firstVisibleItem);
        }

        // Specify Adapter
        mAdapter = new MovieTitleListAdapter(mTitleList, mImageList, mDateList, mOverviewList, mIDList, getActivity());

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY + "&page=" + String.valueOf(pageCount);
                    showSnackBar("Loading Page " + String.valueOf(pageCount));
                    getDataFromApi(url);
                    loading = true;
                }
            }
        });

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
                        mIDList.add(String.valueOf(mResultObject.getInt("id")));
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


    /**
     * This Method Shows Snack Bar With Provided String
     *
     * @param msg String Type Message
     */

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG)
                .show();
    }
}
