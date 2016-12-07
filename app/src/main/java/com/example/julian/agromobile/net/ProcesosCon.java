package com.example.julian.agromobile.net;

import android.content.Context;

import com.example.julian.agromobile.models.Proceso;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by JULIAN on 07/12/2016.
 */

public class ProcesosCon extends AzureClient<Proceso> implements TableQueryCallback<Proceso> {

    ProcesoConI ProcesoConI;

    public ProcesosCon(ProcesoConI ProcesoConI, Context context) {
        super(context);
        this.ProcesoConI = ProcesoConI;
    }

    public void getAllProcess() {
        try {

            getTable().where().field("complete").eq(val(false)).execute().get();
            System.out.println("complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<Proceso> getClassModel() {
        return Proceso.class;
    }

    @Override
    public void onCompleted(Exception exception, ServiceFilterResponse response) {
        ProcesoConI.onDeleteComplete(exception, response);
    }

    @Override
    public void onCompleted(Proceso entity, Exception exception, ServiceFilterResponse response) {
        ProcesoConI.onComlete(entity, exception, response);
    }

    @Override
    public void onCompleted(List<Proceso> result, int count, Exception exception, ServiceFilterResponse response) {
        ProcesoConI.onReadCompleted(result, count, exception, response);
    }

    public interface ProcesoConI {
        void onReadCompleted(List<Proceso> result, int count, Exception exception, ServiceFilterResponse response);

        void onDeleteComplete(Exception exception, ServiceFilterResponse response);

        void onComlete(Proceso entity, Exception exception, ServiceFilterResponse response);
    }
}
