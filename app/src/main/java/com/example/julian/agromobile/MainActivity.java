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

import com.example.julian.agromobile.adapters.ProcesosAdapter;
import com.example.julian.agromobile.layoutmanagers.CustomGridLayoutManager;
import com.example.julian.agromobile.models.Proceso;
import com.example.julian.agromobile.models.Usuario;
import com.example.julian.agromobile.net.ProcesosCon;
import com.example.julian.agromobile.util.AppUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, ProcesosAdapter.OnItemClick, ProcesosCon.ProcesoConI, SwipeRefreshLayout.OnRefreshListener {

    static final String KEY_USER="userId";
    static final String KEY_USER_NAME="userName";
    static final String KEY_USER_MAIL="userMail";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FloatingActionButton btnInitProces;

    NavigationView nav;
    DrawerLayout drawer;
    ProcesosAdapter procesoAdapter;
    List<Proceso> dataProcces;
    ActionBarDrawerToggle toggle;
    ProcesosCon procesosCon;
    RecyclerView recyclerView;
    String usuarioLogin;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(LoginActivity.KEY_USER);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences= getSharedPreferences("pref",MODE_PRIVATE);
        editor=preferences.edit();

        usuarioLogin=preferences.getString(LoginActivity.KEY_USER,"-1");

        btnInitProces= (FloatingActionButton) findViewById(R.id.fab);
        btnInitProces.setOnClickListener(this);
        dataProcces = new ArrayList<>();
        procesosCon = new ProcesosCon(this,this);

        procesosCon.getAllProcess(usuarioLogin);
        Proceso proceso1= new Proceso();
        proceso1.setNombre("Proceso1");
        proceso1.setState(false);
        proceso1.setFechaInicio(new Date());

        Proceso proceso3= new Proceso();
        proceso3.setNombre("Proceso3");
        proceso3.setState(false);
        proceso3.setFechaInicio(new Date());

        Proceso proceso2= new Proceso();
        proceso2.setNombre("Proceso2");
        proceso2.setState(true);
        proceso2.setFechaInicio(new Date());

        dataProcces.add(proceso1);
        dataProcces.add(proceso2);
        dataProcces.add(proceso3);
        procesoAdapter = new ProcesosAdapter(this,dataProcces);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setColorSchemeColors(Color.argb(0xff, 0xff, 0x00, 0x00)
                , Color.argb(0xff, 0x00, 0xff, 0x00)
                , Color.argb(0xff, 0x00, 0x00, 0xff));

        swipe.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(procesoAdapter);
        recyclerView.setLayoutManager(new CustomGridLayoutManager(this));
        procesoAdapter.setOnItemClick(recyclerView,this);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(this);

        toggle =  new ActionBarDrawerToggle(this,drawer
                ,R.string.drawer_open, R.string.drawer_close);


        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);

        ImageView userImage = (ImageView) nav.getHeaderView(0).findViewById(R.id.img_user);
        TextView userName = (TextView) nav.getHeaderView(0).findViewById(R.id.txt_usr);
        TextView userMail = (TextView) nav.getHeaderView(0).findViewById(R.id.txt_mail);
        userName.setText(preferences.getString(KEY_USER_NAME,""));
        userMail.setText(preferences.getString(KEY_USER_MAIL,""));
        String urlImage = preferences.getString(AppUtil.USER_IMG,"");

        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();

        Picasso.with(this).load(Uri.parse("http://tusejemplos.com/wp-content/uploads/2014/12/url-300x240.png"))
                .transform(transformation)
                .into(userImage);

        if(usuarioLogin.equals("-1")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.general_profile_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        procesosCon.getAllProcess(usuarioLogin);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_logout:

                editor.putString(KEY_USER, "-1");
                editor.commit();
                Intent intent =  new Intent(this, LoginActivity.class);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        swipe.setRefreshing(false);
        if(v.getId() == R.id.fab){
            Intent intent = new Intent(this, NewProcessActivity.class);
            startActivity(intent);
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
         procesosCon.getAllProcess(usuarioLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        procesosCon.getAllProcess(usuarioLogin);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ProcessActivity.class);
        intent.putExtra(ProcessActivity.KEY_ID,dataProcces.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onReadProcessCompleted(List<Proceso> result) {
        dataProcces.clear();
        for(int i=0;i<result.size();i++){
            dataProcces.add(result.get(i));
        }
        procesoAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    public void onRegisterProcessCompleted() {

    }

    @Override
    public void onRefresh() {
        procesosCon.getAllProcess(usuarioLogin);
    }
}

