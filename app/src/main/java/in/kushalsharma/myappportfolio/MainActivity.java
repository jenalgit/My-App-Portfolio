package in.kushalsharma.myappportfolio;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import in.kushalsharma.adapters.SimpleListAdapter;
import in.kushalsharma.utils.DividerItemDecoration;


public class MainActivity extends AppCompatActivity {

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

    // Array List of String
    private ArrayList<String> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Variables
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mLayoutManager = new LinearLayoutManager(this);

        // Set Title on Collapsing Toolbar Layout
        mCollapsingToolbarLayout.setTitle(getString(R.string.title_main));
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        // Set Layout Manager On Recycler View
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set Properties On Recycler View
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Add Items In Array List
        mTitleList.add(getString(R.string.spotify_streamer_app));
        mTitleList.add(getString(R.string.football_scores_app));
        mTitleList.add(getString(R.string.library_app));
        mTitleList.add(getString(R.string.build_it_bigger_app));
        mTitleList.add(getString(R.string.xyz_reader_app));
        mTitleList.add(getString(R.string.my_app));

        // Specify Adapter
        mAdapter = new SimpleListAdapter(mTitleList, mRecyclerView, MainActivity.this);

        // Set Adapter on Recycler View
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
