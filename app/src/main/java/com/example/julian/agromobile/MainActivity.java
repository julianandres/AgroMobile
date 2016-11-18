package com.example.julian.agromobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julian.agromobile.layoutmanagers.CustomGridLayoutManager;
import com.example.julian.agromobile.models.Usuario;
import com.example.julian.agromobile.util.AppUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FloatingActionButton btnInitProces;

    NavigationView nav;
    DrawerLayout drawer;

    ActionBarDrawerToggle toggle;

    RecyclerView recyclerView;

    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(LoginActivity.KEY_USER);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();
        String usLog=preferences.getString(LoginActivity.KEY_USER,"-1");

        btnInitProces= (FloatingActionButton) findViewById(R.id.fab);
        btnInitProces.setOnClickListener(this);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeColors(Color.argb(0xff, 0xff, 0x00, 0x00)
                , Color.argb(0xff, 0x00, 0xff, 0x00)
                , Color.argb(0xff, 0x00, 0x00, 0xff));
        System.out.println(usLog);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new CustomGridLayoutManager(this));

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(this);

        toggle =  new ActionBarDrawerToggle(this,drawer
                ,R.string.drawer_open, R.string.drawer_close);


        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);

        ImageView userImage = (ImageView) nav.getHeaderView(0).findViewById(R.id.img_user);
        TextView userName = (TextView) nav.getHeaderView(0).findViewById(R.id.txt_usr);

        userName.setText(preferences.getString(AppUtil.USER_NAME,""));
        String urlImage = preferences.getString(AppUtil.USER_IMG,"");

        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();

        Picasso.with(this).load(Uri.parse("http://tusejemplos.com/wp-content/uploads/2014/12/url-300x240.png"))
                .transform(transformation)
                .into(userImage);

        if(usLog.equals("-1")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_logout:

                SharedPreferences.Editor editor = getSharedPreferences(AppUtil.PREFERENCE_NAME,MODE_PRIVATE)
                        .edit();

                editor.putBoolean(AppUtil.USER_LOGIN, false);

                editor.commit();

                Intent intent =  new Intent(this, MainActivity.class);
                startActivity(intent);

                finish();

                break;

        }

       // drawer.closeDrawers();
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(toggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_perfil) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        swipe.setRefreshing(false);
        if(v.getId() == R.id.fab){
            Intent intent = new Intent(this, NewProcessActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        toggle.onDrawerSlide(drawerView, slideOffset);

    }
    @Override
    public void onDrawerOpened(View drawerView) {
        toggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        toggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        toggle.onDrawerStateChanged(newState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
}
