package in.kushalsharma.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());

        // Set Title on Collapsing Toolbar Layout
        mCollapsingToolbarLayout.setTitle(getActivity().getIntent().getExtras().getString("title"));
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        rowZero.add("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        rowZero.add("10");
        rowZero.add("Okay");
        rowZero.add("Old Date");
        rowZero.add("status");

        rowOne.add("this is overview");

        getMovieDataFromID(getActivity().getIntent().getExtras().getString("id", null));

        // Specify Adapter
        mAdapter = new MovieDetailsAdapter(rowZero, rowOne, getActivity());

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void getMovieDataFromID(String id) {
        String url = TmdbUrls.MOVIE_BASE_URL + id + "?" + TmdbUrls.API_KEY;
        JsonObjectRequest getDetails = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO start here

//                try {
//                    rowZero.add(response.getString("poster_path"));
//                    rowZero.add(response.getString("vote_average"));
//                    rowZero.add(response.getString("poster_path"));
//                    rowZero.add(response.getString("poster_path"));
//                    rowZero.add(response.getString("poster_path"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(getDetails);
    }
}
