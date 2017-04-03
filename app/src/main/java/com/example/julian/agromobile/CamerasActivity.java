package com.example.julian.agromobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julian.agromobile.adapters.CameraAdapter;
import com.example.julian.agromobile.models.Camara;
import com.example.julian.agromobile.net.CamarasCon;

import java.util.ArrayList;
import java.util.List;

public class CamerasActivity extends AppCompatActivity implements CamarasCon.CamaraConI, AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    ListView camaras;
    FloatingActionButton fabcameras;
    List<Camara> dataCamaras;
    CameraAdapter cameraAdapter;
    CamarasCon camarasCon;
    String idUser;
    SwipeRefreshLayout swipe;
    TextView emptyView;

    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameras);
        camaras = (ListView) findViewById(R.id.list_camaras);
        fabcameras = (FloatingActionButton) findViewById(R.id.fab_cameras);
        emptyView = (TextView) findViewById(R.id.empty_view_cameras);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_camaras);
        swipe.setColorSchemeColors(Color.argb(0xff, 0xff, 0x00, 0x00)
                , Color.argb(0xff, 0x00, 0xff, 0x00)
                , Color.argb(0xff, 0x00, 0x00, 0xff));

        swipe.setOnRefreshListener(this);
        Bundle extras = getIntent().getExtras();
        idUser = extras.getString(LoginActivity.KEY_USER_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.txt_cameras);
        fabcameras.setOnClickListener(this);
        camarasCon = new CamarasCon(this, this);
        camarasCon.getAllCameras();

        dataCamaras = new ArrayList<Camara>();
        cameraAdapter = new CameraAdapter(this, dataCamaras);
        camaras.setAdapter(cameraAdapter);
        camaras.setOnItemClickListener(this);
        registerForContextMenu(camaras);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_de_aeronaves, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos;
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        pos = info.position;

        switch (item.getItemId()) {

            case R.id.action_delete:
                camarasCon.delete(dataCamaras.get(pos));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_cameras) {
            Intent intent = new Intent(this, AddCameraActivity.class);
            intent.putExtra(LoginActivity.KEY_USER_NAME, idUser);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        camarasCon.getAllCameras();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        camarasCon.getAllCameras();
        super.onRestart();
    }

    @Override
    public void onReadCameraCompleted(List<Camara> result) {
        dataCamaras.clear();
        System.out.println("holaaa");
        if (result.size() < 1) {
            System.out.println(result.size());
            emptyView.setVisibility(View.VISIBLE);
            //camaras.setVisibility(View.INVISIBLE);
            System.out.println("visible");
        }
        for (int i = 0; i < result.size(); i++) {
            dataCamaras.add(result.get(i));
            emptyView.setVisibility(View.INVISIBLE);
            // camaras.setVisibility(View.GONE);
            System.out.println("invisible");
        }
        cameraAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
        System.out.println("holaaa");
    }

    @Override
    public void onRegisterCameraCompleted() {

    }

    @Override
    public void onUpdateCameraCompleted() {

    }

    @Override
    public void onDeleteCameraCompleted() {
        camarasCon.getAllCameras();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        camarasCon.getAllCameras();
    }
}
