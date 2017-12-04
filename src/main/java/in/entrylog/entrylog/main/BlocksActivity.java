package in.entrylog.entrylog.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.dataposting.ConnectingTask.CheckUpdatedApk;
import in.entrylog.entrylog.dataposting.ConnectingTask.LogoutUser;
import in.entrylog.entrylog.dataposting.ConnectingTask.SmartCheckinout;
import in.entrylog.entrylog.main.adhocVisitors.AdhocVisitorActivity;
import in.entrylog.entrylog.main.apponitments.Appointments;
import in.entrylog.entrylog.main.bluetooth.AddVisitor_Bluetooth;
import in.entrylog.entrylog.main.el101_102.AddVisitors_EL101;
import in.entrylog.entrylog.main.el201.AddVisitors_EL201;
import in.entrylog.entrylog.main.services.ContractorService;
import in.entrylog.entrylog.main.services.FieldsService;
import in.entrylog.entrylog.main.services.PrintingService;
import in.entrylog.entrylog.main.services.PurposeFieldService;
import in.entrylog.entrylog.main.services.StaffService;
import in.entrylog.entrylog.main.services.StaffServiceAddVisitor;
import in.entrylog.entrylog.serialprinter.SerialPrinter;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL101_102;
import in.entrylog.entrylog.values.FunctionCalls;
import in.entrylog.entrylog.values.IMEIFunctionCalls;

