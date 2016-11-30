package com.example.julian.agromobile.net;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;


import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Julian on 20/05/2015.
 */
public abstract class AzureClient<T> implements TableOperationCallback<T>, TableDeleteCallback {
    MobileServiceClient client;
    public AzureClient(Context context){
        try {
            client=new MobileServiceClient("https://probemobileagro.azurewebsites.net",context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void insert(T item){
        getTable().insert(item,this);
    }
    public void update(T item){
        getTable().update(item, this);
    }
    public void delete(T item){
        getTable().delete(item, this);
    }

    public MobileServiceTable<T> getTable(){

        return client.getTable(getClassModel());
    }

    public abstract Class<T> getClassModel();

    @Override
    public abstract void onCompleted(T entity, Exception exception, ServiceFilterResponse response);

    @Override
    public abstract void onCompleted(Exception exception, ServiceFilterResponse response);
}