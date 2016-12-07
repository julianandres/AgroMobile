package com.example.julian.agromobile.net;

import android.content.Context;

import com.example.julian.agromobile.models.SubProceso;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by JULIAN on 07/12/2016.
 */

public class SubProcesosCon extends AzureClient<SubProceso> implements TableQueryCallback<SubProceso> {
    SubProcesoConI SubProcesoConI;

    public SubProcesosCon(SubProcesoConI SubProcesoConI, Context context) {
        super(context);
        this.SubProcesoConI = SubProcesoConI;
    }

    public void getAllSubProcess() {
        try {

            getTable().where().field("complete").eq(val(false)).execute().get();
            System.out.println("complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<SubProceso> getClassModel() {
        return SubProceso.class;
    }

    @Override
    public void onCompleted(Exception exception, ServiceFilterResponse response) {
        SubProcesoConI.onDeleteComplete(exception, response);
    }

    @Override
    public void onCompleted(SubProceso entity, Exception exception, ServiceFilterResponse response) {
        SubProcesoConI.onComlete(entity, exception, response);
    }

    @Override
    public void onCompleted(List<SubProceso> result, int count, Exception exception, ServiceFilterResponse response) {
        SubProcesoConI.onReadCompleted(result, count, exception, response);
    }

    public interface SubProcesoConI {
        void onReadCompleted(List<SubProceso> result, int count, Exception exception, ServiceFilterResponse response);

        void onDeleteComplete(Exception exception, ServiceFilterResponse response);

        void onComlete(SubProceso entity, Exception exception, ServiceFilterResponse response);
    }
}
