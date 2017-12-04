package in.entrylog.entrylog.main;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.UnsupportedEncodingException;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.dataposting.ConnectingTask.SmartCheckinout;
import in.entrylog.entrylog.dataposting.ConnectingTask.VisitorsCheckOut;
import in.entrylog.entrylog.main.bluetooth.AddVisitor_Bluetooth;
import in.entrylog.entrylog.main.el101_102.AddVisitors_EL101;
import in.entrylog.entrylog.main.el201.AddVisitors_EL201;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.FunctionCalls;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckoutVisitors extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final String PREFS_NAME = "MyPrefsFile";
    private ZXingScannerView mScannerView;
    DetailsValue detailsValue;
    ConnectingTask task;
    String OrganizationID, SecurityID, GuardID, OrganizationName;

    Thread mythread;
    ProgressDialog checkoutdialog = null;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    SharedPreferences settings;
    boolean nfcavailable = false;
    FunctionCalls functionCalls;


    private static final int DEVICE_DLG = 1;
    String User;
    private boolean manualcheckoutbtn = false, addvisitorsbtn = false, visitorsbtn = false, checkoutbtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_visitors);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        functionCalls = new FunctionCalls();

        OrganizationID = settings.getString("OrganizationID", "");
        SecurityID = settings.getString("GuardID", "");
    }


    private void showdialog(int id) {
        switch (id) {
            case DEVICE_DLG:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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
                            } else if (manualcheckoutbtn) {
                                manualcheckoutbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn) {
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(CheckoutVisitors.this, "Bluetooth", Toast.LENGTH_SHORT).show();
                            }
                        } else if (el101_radio.isChecked() == true) {
                            /*editor.putString("Device", "EL101");
                            editor.commit();*/
                            if (addvisitorsbtn) {
                                addvisitorsbtn = false;
                                addVisitors(AddVisitors_EL101.class);
                            } else if (manualcheckoutbtn) {
                                manualcheckoutbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn) {
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(CheckoutVisitors.this, "EL101/102", Toast.LENGTH_SHORT).show();
                            }
                        } else if (el201.isChecked() == true) {
                            /*editor.putString("Device", "EL201");
                            editor.commit();*/
                            if (addvisitorsbtn) {
                                addvisitorsbtn = false;
                                addVisitors(AddVisitors_EL201.class);
                            } else if (manualcheckoutbtn) {
                                manualcheckoutbtn = false;
                                visitors(Visitors.class, "Manually Checkout");
                            } else if (visitorsbtn) {
                                visitorsbtn = false;
                                visitors(Visitors.class, "Visitors");
                            } else if (checkoutbtn) {
                                checkoutbtn = false;
                                checkoutVisitors(CheckoutVisitors.class);
                            } else {
                                Toast.makeText(CheckoutVisitors.this, "EL201", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                android.support.v7.app.AlertDialog devicealert = builder.create();
                devicealert.show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manual_checkout, menu);
        MenuItem bedMenuItem = menu.findItem(R.id.menu_manual);
        bedMenuItem.setTitle(User);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_manual_checkout:
                if (functionCalls.isInternetOn(CheckoutVisitors.this)) {
                    if (settings.getString("Printertype", "").equals("")) {
                        manualcheckoutbtn = true;
                        showdialog(DEVICE_DLG);
                    } else {
                        visitors(Visitors.class, "Manually Checkout");
                    }
                } else {
                    Toast.makeText(CheckoutVisitors.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void visitors(Class startclass, String view) {
        Intent visitors = new Intent(CheckoutVisitors.this, startclass);
        visitors.putExtra("VIEW", view);
        startActivity(visitors);
    }


    private void addVisitors(Class startclass) {
        Intent intent = new Intent(CheckoutVisitors.this, startclass);
        intent.putExtra("ID", OrganizationID);
        intent.putExtra("GuardID", GuardID);
        intent.putExtra("OrganizationName", OrganizationName);
        intent.putExtra("User", User);
        startActivity(intent);
    }

    private void checkoutVisitors(Class startclass) {
        Intent checkout = new Intent(CheckoutVisitors.this, startclass);
        checkout.putExtra("ID", OrganizationID);
        checkout.putExtra("GuardID", GuardID);
        startActivity(checkout);
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);   // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, CheckoutVisitors.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    public void handleResult(Result result) {
        checkingout(result.getText().toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(CheckoutVisitors.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(CheckoutVisitors.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagcontent = getTextfromNdefRecord(ndefRecord);
            smartcheckingout(tagcontent);
        } else {
            Toast.makeText(CheckoutVisitors.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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

    class DisplayTimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Message = "";
                    String out = "";
                    if (detailsValue.isVisitorsCheckOutSuccess()) {
                        mythread.interrupt();
                        detailsValue.setVisitorsCheckOutSuccess(false);
                        checkoutdialog.dismiss();
                        Message = "Successfully Checked Out";
                        out = "Success";
                        functionCalls.ringtone(CheckoutVisitors.this);
                        createdialog(Message, out);
                    }
                    if (detailsValue.isVisitorsCheckOutFailure()) {
                        mythread.interrupt();
                        detailsValue.setVisitorsCheckOutFailure(false);
                        checkoutdialog.dismiss();
                        Message = "Checked Out Failed";
                        out = "Failure";
                        createdialog(Message, out);
                    }
                    if (detailsValue.isVisitorsCheckOutDone()) {
                        detailsValue.setVisitorsCheckOutDone(false);
                        checkoutdialog.dismiss();
                        Message = "Checked Out Already Done";
                        out = "Done";
                        createdialog(Message, out);
                    }
                    if (detailsValue.isSmartIn()) {
                        mythread.interrupt();
                        detailsValue.setSmartIn(false);
                        checkoutdialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(CheckoutVisitors.this, Message);
                    }
                    if (detailsValue.isSmartOut()) {
                        mythread.interrupt();
                        detailsValue.setSmartOut(false);
                        checkoutdialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(CheckoutVisitors.this, Message);
                    }
                    if (detailsValue.isSmartError()) {
                        mythread.interrupt();
                        detailsValue.setSmartError(false);
                        checkoutdialog.dismiss();
                        /*Message = "Checking Error.. Please Check again...";*/
                        Message = "Please check again...";
                        functionCalls.smartCardStatus(CheckoutVisitors.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void createdialog(String Message, String Checkout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutVisitors.this);
        builder.setTitle("CheckOut Result");
        builder.setMessage(Message);
        if (Checkout.equals("Success")) {
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        } else if (Checkout.equals("Failure")) {
            builder.setNeutralButton("ReScan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mScannerView.resumeCameraPreview(CheckoutVisitors.this);
                }
            });
        } else {
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void checkingout(String result) {
        VisitorsCheckOut checkOut = task.new VisitorsCheckOut(detailsValue, result,
                OrganizationID, SecurityID);
        checkOut.execute();
        checkoutdialog = ProgressDialog.show(CheckoutVisitors.this, "", "Checking Out...", true);
        mythread = null;
        Runnable runnable = new DisplayTimer();
        mythread = new Thread(runnable);
        mythread.start();
    }

    public void smartcheckingout(String result) {
        SmartCheckinout checkOut = task.new SmartCheckinout(detailsValue, result,
                OrganizationID, SecurityID);
        checkOut.execute();
        checkoutdialog = ProgressDialog.show(CheckoutVisitors.this, "", "Checking...", true);
        mythread = null;
        Runnable runnable = new DisplayTimer();
        mythread = new Thread(runnable);
        mythread.start();
    }
}
