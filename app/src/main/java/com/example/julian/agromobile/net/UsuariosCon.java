package com.example.julian.agromobile.net;

import android.content.Context;

import com.example.julian.agromobile.models.Usuario;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by Julian on 20/05/2015.
 */
public class UsuariosCon extends AzureClient<Usuario> implements TableQueryCallback<Usuario> {

    public interface UsuarioConI{
         void onReadCompleted(List<Usuario> result, int count, Exception exception, ServiceFilterResponse response);
         void onDeleteComplete(Exception exception, ServiceFilterResponse response);
         void onComlete(Usuario entity, Exception exception, ServiceFilterResponse response);
    }

    UsuarioConI usuarioConI;


    public UsuariosCon(UsuarioConI usuarioConI,Context context) {
        super(context);
        this.usuarioConI=usuarioConI;
    }

    public void getAllUsers(){
        try {

            getTable().where().field("complete").eq(val(false)).execute().get();
            System.out.println("complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<Usuario> getClassModel() {
        return  Usuario.class;
    }

    @Override
    public void onCompleted(Exception exception, ServiceFilterResponse response) {
        usuarioConI.onDeleteComplete(exception,response);
    }

    @Override
    public void onCompleted(Usuario entity, Exception exception, ServiceFilterResponse response) {
        usuarioConI.onComlete(entity,exception,response);
    }

    @Override
    public void onCompleted(List<Usuario> result, int count, Exception exception, ServiceFilterResponse response) {
        usuarioConI.onReadCompleted(result,count,exception,response);
    }
}
