package com.example.vinay.smsapplication.Search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vinay.smsapplication.Adapter.SearchAdapter;
import com.example.vinay.smsapplication.Messaging.SMS;
import com.example.vinay.smsapplication.R;
import com.example.vinay.smsapplication.Utilities.DatabaseHandler;
import com.example.vinay.smsapplication.Utilities.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    List<SMS> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isDark = pref.getBoolean("isDark", true);
        if (isDark) {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String txt = "Search";
        int searchListLength;
        Intent intent = getIntent();

        String tableName = "_INBOX";

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            txt = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + txt, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            txt = intent.getExtras().getString("query");
            Log.d("TAG", "onCreateSearchable: " + txt);
            Toast.makeText(this, "Searching by: " + txt, Toast.LENGTH_SHORT).show();
        }

        searchList = db.getMessageDetails(txt, tableName);

        searchListLength = searchList.size();

        String searchResult = searchListLength + " results for \"" + txt + "\"";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator
                    (isDark ? R.drawable.ic_arrow_back_white_48dp : R.drawable.ic_arrow_back_black_48dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(searchResult);
            getSupportActionBar().setElevation(15.0f);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);

        assert recyclerView != null;
        SearchAdapter mAdapter = new SearchAdapter(searchList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}