package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.Typefaces;

/**
 * Created by Kushal on 14/7/15.
 * This is Movies Details Adapter
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // Array List Of String
    private ArrayList<String> rowZero = new ArrayList<>();

    // Array List Of String
    private ArrayList<String> rowOne = new ArrayList<>();

    // Activity
    private Activity mAct;

    // Layout Inflater
    private LayoutInflater mInflater;

    /**
     * Constructor for Simple List Adapter
     *
     * @param rowZero  Array List Of String
     * @param rowOne   Recycler View For Snack Bar
     * @param activity Activity
     */

    public MovieDetailsAdapter(ArrayList<String> rowZero, ArrayList<String> rowOne, Activity activity) {
        this.rowZero = rowZero;
        this.rowOne = rowOne;
        this.mAct = activity;

        // Initialise Layout Inflater
        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDetails = mInflater.inflate(R.layout.layout_list_details, parent, false);
        View viewInfo = mInflater.inflate(R.layout.layout_list_info, parent, false);
        switch (viewType) {
            case 0:
                return new ViewHolderInfo(viewInfo);
            case 1:
                return new ViewHolderOverview(viewDetails);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (position) {
            case 0:
                String imageUrl = "http://image.tmdb.org/t/p/w342/" + rowZero.get(0);


                ((ViewHolderInfo) holder).getRatingView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderInfo) holder).getViewGenre().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderInfo) holder).getDateView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderInfo) holder).getStatusView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));

                ((ViewHolderInfo) holder).getPosterView().setImageUrl(imageUrl, AppController.getInstance().getImageLoader());
                ((ViewHolderInfo) holder).getRatingView().setText("Rating - " + rowZero.get(1));
                ((ViewHolderInfo) holder).getViewGenre().setText("Genre - " + rowZero.get(2));
                ((ViewHolderInfo) holder).getDateView().setText("Release Date - " + rowZero.get(3));
                ((ViewHolderInfo) holder).getStatusView().setText("Status - " + rowZero.get(4));
                break;
            case 1:
                ((ViewHolderOverview) holder).getOverviewView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderOverview) holder).getOverviewView().setText("Overview - " + rowOne.get(0));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }


    /**
     * View Holder Class Info
     */

    public static class ViewHolderInfo extends RecyclerView.ViewHolder {

        // Network Image View
        private NetworkImageView mPosterView;

        // Text View
        private TextView mViewRating;

        // Text View
        private TextView mViewGenre;

        // Text View
        private TextView mViewDate;

        // Text View
        private TextView mViewStatus;

        /**
         * Constructor Of View Holder Class
         *
         * @param v View
         */

        public ViewHolderInfo(View v) {
            super(v);

            // Initialise variables
            mPosterView = (NetworkImageView) v.findViewById(R.id.poster);
            mViewRating = (TextView) v.findViewById(R.id.rating);
            mViewGenre = (TextView) v.findViewById(R.id.genre);
            mViewDate = (TextView) v.findViewById(R.id.date);
            mViewStatus = (TextView) v.findViewById(R.id.status);

        }


        /**
         * This Method Returns Poster View
         *
         * @return Poster View
         */

        public NetworkImageView getPosterView() {
            return mPosterView;
        }

        /**
         * This Method Returns Rating View
         *
         * @return Rating View
         */

        public TextView getRatingView() {
            return mViewRating;
        }


        /**
         * This Method Returns Genre View
         *
         * @return Genre View
         */

        public TextView getViewGenre() {
            return mViewGenre;
        }


        /**
         * This Method Returns Date View
         *
         * @return Date View
         */

        public TextView getDateView() {
            return mViewDate;
        }

        /**
         * This Method Returns Status View
         *
         * @return Status View
         */

        public TextView getStatusView() {
            return mViewStatus;
        }

    }

    /**
     * View Holder Class Details
     */

    public static class ViewHolderOverview extends RecyclerView.ViewHolder {

        // Text View
        private TextView mViewOverview;

        /**
         * Constructor Of View Holder Class
         *
         * @param v View
         */

        public ViewHolderOverview(View v) {
            super(v);

            // Initialise variables
            mViewOverview = (TextView) v.findViewById(R.id.overview);
        }

        /**
         * This Method Returns Title View
         *
         * @return Title View
         */

        public TextView getOverviewView() {
            return mViewOverview;
        }
    }


}
