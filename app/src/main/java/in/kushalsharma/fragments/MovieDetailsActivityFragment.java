package in.kushalsharma.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.PaletteNetworkImageView;
import in.kushalsharma.utils.TmdbUrls;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    private String ID;
    private String backdropPath;
    private String budget;
    private String genres = "";
    private String overView;
    private String posterPath;
    private String releaseDate;
    private String status;
    private Double voteAvg;
    private TextView mRatingView, mGenreView, mDateView, mStatusView;
    private PaletteNetworkImageView mPosterView;
    private NetworkImageView mBackdropView;

    // Toolbar
    private Toolbar mToolbar;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Bundle mBundle = getActivity().getIntent().getExtras();
        ID = mBundle.getString("id");

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_movie_details);

        // Set Title on Collapsing Toolbar Layout
        mToolbar.setTitle(mBundle.getString("title"));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mRatingView = (TextView) rootView.findViewById(R.id.rating);
        mGenreView = (TextView) rootView.findViewById(R.id.genre);
        mDateView = (TextView) rootView.findViewById(R.id.date);
        mStatusView = (TextView) rootView.findViewById(R.id.status);
        mPosterView = (PaletteNetworkImageView) rootView.findViewById(R.id.image);
        mBackdropView = (NetworkImageView) rootView.findViewById(R.id.backdrop);


        String url = TmdbUrls.MOVIE_BASE_URL + ID + "?" + TmdbUrls.API_KEY;
        JsonObjectRequest movieDataRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    backdropPath = response.getString("backdrop_path");
                    budget = response.getString("budget");
                    overView = response.getString("overview");
                    posterPath = response.getString("poster_path");
                    releaseDate = response.getString("release_date");
                    status = response.getString("status");
                    voteAvg = response.getDouble("vote_average");

                    JSONArray mGenreArray = response.getJSONArray("genres");

                    for (int i = 0; i < mGenreArray.length(); i++) {
                        String genre = mGenreArray.getJSONObject(i).getString("name");
                        if (i != mGenreArray.length() - 1) {
                            genres = genres + genre + ", ";
                        } else {
                            genres = genres + genre + ".";
                        }
                    }

                    mRatingView.setText(String.valueOf(voteAvg));
                    mGenreView.setText(genres);
                    mDateView.setText(releaseDate);
                    mStatusView.setText(status);

                    String imageUrl = "http://image.tmdb.org/t/p/w342/" + posterPath;
                    mPosterView.setImageUrl(imageUrl, AppController.getInstance().getImageLoader());

                    String backdropUrl = "http://image.tmdb.org/t/p/w780/" + backdropPath;
                    mBackdropView.setImageUrl(backdropUrl, AppController.getInstance().getImageLoader());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(movieDataRequest);

        return rootView;
    }
}
