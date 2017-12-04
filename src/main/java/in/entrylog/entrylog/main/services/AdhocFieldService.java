package in.entrylog.entrylog.main.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.HashSet;

import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.values.DetailsValue;

/**
 * Created by Admin on 17-Mar-17.
 */

public class AdhocFieldService  extends Service {
    public static final String PREFS_NAME = "MyPrefsFile";
    DetailsValue detailsValue;
    ConnectingTask task;
    Thread fieldsthread;
    SharedPreferences settings;
    String OrganizationID;
    public static HashSet<String> fieldset;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        fieldset = new HashSet<String>();
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("debug", "Field Service Started");
        OrganizationID = settings.getString("OrganizationID", "");
        ConnectingTask.AdhocDisplayingFields displayingFields = task.new AdhocDisplayingFields(OrganizationID, detailsValue, fieldset);
        displayingFields.execute();
        fieldsthread = null;
        Runnable runnable = new AdhocFieldService.FieldsData();
        fieldsthread = new Thread(runnable);
        fieldsthread.start();

        return Service.START_STICKY;
    }

    class FieldsData implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Fetchfields();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    private void Fetchfields() {
        if (detailsValue.isAdhocFieldsexists()) {
            detailsValue.setAdhocFieldsexists(false);
            fieldsthread.interrupt();
            this.stopSelf();
        }
        if (detailsValue.isNoAdhocFieldsExists()) {
            detailsValue.setNoAdhocFieldsExists(false);
            fieldsthread.interrupt();
            this.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        if (fieldsthread.isAlive()) {
            fieldsthread.interrupt();
            this.stopSelf();
        }
    }
}
