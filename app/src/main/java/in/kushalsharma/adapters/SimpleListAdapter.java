package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import in.kushalsharma.myappportfolio.PopularMovieActivity;
import in.kushalsharma.myappportfolio.R;
import in.kushalsharma.utils.Typefaces;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder> {

    // Array List Of String
    private ArrayList<String> mTitleList = new ArrayList<>();

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
     * @param mRecyclerView Recycler View For Snack Bar
     * @param activity      Activity
     */

    public SimpleListAdapter(ArrayList<String> mTitleList, RecyclerView mRecyclerView, Activity activity) {
        this.mTitleList = mTitleList;
        this.mAct = activity;
        this.mRecyclerView = mRecyclerView;

        // Initialise Layout Inflater
        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.layout_simple_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.getTitleView().setText(mTitleList.get(position));
        holder.getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
        holder.getRippleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Intent mIntent = new Intent(mAct, PopularMovieActivity.class);
                        mAct.startActivity(mIntent);
                        break;
                    default:
                        showSnackBar("This is my " + holder.getTitleView().getText() + "!");
                }
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

        // Text View
        private TextView mViewTitle;

        // Material Ripple Layout
        private MaterialRippleLayout mViewRipple;

        /**
         * Constructor Of View Holder Class
         *
         * @param v View
         */

        public ViewHolder(View v) {
            super(v);

            // Initialise variables
            mViewTitle = (TextView) v.findViewById(R.id.title);
            mViewRipple = (MaterialRippleLayout) v.findViewById(R.id.ripple);
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
         * This Method Returns Ripple View
         *
         * @return Ripple View
         */

        public MaterialRippleLayout getRippleView() {
            return mViewRipple;
        }
    }
}
