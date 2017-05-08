package com.example.julian.agromobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ResultadosActivity extends AppCompatActivity {


    ImageView image_resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        image_resultado = (ImageView) findViewById(R.id.image_resultados);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setTitle("Resultados");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Picasso.with(this).load(extras.getString(ProcessActivity.IMG_RESULTADO)).into(image_resultado);
        }catch(Exception e){
            System.out.println("no se pudo agregar la imagen");
        }
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
}
