package com.example.julian.agromobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

public class NewProcessActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private RadioGroup rdgGrupo;
    private int recurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_process);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rdgGrupo = (RadioGroup)findViewById(R.id.recurrence);
        rdgGrupo.setOnCheckedChangeListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        finish();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdbOne){
            recurrency=1;
        }else if (checkedId == R.id.rdbTwo){
            recurrency=2;
        }else if (checkedId == R.id.rdbThree){
           recurrency=3;
        }
    }
}
