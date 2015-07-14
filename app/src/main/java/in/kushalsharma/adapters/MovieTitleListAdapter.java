package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.kushalsharma.myappportfolio.MovieDetailsActivity;
import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.PaletteNetworkImageView;
import in.kushalsharma.utils.Typefaces;

public class MovieTitleListAdapter extends RecyclerView.Adapter<MovieTitleListAdapter.ViewHolder> {

    // Array List Of String
    private ArrayList<String> mTitleList = new ArrayList<>();

    // Array List Of String
    private ArrayList<String> mImageList = new ArrayList<>();


    // Array List of String
    private ArrayList<String> mDateList = new ArrayList<>();


    // Array List of String
    private ArrayList<String> mOverviewList = new ArrayList<>();

    // Array List of String
    private ArrayList<String> mIDList = new ArrayList<>();

    // Activity
    private Activity mAct;

    // Layout Inflater
    private LayoutInflater mInflater;

    // Recycler View
    private RecyclerView mRecyclerView;

    /**
     * Constructor for Simple List Adapter
     *
     * @param mTitleList    Array List Of String
     * @param mImageList    Array List Of String
     * @param mDateList     Array List Of String
     * @param mOverviewList Array List Of String
     * @param mIDList       Array List Of String
     * @param activity      Activity
     */

    public MovieTitleListAdapter(ArrayList<String> mTitleList, ArrayList<String> mImageList, ArrayList<String> mDateList, ArrayList<String> mOverviewList, ArrayList<String> mIDList, Activity activity) {
        this.mTitleList = mTitleList;
        this.mImageList = mImageList;
        this.mDateList = mDateList;
        this.mOverviewList = mOverviewList;
        this.mIDList = mIDList;
        this.mAct = activity;

        // Initialise Layout Inflater
        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.layout_movie_title_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String imageUrl = "http://image.tmdb.org/t/p/w185/" + mImageList.get(position);
        holder.getImageView().setImageUrl(imageUrl, AppController.getInstance().getImageLoader());

        holder.getImageView().setResponseObserver(new PaletteNetworkImageView.ResponseObserver() {
            @Override
            public void onSuccess() {
                holder.setDarkColor(holder.getImageView().getDarkVibrantColor());
                holder.setLightColor(holder.getImageView().getVibrantColor());
                holder.getMoreIconView().setColorFilter(holder.getDarkColor(), PorterDuff.Mode.MULTIPLY);
                holder.getReadMoreView().setTextColor(holder.getDarkColor());
            }

            @Override
            public void onError() {

            }
        });

        holder.getTitleView().setText(mTitleList.get(position));
        holder.getDateView().setText(mDateList.get(position));
        holder.getOverviewView().setText(mOverviewList.get(position));
        holder.getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
        holder.getDateView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
        holder.getOverviewView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
        holder.getReadMoreView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));

        holder.getMoreIconView().setColorFilter(holder.getDarkColor(), PorterDuff.Mode.MULTIPLY);
        holder.getReadMoreView().setTextColor(holder.getDarkColor());

        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mAct, MovieDetailsActivity.class);
                mIntent.putExtra("id", mIDList.get(position));
                mIntent.putExtra("title", mTitleList.get(position));
                mIntent.putExtra("darkColor", holder.getDarkColor());
                mIntent.putExtra("lightColor", holder.getLightColor());
                mAct.startActivity(mIntent);
            }
        });


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

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    /**
     * View Holder Class
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int darkColor;
        int lightColor;
        // Text View
        private TextView mViewTitle;
        // Text View
        private TextView mViewDate;
        // Text View
        private TextView mViewOverview;
        // Image View
        private PaletteNetworkImageView mNetworkImageView;
        // Text View
        private TextView mViewReadMore;
        // Card View
        private CardView mCardView;


        // Color
        // Image View
        private ImageView mMoreIcon;


        /**
         * Constructor Of View Holder Class
         *
         * @param v View
         */

        public ViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mViewTitle = (TextView) v.findViewById(R.id.title);
            mViewDate = (TextView) v.findViewById(R.id.date);
            mViewOverview = (TextView) v.findViewById(R.id.overview);
            mNetworkImageView = (PaletteNetworkImageView) v.findViewById(R.id.image);
            mMoreIcon = (ImageView) v.findViewById(R.id.more_icon);
            mViewReadMore = (TextView) v.findViewById(R.id.read_more);
            darkColor = Color.parseColor("#60000000");
            lightColor = Color.parseColor("#60000000");
        }


        /**
         * This Method Returns Card View
         *
         * @return Card View
         */

        public CardView getCardView() {
            return mCardView;
        }


        /**
         * This Method Returns Title View
         *
         * @return Title View
         */

        public TextView getTitleView() {
            return mViewTitle;
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
         * This Method Returns Overview View
         *
         * @return Overview View
         */

        public TextView getOverviewView() {
            return mViewOverview;
        }


        /**
         * This Method Returns Image View
         *
         * @return Network Image View
         */

        public PaletteNetworkImageView getImageView() {
            return mNetworkImageView;
        }

        /**
         * This Method Returns Read More View
         *
         * @return Overview View
         */

        public TextView getReadMoreView() {
            return mViewReadMore;
        }


        /**
         * This Method Returns Image View
         *
         * @return More Icon
         */

        public ImageView getMoreIconView() {
            return mMoreIcon;
        }

        public int getDarkColor() {
            return darkColor;
        }

        public void setDarkColor(int color) {
            darkColor = color;
        }

        public int getLightColor() {
            return lightColor;
        }

        public void setLightColor(int color) {
            lightColor = color;
        }
    }
}
