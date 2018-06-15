package com.jjdeveloper.notecloud.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adapter.NoteAdapter;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.controller.FeedControl;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.jjdeveloper.notecloud.view.MainActivity.PREF_NAME;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mRecyclerView;
    NavigationView navigationView;
    DrawerLayout drawer;
    static SwipeRefreshLayout mSwipeRefresh;
    private int id = 1;
    public static NoteAdapter mAdapter;
    public static NoteModel noteId;
    private ArrayList<NoteModel> listNotte;
    public static long result;
    TextView labelUser, labelNote, labelEmail;
    private RecyclerView recyclerListFeed;
    public UserModel userLogado;
    private static final String PREF_NAME = "MainActivityPreferences";
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
                fab2.hide();
            }
        });

        initObjects();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshContent();
            }
        });
        mAdapter = new NoteAdapter(new ArrayList<>(0),FeedActivity.this);
        setupRecycler();
        labelUser.setText(MainActivity.userLogado.getLogin());
        labelEmail.setText(MainActivity.userLogado.getEmail());
        //labelNote = (TextView) findViewById(R.id.txtNoEvents);
        //labelUser.setText("Olá " + MainActivity.userLogado.getNome());
        //loadEvento();
        FeedControl.listaEvento(FeedActivity.this);

    }

    private void initObjects(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_feedlayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View navHeader = navigationView.getHeaderView(0);
        labelUser = (TextView) navHeader.findViewById(R.id.labelUser);
        labelEmail = (TextView) navHeader.findViewById(R.id.labelEmail);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void btLogout(View v){
        esqueceUsuario();
        Intent i = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void esqueceUsuario(){
        //UpdateTokenId.updateTokenUser(false);

        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", "");
        editor.putString("senha", "");
        editor.commit();
    }

    private void setupRecycler() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView = new RecyclerView(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // Adiciona o adapter que irá anexar os objetos à lista.
        //mAdapter = new LineAdapter(new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                    fab2.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                    fab2.show();
                }
            }
        });
    }

    private void refreshContent(){
        FeedControl.listaEvento(FeedActivity.this);
    }

    public static void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefresh.setRefreshing(false);
    }

    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 segundos para sair
                backPressedTime = t;
                Toast.makeText(this, "Clique 2x voltar para sair!",
                        Toast.LENGTH_SHORT).show();
            } else {    // se pressionado novamente encerrar app
                // clean up
                finish();//super.onBackPressed();       // bye
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_mynotes) {
            Fragment fragment = new Fragment();

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_mynotes, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_likes) {

        } else if (id == R.id.nav_favorites) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
