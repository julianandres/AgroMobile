package com.example.julian.agromobile.net;

import android.content.Context;
import android.widget.Toast;

import com.example.julian.agromobile.models.SubProceso;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by JULIAN on 07/12/2016.
 */

public class SubProcesosCon {

    MobileServiceClient client;
    Context con;
    List<SubProceso> result;
    SubProcesoConI SubProcesoConI;

    public SubProcesosCon(SubProcesoConI SubProcesoConI, Context context) {
        con = context;
        this.SubProcesoConI = SubProcesoConI;
        try {
            client = new MobileServiceClient("https://agromobile.azurewebsites.net", context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void insert(SubProceso item) {
        ListenableFuture<SubProceso> result = getTable().insert(item);
        Futures.addCallback(result, new FutureCallback<SubProceso>() {
            @Override
            public void onFailure(Throwable exc) {
                Toast.makeText(con, exc.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(SubProceso result) {
               // Toast.makeText(con, "Registro Completado" + result.getNombre(), Toast.LENGTH_SHORT).show();
                SubProcesoConI.onRegisterSubProcessCompleted();
                //TODO COLOCAR UN MENSAJE DE ERROR EL CUAL DIGA QUE EXISTIÓ UN ERROR EN EL REGISTRO
            }
        });
    }

    public void update(SubProceso item) {

        getTable().update(item);

    }

    public void delete(SubProceso item) {

        getTable().delete(item);
    }

    public MobileServiceTable<SubProceso> getTable() {
        return client.getTable(SubProceso.class);
    }

    public void getAllProcess() {
        try {
            ListenableFuture<MobileServiceList<SubProceso>> result = getTable().where().field("complete").eq(val(false)).execute();
            Futures.addCallback(result, new FutureCallback<List<SubProceso>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<SubProceso> result) {
                    //Toast.makeText(con, "Busqueda Completada con " + result.size(), Toast.LENGTH_SHORT).show();
                    SubProcesoConI.onReadSubProcessCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getProcessByIdProcess(String idProcess ){
        try {
            ListenableFuture<MobileServiceList<SubProceso>> result= getTable().where().field("idProceso").eq(val(idProcess)).execute();
            Futures.addCallback(result, new FutureCallback<List<SubProceso>>() {
                @Override
                public void onFailure(Throwable exc) {
                    Toast.makeText(con, exc.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<SubProceso> result) {
                    Toast.makeText(con, "Busqueda de procesos Completada con "+ result.size(), Toast.LENGTH_SHORT).show();
                    SubProcesoConI.onReadSubProcessCompleted(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<SubProceso> getClassModel() {
        return SubProceso.class;
    }

    public interface SubProcesoConI {
        public void onReadSubProcessCompleted(List<SubProceso> result);
        public void onRegisterSubProcessCompleted();
    }



}
