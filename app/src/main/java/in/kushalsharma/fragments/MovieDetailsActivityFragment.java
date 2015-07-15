package in.kushalsharma.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.kushalsharma.adapters.MovieDetailsAdapter;
import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.DividerItemDecoration;
import in.kushalsharma.utils.TmdbUrls;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    // Recycler View
    private RecyclerView mRecyclerView;

    // Recycler View Adapter
    private RecyclerView.Adapter mAdapter;

    // Recycler View Layout Manager
    private RecyclerView.LayoutManager mLayoutManager;

    // Collapsing Toolbar Layout
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    // Toolbar
    private Toolbar mToolbar;

    // Network Image View
    private NetworkImageView mBackdrop;

    // ArrayList Of String
    private ArrayList<String> rowZero = new ArrayList<>();

    // Array List Of String
    private ArrayList<String> rowOne = new ArrayList<>();


    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);


        // Initialise Variables
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBackdrop = (NetworkImageView) rootView.findViewById(R.id.backdrop);

        // Set Status Bar Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getActivity().getIntent().getIntExtra("darkColor", 0));
        }

        // Set Title on Collapsing Toolbar Layout
        mCollapsingToolbarLayout.setTitle(getActivity().getIntent().getExtras().getString("title"));
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setContentScrimColor(getActivity().getIntent().getIntExtra("lightColor", 0));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getMovieDataFromID(getActivity().getIntent().getExtras().getString("id", null));

        return rootView;
    }

    /**
     * Method to Get Data From given ID
     *
     * @param id ID of Movie
     */

    private void getMovieDataFromID(String id) {
        String url = TmdbUrls.MOVIE_BASE_URL + id + "?" + TmdbUrls.API_KEY;
        Log.e("TAG", url);
        JsonObjectRequest getDetails = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO start here

                try {

                    String imageUrl = "http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path");
                    mBackdrop.setImageUrl(imageUrl, AppController.getInstance().getImageLoader());

                    rowZero.add(response.getString("poster_path"));
                    rowZero.add(response.getString("vote_average"));

                    String genres = "";

                    JSONArray genreArray = response.getJSONArray("genres");

                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }

                    rowZero.add(genres);
                    rowZero.add(response.getString("release_date"));
                    rowZero.add(response.getString("status"));

                    rowOne.add(response.getString("overview"));
                    // Specify Adapter
                    mAdapter = new MovieDetailsAdapter(rowZero, rowOne, getActivity());

                    // Set Adapter on Recycler View
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
            }
        });
        AppController.getInstance().addToRequestQueue(getDetails);
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
