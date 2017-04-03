package com.example.julian.agromobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.julian.agromobile.adapters.AircraftAdapter;
import com.example.julian.agromobile.models.Aeronave;
import com.example.julian.agromobile.net.AeronavesCon;

import java.util.ArrayList;
import java.util.List;

public class AeronavesActivity extends AppCompatActivity implements  AeronavesCon.AeronaveConI, AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    ListView aeronaves;
    FloatingActionButton fabaircrafts;
    List<Aeronave> dataAeronaves;
    AircraftAdapter aircraftAdapter;
    AeronavesCon aeronavesCon;
    String idUser;
    SwipeRefreshLayout swipe;
    TextView emptyView;

    boolean azureLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeronaves);
        aeronaves= (ListView) findViewById(R.id.list_aeronaves);
        fabaircrafts = (FloatingActionButton) findViewById(R.id.fab_aircrafts);
        emptyView = (TextView) findViewById(R.id.empty_view_aircrafts);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_aircafts);
        swipe.setColorSchemeColors(Color.argb(0xff, 0xff, 0x00, 0x00)
                , Color.argb(0xff, 0x00, 0xff, 0x00)
                , Color.argb(0xff, 0x00, 0x00, 0xff));

        swipe.setOnRefreshListener(this);
        Bundle extras = getIntent().getExtras();
        idUser=extras.getString(LoginActivity.KEY_USER_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_aircraft);
        fabaircrafts.setOnClickListener(this);
        aeronavesCon= new AeronavesCon(this,this);
        aeronavesCon.getAircraftByIdUser(idUser);

        dataAeronaves = new ArrayList<Aeronave>();
        aircraftAdapter = new AircraftAdapter(this,dataAeronaves);
        aeronaves.setAdapter(aircraftAdapter);
        aeronaves.setOnItemClickListener(this);
        registerForContextMenu(aeronaves);

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

        switch (item.getItemId()){

            case R.id.action_delete:
                aeronavesCon.delete(dataAeronaves.get(pos));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab_aircrafts){
            Intent intent = new Intent(this, AddUAVActivity.class);
            intent.putExtra(LoginActivity.KEY_USER_NAME,idUser);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        aeronavesCon.getAircraftByIdUser(idUser);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        aeronavesCon.getAircraftByIdUser(idUser);
        super.onRestart();
    }

    @Override
    public void onReadAircraftCompleted(List<Aeronave> result) {
        dataAeronaves.clear();
        if(result.size()<1){
           System.out.println(result.size());
              emptyView.setVisibility(View.VISIBLE);
            //aeronaves.setVisibility(View.INVISIBLE);
            System.out.println("visible");
        }
        for(int i=0;i<result.size();i++){
            dataAeronaves.add(result.get(i));
            emptyView.setVisibility(View.INVISIBLE);
           // aeronaves.setVisibility(View.GONE);
            System.out.println("invisible");
        }
        aircraftAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    public void onRegisterAircraftCompleted() {

    }

    @Override
    public void onUpdateAircraftCompleted() {

    }

    @Override
    public void onDeleteAircraftCompleted() {
        aeronavesCon.getAircraftByIdUser(idUser);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        aeronavesCon.getAircraftByIdUser(idUser);

    }
}
