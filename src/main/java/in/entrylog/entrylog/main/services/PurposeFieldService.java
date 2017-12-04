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
 * Created by Admin on 08-Apr-17.
 */

public class PurposeFieldService extends Service{
    public static final String PREFS_NAME = "MyPrefsFile";
    DetailsValue detailsValue;
    ConnectingTask task;
    Thread staffthread;
    SharedPreferences settings;
    String OrganizationID;
    public static HashSet<String> staffset1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        staffset1 = new HashSet<String>();
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("debug", "Staff Service Started");
        OrganizationID = settings.getString("OrganizationID", "");
        ConnectingTask.PurposeStaffFetching staffFetching = task.new PurposeStaffFetching(OrganizationID, detailsValue, staffset1);
        staffFetching.execute();
        staffthread = null;
        Runnable runnable = new PurposeFieldService.StaffData();
        staffthread = new Thread(runnable);
        staffthread.start();

        return Service.START_STICKY;
    }

    class StaffData implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Fetchstaff();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    private void Fetchstaff() {
        if (detailsValue.isPurposeStaffExists()) {
            detailsValue.setPurposeStaffExists(false);
            staffthread.interrupt();
            this.stopSelf();
        }
        if (detailsValue.isNoPurposeStaffExist()) {
            detailsValue.setNoPurposeStaffExist(false);
            staffthread.interrupt();
            this.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        if (staffthread.isAlive()) {
            staffthread.interrupt();
            this.stopSelf();
        }
    }
}
