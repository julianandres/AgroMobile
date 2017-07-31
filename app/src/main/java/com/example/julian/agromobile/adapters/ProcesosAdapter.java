package com.example.julian.agromobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julian.agromobile.R;
import com.example.julian.agromobile.models.Proceso;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by JULIAN on 04/12/2016.
 */

public class ProcesosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnCreateContextMenuListener {

    static final int VIEW_SPAN = 0;
    static final int VIEW_NOSPAN = 1;
    Context context;
    List<Proceso> data;
    RecyclerView recyclerView;
    OnItemClick onItemClick;
    public ProcesosAdapter(Context context,List<Proceso> data) {
        this.context = context;
        this.data = data;
    }
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if(viewType == VIEW_SPAN){
            View v = View.inflate(context, R.layout.template_process, null);
            v.setOnClickListener(this);
            viewHolder =  new ProcesoSpanViewHolder(v);
        }else {
            View v = View.inflate(context, R.layout.template_process, null);
            v.setOnClickListener(this);
            viewHolder =  new ProcesoViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Proceso p = data.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
        if(holder instanceof ProcesoSpanViewHolder ){
            ProcesoSpanViewHolder spanHolder = (ProcesoSpanViewHolder) holder;
            spanHolder.nombre.setText(p.getNombre());
            spanHolder.fecha.setText(formatDate(p.getFechaInicio()));

            if(p.isState()){
                spanHolder.estado.setText(context.getString(R.string.txt_onprocess));
                spanHolder.estado.setBackgroundResource(R.color.onProccesColor);
            }else{
                spanHolder.estado.setText(context.getString(R.string.txt_finished));
                spanHolder.estado.setBackgroundResource(R.color.offProccessColor);
            }


           // Picasso.with(context).load(Uri.parse(p.getUrlImg())).into(spanHolder.img);

        }else {
            ProcesoViewHolder pHolder = (ProcesoViewHolder) holder;
            pHolder.nombre.setText(p.getNombre());

            pHolder.fecha.setText(formatDate(p.getFechaInicio()));

            if(p.isState()){
                pHolder.estado.setText(context.getString(R.string.txt_onprocess));
                pHolder.estado.setBackgroundResource(R.color.onProccesColor);
            }else{
                pHolder.estado.setText(context.getString(R.string.txt_finished));
                pHolder.estado.setBackgroundResource(R.color.offProccessColor);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position%3 == 0)
            return VIEW_SPAN;
        else
            return VIEW_NOSPAN;
    }

    public void setOnItemClick(RecyclerView recyclerView, OnItemClick onItemClick) {
        this.recyclerView = recyclerView;
        this.onItemClick = onItemClick;
        recyclerView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        onItemClick.onItemClick(position);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //menuInfo is null
        menu.add(Menu.NONE, R.id.action_delete,
                Menu.NONE, "Eliminar");
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
    //endregion

    //region OnItemClick Event

    //region ViewHolders
    public class ProcesoSpanViewHolder extends RecyclerView.ViewHolder{

        TextView nombre,estado,fecha;

        public ProcesoSpanViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.template_title);
            estado = (TextView) itemView.findViewById(R.id.template_state);
            fecha = (TextView) itemView.findViewById(R.id.template_date);

        }
    }

    public class ProcesoViewHolder extends RecyclerView.ViewHolder{

        TextView nombre,estado,fecha;

        public ProcesoViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.template_title);
            estado = (TextView) itemView.findViewById(R.id.template_state);
            fecha = (TextView) itemView.findViewById(R.id.template_date);
        }
    }
    //endregion
    String formatDate(Date imput){
        String fecha="";
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        fecha =formatoFecha.format(imput)+context.getString(R.string.tothe)+formatoHora.format(imput);
        return fecha;
    }
}
