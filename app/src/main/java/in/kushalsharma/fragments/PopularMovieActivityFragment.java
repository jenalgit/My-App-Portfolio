package in.kushalsharma.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    int sort = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
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

        // Set Up Toolbar
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.popular_movies_toolbar);
        mToolbar.setTitle(getResources().getString(R.string.title_activity_popular_movie));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolbar.inflateMenu(R.menu.menu_popular_movie);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemID = item.getItemId();
                if (menuItemID == R.id.action_most_popular) {
                    sort = 0;
                    refreshList(0);
                    return true;

                } else if (menuItemID == R.id.action_highest_rated) {
                    sort = 1;
                    refreshList(1);
                    return true;
                }
                return false;
            }
        });

        // Initialize Variables
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_popular_movies);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Specify Adapter
        mAdapter = new MovieTitleListAdapter(mTitleList, mImageList, mDateList, mOverviewList, mIDList, getActivity());

        if (pageCount == 1) {
            String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY;
            getDataFromApi(url);
        } else {
            mRecyclerView.scrollToPosition(firstVisibleItem);
        }

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);

        // Set OnScrollListener For Endless Scroll
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
                    if (sort == 0) {

                        String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY + "&page=" + String.valueOf(pageCount);
                        showSnackBar(getString(R.string.loading_page) + String.valueOf(pageCount));
                        getDataFromApi(url);
                    } else {

                        String url = TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_R_RATED + "&page=" + String.valueOf(pageCount);
                        showSnackBar(getString(R.string.loading_page) + String.valueOf(pageCount));
                        getDataFromApi(url);
                    }
                    loading = true;
                }
            }
        });

        return rootView;
    }

    /**
     * Method to Refresh list Depending on Position
     *
     * @param position List Type
     */

    private void refreshList(int position) {

        loading = true;
        visibleThreshold = 4;
        pageCount = 1;
        previousTotal = 0;
        mTitleList.clear();
        mDateList.clear();
        mIDList.clear();
        mImageList.clear();
        mOverviewList.clear();
        mAdapter.notifyDataSetChanged();
        if (position == 0)
            getDataFromApi(TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_POPULARITY);
        else
            getDataFromApi(TmdbUrls.BASE_URL + TmdbUrls.API_KEY + TmdbUrls.SORT_R_RATED);
    }

    /**
     * Method To Get Data From Api
     *
     * @param url String Type URL
     */

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
                showSnackBar(getString(R.string.error_msg));
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