public class BlocksActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int DEVICE_DLG = 1;
    private static final int ABOUTUS_DLG = 2;
    private static final int UPDATES_DLG = 3;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    Button AddVisitors_btn, Checkout_btn, Visitors_btn, Appointments_btn;
    TextView tv_app_version;
    String OrganizationID, OrganizationName, GuardID, User, UpdateApkURL="", Apkfile="", Serverapkversion="", Appversion="",
            OverNightTime="";
    SerialPrinter printer;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ConnectingTask task;
    Thread logoutthread, updatethread, adhocthread;
    DetailsValue detailsValue;
    private boolean appointmentsbtn = false, addvisitorsbtn = false, visitorsbtn = false, checkoutbtn = false,
            updatefound = false, appdownloaded = false, el101_enabled = false, adhocvisitor=false;
    static ProgressDialog dialog = null, dialog1=null;
    FunctionCalls functionCalls;
    IMEIFunctionCalls imeiFunctionCalls;
    EL101_102 el101_102device;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;
    String AdhocPass;



    static String AddPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_entrylog_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_blocks);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        OrganizationID = settings.getString("OrganizationID", "");
        GuardID = settings.getString("GuardID", "");
        OrganizationName = settings.getString("OrganizationName", "");
        User = settings.getString("User", "");
        OverNightTime = settings.getString("OverNightTime", "");

        task = new ConnectingTask();
        printer = new SerialPrinter();
        detailsValue = new DetailsValue();
        functionCalls = new FunctionCalls();
        imeiFunctionCalls = new IMEIFunctionCalls();
        el101_102device = new EL101_102();

        AddVisitors_btn = (Button) findViewById(R.id.addvisitors_btn);
        Visitors_btn = (Button) findViewById(R.id.visitors_btn);
        Checkout_btn = (Button) findViewById(R.id.checkout_btn);
        Appointments_btn = (Button) findViewById(R.id.manually_checkout_btn);
        tv_app_version = (TextView) findViewById(R.id.app_version);

        el101_enabled = el101_102device.EnablePrinter(true);

        Intent service = new Intent(BlocksActivity.this, FieldsService.class);
        startService(service);
        Intent service1 = new Intent(BlocksActivity.this, PrintingService.class);
        startService(service1);
        Intent service2 = new Intent(BlocksActivity.this, StaffService.class);
        startService(service2);
        Intent service3 = new Intent(BlocksActivity.this, StaffServiceAddVisitor.class);
        startService(service3);
        Intent service4 = new Intent(BlocksActivity.this, ContractorService.class);
        startService(service4);
        Intent service5 = new Intent(BlocksActivity.this, PurposeFieldService.class);
        startService(service5);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!OverNightTime.equals("")) {
                    int time = Integer.parseInt(OverNightTime);
                    functionCalls.startReceiver(BlocksActivity.this, time);
                }
            }
        }, 5000);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pInfo.versionName;
        tv_app_version.setText("VER: "+version);

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

        tv_app_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AddVisitors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    if (settings.getString("Printertype", "").equals("")) {
                        addvisitorsbtn = true;
                        showdialog(DEVICE_DLG);
                    } else if (settings.getString("Printertype", "").equals("Bluetooth")) {
                        Toast.makeText(BlocksActivity.this, "Bluetooth with "+settings.getString("Device", ""),
                                Toast.LENGTH_SHORT).show();
                        addVisitors(AddVisitor_Bluetooth.class);
                    } else if (!settings.getString("Printertype", "").equals("Bluetooth")) {
                        if (settings.getString("Device", "").equals("EL101")) {
                            Toast.makeText(BlocksActivity.this, "EL101", Toast.LENGTH_SHORT).show();
                            addVisitors(AddVisitors_EL101.class);
                            /*if (el101_enabled) {
                                Toast.makeText(BlocksActivity.this, "EL101", Toast.LENGTH_SHORT).show();
                                addVisitors(AddVisitors_EL101.class);
                            } else {
                                Toast.makeText(BlocksActivity.this, "EL101/102 device will not support for your device..",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                        } else if (settings.getString("Device", "").equals("EL201")) {
                            addVisitors(AddVisitors_EL201.class);
                            /*if (Build.MANUFACTURER.equals("LS888")) {
                                Toast.makeText(BlocksActivity.this, "EL201", Toast.LENGTH_SHORT).show();
                                addVisitors(AddVisitors_EL201.class);
                            } else {
                                Toast.makeText(BlocksActivity.this, "EL201 device will not support for your device..",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    if (settings.getString("Printertype", "").equals("")) {
                        checkoutbtn = true;
                        showdialog(DEVICE_DLG);
                    } else {
                        checkoutVisitors(CheckoutVisitors.class);
                    }
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Visitors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    if (settings.getString("Printertype", "").equals("")) {
                        visitorsbtn = true;
                        showdialog(DEVICE_DLG);
                    } else {
                        visitors(Visitors.class, "Visitors");
                    }
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Appointments_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    if (settings.getString("Printertype", "").equals("")) {
                        appointmentsbtn = true;
                        showdialog(DEVICE_DLG);
                    } else {
                        visitors(Appointments.class, "Appointments");
                    }
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        MenuItem bedMenuItem = menu.findItem(R.id.menu_user_name);
        bedMenuItem.setTitle(User);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_aboutus:
                showdialog(ABOUTUS_DLG);
                break;

            case R.id.menu_updates:
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    CheckUpdatedApk checkUpdatedApk = task.new CheckUpdatedApk(detailsValue);
                    checkUpdatedApk.execute();
                    dialog = ProgressDialog.show(BlocksActivity.this, "", "Checking for App Updates..", true);
                    updatethread = null;
                    Runnable updaterunnable = new Updatetimer();
                    updatethread = new Thread(updaterunnable);
                    updatethread.start();
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_adhoc:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Organization Details");
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.addhoc_dialogview, null);
                builder.setView(ll);
                builder.setCancelable(false);
                final EditText adhoc_password= (EditText) ll.findViewById(R.id.dialogpassword_etTxt);
                adhoc_password.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

                adhoc_password.setImeOptions(EditorInfo.IME_ACTION_DONE);

                builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        if (!adhoc_password.getText().toString().equals("")) {
                            adhocvisitor=true;
                            AdhocPass= adhoc_password.getText().toString();
                            ConnectingTask.Addhoc_user addhocUser = task.new Addhoc_user(AdhocPass, OrganizationID, BlocksActivity.this, detailsValue);
                            addhocUser.execute();
                           // Log.d("debug", "Stated");
                            dialog= ProgressDialog.show(BlocksActivity.this, "", "Logging in please wait..", true);
                            //adhocstatus();
                            adhocthread = null;
                            Runnable runnable = new Adhoctimer();
                            adhocthread = new Thread(runnable);
                            adhocthread.start();

                        } else {
                           // adhoc_password.setError("Entered Organization Password");
                            adhoc_password.setText("");
                            functionCalls.showToast(BlocksActivity.this,
                                    "Enter Organization Password");
                        }
                    }
                });

                //done keypad button in Dialog button
                /*adhoc_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if (!adhoc_password.getText().toString().equals("")) {
                            adhocvisitor=true;
                            AdhocPass= adhoc_password.getText().toString();
                            ConnectingTask.Addhoc_user addhocUser = task.new Addhoc_user(AdhocPass, OrganizationID, BlocksActivity.this, detailsValue);
                            addhocUser.execute();
                            Log.d("debug", "Stated");
                            dialog= ProgressDialog.show(BlocksActivity.this, "", "Logging in please wait..", true);
                            //adhocstatus();
                            adhocthread = null;
                            Runnable runnable = new Adhoctimer();
                            adhocthread = new Thread(runnable);
                            adhocthread.start();

                        } else {
                            // adhoc_password.setError("Entered Organization Password");
                            adhoc_password.setText("");
                            functionCalls.showToast(BlocksActivity.this,
                                    "Enter Organization Password");
                        }

                        dialog.dismiss();
                        dialog.cancel();
                        return true;
                    }

                });*/

                builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent sameIntent = new Intent(BlocksActivity.this,
                                BlocksActivity.class);
                        startActivity(sameIntent);
                        finish();
                    }
                });
                AlertDialog adhocwindowAlert = builder.create();
                adhocwindowAlert.show();
                break;

            case R.id.menu_settings:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                /*startActivity(new Intent("android.settings.NFC_SETTINGS"));*/
                break;
          /*  case R.id.menu_timebound:
                Intent intn= new Intent(BlocksActivity.this, OverStay_Visitors.class);
                startActivity(intn);
                break;*/

            case R.id.menu_printer:
                showdialog(DEVICE_DLG);
                break;

            case R.id.menu_logout:
                if (functionCalls.isInternetOn(BlocksActivity.this)) {
                    LogoutUser logoutUser = task.new LogoutUser(GuardID, BlocksActivity.this, detailsValue);
                    logoutUser.execute();
                    dialog = ProgressDialog.show(BlocksActivity.this, "", "Logging Out please wait..", true);
                    logoutthread = null;
                    Runnable runnable = new Logouttimer();
                    logoutthread = new Thread(runnable);
                    logoutthread.start();
                } else {
                    Toast.makeText(BlocksActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showdialog(int id) {
        switch (id) {
            case DEVICE_DLG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Device");
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.deviceview, null);
                builder.setView(ll);
                final RadioButton bluetooth_radio = (RadioButton) ll.findViewById(R.id.bluetooth_radio);
                final RadioButton el101_radio = (RadioButton) ll.findViewById(R.id.el101_102_radio);
                final RadioButton el201 = (RadioButton) ll.findViewById(R.id.el201_radio);
                if (settings.getString("Device", "").equals("Bluetooth")) {
                    bluetooth_radio.setChecked(true);
                } else if (settings.getString("Device", "").equals("EL101")) {
                    el101_radio.setChecked(true);
                } else if (settings.getString("Device", "").equals("EL201")) {
                    el201.setChecked(true);
                }
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (bluetooth_radio.isChecked() == true) {
                            /*editor.putString("Device", "Bluetooth");
                            editor.commit();*/
                            if (addvisitorsbtn) {
                                addvisitorsbtn = false;
                                addVisitors(AddVisitor_Bluetooth.class);
                            } else if (appointmentsbtn) {
                                appointmentsbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn){
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(BlocksActivity.this, "Bluetooth", Toast.LENGTH_SHORT).show();
                            }
                        } else if (el101_radio.isChecked() == true) {
                            /*editor.putString("Device", "EL101");
                            editor.commit();*/
                            if (addvisitorsbtn) {
                                addvisitorsbtn = false;
                                addVisitors(AddVisitors_EL101.class);
                            } else if (appointmentsbtn) {
                                appointmentsbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn){
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(BlocksActivity.this, "EL101/102", Toast.LENGTH_SHORT).show();
                            }
                        } else if (el201.isChecked() == true) {
                            /*editor.putString("Device", "EL201");
                            editor.commit();*/
                            if (addvisitorsbtn) {
                                addvisitorsbtn = false;
                                addVisitors(AddVisitors_EL201.class);
                            } else if (appointmentsbtn) {
                                appointmentsbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn){
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(BlocksActivity.this, "EL201", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                AlertDialog devicealert = builder.create();
                devicealert.show();
                break;

            case ABOUTUS_DLG:
                AlertDialog.Builder aboutus = new AlertDialog.Builder(this);
                aboutus.setTitle("About Us");
                aboutus.setMessage("EntryLog.in is an innovative, smart visitor management system."+"\n"+"\n"
                        +"http://www.entrylog.in/"+"\n"+"\n"+"Contact: +91 80953-12121"+"\n"+"Email: support@entrylog.in");
                aboutus.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog aboutusalert = aboutus.create();
                aboutusalert.show();
                break;

            case UPDATES_DLG:
                AlertDialog.Builder appupdate = new AlertDialog.Builder(this);
                appupdate.setTitle("App Updates");
                if (updatefound) {
                    appupdate.setMessage("Your current version number : "+Appversion+
                            "\n"+"\n"+
                            "New version is available : "+Serverapkversion+"\n");
                    appupdate.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File apkFile = new File(functionCalls.checkapkfilepath(Apkfile));
                            if (apkFile.exists()) {
                                apkFile.delete();
                                DownloadLatestApk downloadLatestApk = new DownloadLatestApk();
                                downloadLatestApk.execute();
                                functionCalls.LogStatus("Apk file exists and deleted.. new file downloading necessary");
                            } else {
                                DownloadLatestApk downloadLatestApk = new DownloadLatestApk();
                                downloadLatestApk.execute();
                                functionCalls.LogStatus("Apk file doesn't exists downloading necessary");
                            }
                        }
                    });
                } else {
                    appupdate.setMessage("Your current version number : "+Appversion+" is up to date..");
                    appupdate.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updatethread.interrupt();
                        }
                    });
                }
                appupdate.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatethread.interrupt();
                    }
                });
                AlertDialog appupdatealert = appupdate.create();
                appupdatealert.show();
                if (updatefound) {
                    updatefound = false;
                    ((AlertDialog) appupdatealert).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void addVisitors(Class startclass) {
        Intent intent = new Intent(BlocksActivity.this, startclass);
        intent.putExtra("ID", OrganizationID);
        intent.putExtra("GuardID", GuardID);
        intent.putExtra("OrganizationName", OrganizationName);
        intent.putExtra("User", User);
        startActivity(intent);
    }

    private void checkoutVisitors(Class startclass) {
        Intent checkout = new Intent(BlocksActivity.this, startclass);
        checkout.putExtra("ID", OrganizationID);
        checkout.putExtra("GuardID", GuardID);
        startActivity(checkout);
    }

    private void visitors(Class startclass, String view) {
        Intent visitors = new Intent(BlocksActivity.this, startclass);
        visitors.putExtra("VIEW", view);
        startActivity(visitors);
    }

    class Logouttimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    logoutstatus();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void logoutstatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isLoginSuccess()) {
                        detailsValue.setLoginSuccess(false);
                        logoutthread.interrupt();
                        dialog.dismiss();
                        editor.putString("Login", "No");
                        editor.putString("Device", "");
                        editor.putString("OrganizationID", "");
                        editor.commit();
                        boolean deleted = functionCalls.deletefolder();
                        if (deleted) {
                            Log.d("debug", "Entrylog Folder deleted");
                        } else {
                            Log.d("debug", "Entrylog Folder not deleted");
                        }
                        if (!OverNightTime.equals("")) {
                            functionCalls.cancelReceiver(BlocksActivity.this);
                        }
                        Intent logoutIntent = new Intent();
                        setResult(Activity.RESULT_OK, logoutIntent);
                        finish();
                    }
                    if (detailsValue.isLoginFailure()) {
                        detailsValue.setLoginFailure(false);
                        dialog.dismiss();
                        Toast.makeText(BlocksActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                        logoutthread.interrupt();
                    }
                } catch (Exception e) {
                }
            }
        });
    }



    class Adhoctimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    adhocstatus();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }


    public void adhocstatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isAdhocLoginSuccess()) {
                        detailsValue.setAdhocLoginSuccess(false);
                        adhocthread.interrupt();
                        dialog.dismiss();
                        Log.d("debug", "started");
                        Intent logoutIntent = new Intent(BlocksActivity.this, AdhocVisitorActivity.class);
                        startActivity(logoutIntent);
                        Log.d("debug", "escaping");
                    }
                    if(detailsValue.isAdhocLoginFailure()){
                        detailsValue.setAdhocLoginFailure(false);
                        dialog.dismiss();
                        Log.d("debug", "incorrect password");
                        Toast.makeText(BlocksActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                        adhocthread.interrupt();
                    }

                } catch (Exception e) {
                }
            }
        });
    }


    class Updatetimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    updatestatus();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void updatestatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isApkfilexist()) {
                        detailsValue.setApkfilexist(false);
                        dialog.dismiss();
                        PackageInfo pInfo = null;
                        try {
                            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        Appversion = pInfo.versionName;
                        Apkfile = detailsValue.getApkfile();
                        UpdateApkURL = detailsValue.getApkdownloadUrl();
                        if (Apkfile.length() > 21) {
                            if (Apkfile.length() == 22) {
                                Serverapkversion = Apkfile.substring(Apkfile.length()-10, Apkfile.length()-4);
                            } else if (Apkfile.length() == 23) {
                                Serverapkversion = Apkfile.substring(Apkfile.length()-11, Apkfile.length()-4);
                            }
                        } else {
                            Serverapkversion = Apkfile.substring(Apkfile.length()-9, Apkfile.length()-4);
                        }
                        compare(Appversion, Serverapkversion);
                    }
                    if (appdownloaded) {
                        appdownloaded = false;
                        updatethread.interrupt();
                        File apkFile = new File(functionCalls.checkapkfilepath(Apkfile));
                        try {
                            if (apkFile.exists()) {
                                functionCalls.LogStatus("Apk file exist");
                                UpdateApp(apkFile);
                            }
                        } catch (ActivityNotFoundException e) {
                        }
                    }
                    String Message = "";
                    if (detailsValue.isSmartIn()) {
                        updatethread.interrupt();
                        detailsValue.setSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(BlocksActivity.this, Message);
                    }
                    if (detailsValue.isSmartOut()) {
                        updatethread.interrupt();
                        detailsValue.setSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(BlocksActivity.this, Message);
                    }
                    if (detailsValue.isSmartError()) {
                        updatethread.interrupt();
                        detailsValue.setSmartError(false);
                        dialog.dismiss();
                        Message = "Please Check again...";
                        functionCalls.smartCardStatus(BlocksActivity.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void UpdateApp(File Apkfile) {
        Uri path = Uri.fromFile(Apkfile);
        Intent objIntent = new Intent(Intent.ACTION_VIEW);
        objIntent.setDataAndType(path, "application/vnd.android.package-archive");
        objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(objIntent);
    }

    public void compare(String v1, String v2) {
        String s1 = functionCalls.normalisedVersion(v1);
        String s2 = functionCalls.normalisedVersion(v2);
        int cmp = s1.compareTo(s2);
        String cmpStr = cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
        if (cmpStr.equals("<")) {
            updatefound = true;
            showdialog(UPDATES_DLG);
        } else {
            showdialog(UPDATES_DLG);
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadLatestApk extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count = 0;

            try {
                URL url = new URL(UpdateApkURL);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lengthOfFile = conexion.getContentLength();
                Log.d("debug", "Length of file: " + lengthOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                File apkFile = new File(functionCalls.checkapkfilepath(Apkfile));
                if (apkFile.exists()) {
                    apkFile.delete();
                    functionCalls.LogStatus("Apk file exists and deleted it");
                } else {
                    functionCalls.LogStatus("Apk file doesn't exists");
                }
                OutputStream output = new FileOutputStream(functionCalls.apkfilepath()+ File.separator + Apkfile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lengthOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            appdownloaded = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, BlocksActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(BlocksActivity.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(BlocksActivity.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagcontent = getTextfromNdefRecord(ndefRecord);
            checkingout(tagcontent);
        } else {
            Toast.makeText(BlocksActivity.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextfromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {

        }
        return tagContent;
    }

    public void checkingout(String result) {
        SmartCheckinout checkOut = task.new SmartCheckinout(detailsValue, result,
                OrganizationID, GuardID);
        checkOut.execute();
        dialog = ProgressDialog.show(BlocksActivity.this, "", "Checking...", true);
        dialog.setCancelable(true);
        updatethread = null;
        Runnable runnable = new Updatetimer();
        updatethread = new Thread(runnable);
        updatethread.start();
    }
}