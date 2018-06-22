package com.jjdeveloper.notecloud.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.fragment.FavoritesFragment;
import com.jjdeveloper.notecloud.view.fragment.FeedFragment;
import com.jjdeveloper.notecloud.view.fragment.LikesFragment;
import com.jjdeveloper.notecloud.view.fragment.MyNotesFragment;

import static com.jjdeveloper.notecloud.config.Config.PREF_NAME;

public class FeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    android.support.v4.app.FragmentManager fragment;
    Context activity;
    NavigationView navigationView;
    DrawerLayout drawer;
    TextView labelUser, labelEmail;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    public static NoteModel clickedNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = getApplicationContext();
        //MobileAds.initialize(this, "ca-app-pub-1431450907522749~2409168009");
        //ActionAdapter.action(MainActivity.userLogado.getId(),activity);
        fragment = getSupportFragmentManager();
        setTitle("NoteCloud Feed");
        fragment.beginTransaction().replace(R.id.content_fragment, new FeedFragment()).commit();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View navHeader = navigationView.getHeaderView(0);
        labelUser = (TextView) navHeader.findViewById(R.id.labelUser);
        labelEmail = (TextView) navHeader.findViewById(R.id.labelEmail);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
                fab2.hide();
            }
        });*/


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        labelUser.setText(MainActivity.userLogado.getLogin());
        labelEmail.setText(MainActivity.userLogado.getEmail());

        /*AdsSetting ads = new AdsSetting(activity);
        ads.start();*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void btLogout(View v){
        esqueceUsuario();
        Intent i = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    public void setFab2(View v){
        RecyclerView r = (RecyclerView) FeedFragment.view.findViewById(R.id.recycler_list);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) r
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void esqueceUsuario(){
        //UpdateTokenId.updateTokenUser(false);

        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", "");
        editor.putString("senha", "");
        editor.commit();
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
        if (id == R.id.action_logoff) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        drawer.closeDrawers();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            FeedFragment.swipeRefresh = 0;
            setTitle("NoteCloud Feed");
            fragment.beginTransaction().replace(R.id.content_fragment, new FeedFragment()).commit();
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_mynotes) {
            setTitle("My Notes");
            fragment.beginTransaction().replace(R.id.content_fragment, new MyNotesFragment()).commit();
        } else if (id == R.id.nav_likes) {
            setTitle("Likes");
            fragment.beginTransaction().replace(R.id.content_fragment, new LikesFragment()).commit();
        } else if (id == R.id.nav_favorites) {
            setTitle("My Favorites");
            fragment.beginTransaction().replace(R.id.content_fragment, new FavoritesFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clickNoteInfo(View v){
        //clickedNote = FeedFragment.noteInfo(v);
        clickedNote = FeedFragment.mAdapter.getNote(21);
        //setTitle("Criar Nota");
        //fragment.beginTransaction().replace(R.id.content_fragment, new NoteInfoFragment()).commit();
        Intent i = new Intent(this, NoteActivity.class);
        startActivity(i);
    }
}
