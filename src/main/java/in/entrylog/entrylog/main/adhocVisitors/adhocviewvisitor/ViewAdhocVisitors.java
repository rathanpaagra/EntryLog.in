package in.entrylog.entrylog.main.adhocVisitors.adhocviewvisitor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.adapters.AdhocVisitorsAdapters;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.main.Search_Details;
import in.entrylog.entrylog.main.Visitors;
import in.entrylog.entrylog.main.adhocVisitors.adhoc_el201.Adhoc_Search_Details;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.FunctionCalls;

public class ViewAdhocVisitors extends AppCompatActivity implements View.OnClickListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;
    public static final int DATE_DLG = 2;
    public static final int VISITORS_DLG = 3;

    //region Declaration
    RecyclerView VisitorsView;
    ArrayList<DetailsValue> VisitorsList;
    AdhocVisitorsAdapters Visitorsadapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectingTask task;
    DetailsValue detailsValue;
    String Organization_ID, CheckingUser, CheckinDate = "", CheckoutDate = "", SearchName = "", SearchEmail = "",
            SearchMobile = "", SearchTomeet = "", SearchVehicle = "", Device = "", PrinterType = "";
    Button Search_btn, Checkin_btn, Checkout_btn, SearchByName_btn, SearchByMobile_btn, SearchByVehicle_btn, SearchByToMeet_btn,
            Reset_btn;
    boolean checkindate = false, checkoutdate = false, searchname = false, searchmobile = false, searchtomeet = false,
            searchvehicle = false, searchcheckin = false, searchcheckout = false, result = false;
    EditText et_SearchName, et_SearchEmail, et_SearchMobile, et_SearchTomeet, et_SearchVehicle;
    TextInputLayout Til_SearchName, Til_SearchEmail, Til_SearchMobile, Til_SearchTomeet, Til_SearchVehicle;
    ImageView Edit_Checkin, Edit_Checkout;
    TextView tv_CheckIndate, tv_CheckOutdate;
    LinearLayout CheckinDateLayout, CheckinLayout, CheckoutDateLayout, CheckoutLayout;
    int year, month, date;
    SharedPreferences settings;
    Thread visitorsthread;
    FunctionCalls functionCalls;

    static ProgressDialog dialog = null;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;
    SharedPreferences.Editor editor;
    //endregion

    String Organization_ID1, ContextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_entrylog_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_adhoc_view_visitors);

        Intent in = getIntent();
        Bundle bnd = in.getExtras();
        Organization_ID1 = bnd.getString("ID");
        ContextView = bnd.getString("VIEW");

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();


        Organization_ID = settings.getString("OrganizationID", "");
        CheckingUser = settings.getString("GuardID", "");
        Device = settings.getString("Device", "");
        PrinterType = settings.getString("Printertype", "");

        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        functionCalls = new FunctionCalls();
        functionCalls.OrientationView(ViewAdhocVisitors.this);
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        //region Buttons Initialization
        Search_btn = (Button) findViewById(R.id.adhoc_search_btn);
        Search_btn.setOnClickListener(this);
        Reset_btn = (Button) findViewById(R.id.adhoc_reset_btn);
        Reset_btn.setOnClickListener(this);
        Checkin_btn = (Button) findViewById(R.id.adhoc_checkindate_btn);
        Checkin_btn.setOnClickListener(this);
        Checkout_btn = (Button) findViewById(R.id.adhoc_checkoutdate_btn);
        Checkout_btn.setOnClickListener(this);
        SearchByName_btn = (Button) findViewById(R.id.adhoc_searchvisitor_name_btn);
        SearchByName_btn.setOnClickListener(this);
        SearchByMobile_btn = (Button) findViewById(R.id.adhoc_searchvisitor_mobile_btn);
        SearchByMobile_btn.setOnClickListener(this);
        SearchByVehicle_btn = (Button) findViewById(R.id.adhoc_searchvisitor_vehicle_btn);
        SearchByVehicle_btn.setOnClickListener(this);
        SearchByToMeet_btn = (Button) findViewById(R.id.adhoc_searchvisitor_tomeet_btn);
        SearchByToMeet_btn.setOnClickListener(this);
        //endregion

        //region CheckDates Linear Layout Initialization
        CheckinDateLayout = (LinearLayout) findViewById(R.id.adhoc_checkindate_layout);
        CheckinLayout = (LinearLayout) findViewById(R.id.adhoc_checkin_layout);
        CheckoutDateLayout = (LinearLayout) findViewById(R.id.adhoc_checkoutdate_layout);
        CheckoutLayout = (LinearLayout) findViewById(R.id.adhoc_checkout_layout);

        //region Check Date Edit Initialization
        Edit_Checkin = (ImageView) findViewById(R.id.adhoc_checkindate_edit);
        Edit_Checkin.setOnClickListener(this);
        Edit_Checkout = (ImageView) findViewById(R.id.adhoc_checkoutdate_edit);
        Edit_Checkout.setOnClickListener(this);
        //endregion

        //region Text Input Layout Initialization
        Til_SearchName = (TextInputLayout) findViewById(R.id.adhoc_searchvisitor_name_Til);
        Til_SearchMobile = (TextInputLayout) findViewById(R.id.adhoc_searchvisitor_mobile_Til);
        Til_SearchTomeet = (TextInputLayout) findViewById(R.id.adhoc_searchvisitor_tomeet_Til);
        Til_SearchVehicle = (TextInputLayout) findViewById(R.id.adhoc_searchvisitor_vehicle_Til);
        //endregion

        //region Edit Text Initialization
        et_SearchName = (EditText) findViewById(R.id.adhoc_searchvisitor_name);
        et_SearchMobile = (EditText) findViewById(R.id.adhoc_searchvisitor_mobile);
        et_SearchTomeet = (EditText) findViewById(R.id.adhoc_searchvisitor_tomeet);
        et_SearchVehicle = (EditText) findViewById(R.id.adhoc_searchvisitor_vehicle);
        //endregion

        //region TextView Initialization
        tv_CheckIndate = (TextView) findViewById(R.id.adhoc_appointmentdate_Txt);
        tv_CheckOutdate = (TextView) findViewById(R.id.adhoc_checkoutdate_Txt);
        //endregion

        //region RecyclerView with Adapter
        StaggeredRotationChanged();
        VisitorsView = (RecyclerView) findViewById(R.id.adhoc_visitorsview);
        VisitorsList = new ArrayList<DetailsValue>();
        Visitorsadapter = new AdhocVisitorsAdapters(VisitorsList, ViewAdhocVisitors.this, ContextView, Organization_ID, CheckingUser,
                Device, PrinterType);

        VisitorsView.setHasFixedSize(true);
        VisitorsView.setLayoutManager(layoutManager);
        VisitorsView.setAdapter(Visitorsadapter);
        //endregion

        //visitorsDetails();
        adhocvisitordetails();


        et_SearchMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                int test = number.length();
                if (test >= 1) {
                    String trimnumber = number.substring(0, 1);
                    int num = Integer.parseInt(trimnumber);
                    if (num == 7 || num == 8 || num == 9) {

                    } else {
                        et_SearchMobile.setText("");
                    }
                }
            }
        });

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

    }

    private void adhocvisitordetails() {

        ConnectingTask.AllAdhocVisitors checkVisitors = task.new AllAdhocVisitors(VisitorsList, Visitorsadapter, detailsValue, Organization_ID, ViewAdhocVisitors.this);
        checkVisitors.execute();
        VisitorsList.clear();
        dialog = ProgressDialog.show(ViewAdhocVisitors.this, "", "Searching for a Adhoc Visitors..", true);
        visitorsthread = null;
        Runnable runnable = new ViewAdhocVisitors.VisitorsTimer();
        visitorsthread = new Thread(runnable);
        visitorsthread.start();

    }

    private void StaggeredRotationChanged() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation) {
                    case Surface.ROTATION_0:
                        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case Surface.ROTATION_90:
                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case Surface.ROTATION_270:
                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        break;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                break;
            default:
                layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                reload();
            }
        }
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void searchbtn() {
        if (Search_btn.getVisibility() != View.VISIBLE) {
            Search_btn.setVisibility(View.VISIBLE);
            Reset_btn.setVisibility(View.VISIBLE);
            VisitorsView.setVisibility(View.GONE);
        }
    }


    class VisitorsTimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    visiting();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void visiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isAdhocVisitorsFound()) {
                        detailsValue.setAdhocVisitorsFound(false);
                        dialog.dismiss();
                        visitorsthread.interrupt();
                    }
                    if (detailsValue.isNoAdhocVisitorsFound()) {
                        detailsValue.setNoAdhocVisitorsFound(false);
                        dialog.dismiss();
                        visitorsthread.interrupt();
                        showdialog(VISITORS_DLG);
                    }
                    String Message = "";
                    if (detailsValue.isAdhocSmartIn()) {
                        visitorsthread.interrupt();
                        detailsValue.setAdhocSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        smartCardStatus(ViewAdhocVisitors.this, Message);
                    }
                    if (detailsValue.isAdhocSmartOut()) {
                        visitorsthread.interrupt();
                        detailsValue.setAdhocSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        smartCardStatus(ViewAdhocVisitors.this, Message);
                    }
                    if (detailsValue.isAdhocSmartError()) {
                        visitorsthread.interrupt();
                        detailsValue.setAdhocSmartError(false);
                        dialog.dismiss();
                        /*Message = "Checking Error.. Please swipe again..";*/
                        Message = "Adhoc Visitor is Expired......";
                        functionCalls.smartCardStatus(ViewAdhocVisitors.this, Message);
                    }
                    if (detailsValue.isAdhocSmartInvalidCard()) {
                        visitorsthread.interrupt();
                        detailsValue.setAdhocSmartInvalidCard(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Please Check in Again......";
                        functionCalls.smartCardStatus(ViewAdhocVisitors.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    protected void showdialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DLG:
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dp = new DatePickerDialog(ViewAdhocVisitors.this, dpd, year, month, date);
               // dp.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog = dp;
                dialog.show();
                break;

            case VISITORS_DLG:
                AlertDialog.Builder novisitors = new AlertDialog.Builder(this);
                novisitors.setTitle("Visitor Details");
                novisitors.setCancelable(false);
                novisitors.setMessage("No Visitors Found to display..");
                novisitors.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertdialog = novisitors.create();
                alertdialog.show();
                break;
        }
    }

    public DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Starttime = new SimpleDateFormat("dd/MM/yyyy").parse(("" + dayOfMonth + "/" + "" + (monthOfYear + 1) + "/" + "" + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateselected = sdf.format(Starttime);
            if (checkindate) {
                checkindate = false;
                Checkin_btn.setVisibility(View.GONE);
                CheckinLayout.setVisibility(View.VISIBLE);
                CheckinDate = dateselected;
                tv_CheckIndate.setText(dateselected);
                searchcheckin = true;
            } else if (checkoutdate) {
                checkoutdate = false;
                Checkout_btn.setVisibility(View.GONE);
                CheckoutLayout.setVisibility(View.VISIBLE);
                CheckoutDate = dateselected;
                tv_CheckOutdate.setText(dateselected);
                searchcheckout = true;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adhoc_checkindate_btn:
                CheckinDate = "";
                searchbtn();
                checkindate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.adhoc_checkoutdate_btn:
                CheckoutDate = "";
                searchbtn();
                checkoutdate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.adhoc_checkindate_edit:
                checkindate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.adhoc_checkoutdate_edit:
                checkoutdate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.adhoc_searchvisitor_name_btn:
                SearchName = "";
                SearchByName_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchname = true;
                if (Til_SearchName.getVisibility() != View.VISIBLE) {
                    Til_SearchName.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.adhoc_searchvisitor_mobile_btn:
                SearchMobile = "";
                SearchByMobile_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchmobile = true;
                if (Til_SearchMobile.getVisibility() != View.VISIBLE) {
                    Til_SearchMobile.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.adhoc_searchvisitor_tomeet_btn:
                SearchTomeet = "";
                SearchByToMeet_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchtomeet = true;
                if (Til_SearchTomeet.getVisibility() != View.VISIBLE) {
                    Til_SearchTomeet.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.adhoc_searchvisitor_vehicle_btn:
                SearchVehicle = "";
                SearchByVehicle_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchvehicle = true;
                if (Til_SearchVehicle.getVisibility() != View.VISIBLE) {
                    Til_SearchVehicle.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.adhoc_reset_btn:
                fullreset();
                break;

            case R.id.adhoc_search_btn:
                for (int i = 0; i < 6; i++) {
                    if (i == 0) {
                        if (searchname) {
                            if (!et_SearchName.getText().toString().equals("")) {
                                SearchName = et_SearchName.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Enter Search Name", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 1) {
                        if (searchmobile) {
                            if (!et_SearchMobile.getText().toString().equals("")) {
                                SearchMobile = et_SearchMobile.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Enter Search Mobile", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 2) {
                        if (searchtomeet) {
                            if (!et_SearchTomeet.getText().toString().equals("")) {
                                SearchTomeet = et_SearchTomeet.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Enter Search ToMeet", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 3) {
                        if (searchvehicle) {
                            if (!et_SearchVehicle.getText().toString().equals("")) {
                                SearchVehicle = et_SearchVehicle.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Enter Search Vehicle", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 4) {
                        if (searchcheckin) {
                            if (!CheckinDate.equals("")) {
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Take Check in Date", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 5) {
                        if (searchcheckout) {
                            if (!CheckoutDate.equals("")) {
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(ViewAdhocVisitors.this, "Please Take Check out Date", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                }
                if (result) {
                    result = false;
                    Intent intent = new Intent(ViewAdhocVisitors.this, Adhoc_Search_Details.class);
                    intent.putExtra("ID", Organization_ID);
                    intent.putExtra("VIEW", ContextView);
                    intent.putExtra("User", CheckingUser);
                    intent.putExtra("CHECKINDATE", CheckinDate);
                    intent.putExtra("CHECKOUTDATE", CheckoutDate);
                    intent.putExtra("SEARCHNAME", SearchName);
                    intent.putExtra("SEARCHEMAIL", SearchEmail);
                    intent.putExtra("SEARCHMOBILE", SearchMobile);
                    intent.putExtra("SEARCHTOMEET", SearchTomeet);
                    intent.putExtra("SEARCHVEHICLE", SearchVehicle);
                    startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (visitorsthread.isAlive()) {
            visitorsthread.interrupt();
        }
        super.onDestroy();
    }

    private void reset() {
        if (searchname) {
            if (et_SearchName.getText().toString().equals("")) {
                searchname = false;
                Til_SearchName.setVisibility(View.GONE);
                SearchByName_btn.setVisibility(View.VISIBLE);
                SearchName = "";
            }
        }
        if (searchmobile) {
            if (et_SearchMobile.getText().toString().equals("")) {
                searchmobile = false;
                Til_SearchMobile.setVisibility(View.GONE);
                SearchByMobile_btn.setVisibility(View.VISIBLE);
                SearchMobile = "";
            }
        }
        if (searchtomeet) {
            if (et_SearchTomeet.getText().toString().equals("")) {
                searchtomeet = false;
                Til_SearchTomeet.setVisibility(View.GONE);
                SearchByToMeet_btn.setVisibility(View.VISIBLE);
                SearchTomeet = "";
            }
        }
        if (searchvehicle) {
            if (et_SearchVehicle.getText().toString().equals("")) {
                searchvehicle = false;
                Til_SearchVehicle.setVisibility(View.GONE);
                SearchByVehicle_btn.setVisibility(View.VISIBLE);
                SearchVehicle = "";
            }
        }
    }

    private void fullreset() {
        Search_btn.setVisibility(View.GONE);
        Reset_btn.setVisibility(View.GONE);
        VisitorsView.setVisibility(View.VISIBLE);
        if (searchcheckin) {
            searchcheckin = false;
            CheckinLayout.setVisibility(View.GONE);
            Checkin_btn.setVisibility(View.VISIBLE);
            CheckinDate = "";
        }
        if (searchcheckout) {
            searchcheckout = false;
            CheckoutLayout.setVisibility(View.GONE);
            Checkout_btn.setVisibility(View.VISIBLE);
            CheckoutDate = "";
        }
        if (searchname) {
            searchname = false;
            Til_SearchName.setVisibility(View.GONE);
            et_SearchName.setText("");
            SearchByName_btn.setVisibility(View.VISIBLE);
            SearchName = "";
        }
        if (searchmobile) {
            searchmobile = false;
            Til_SearchMobile.setVisibility(View.GONE);
            et_SearchMobile.setText("");
            SearchByMobile_btn.setVisibility(View.VISIBLE);
            SearchMobile = "";
        }
        if (searchtomeet) {
            searchtomeet = false;
            Til_SearchTomeet.setVisibility(View.GONE);
            et_SearchTomeet.setText("");
            SearchByToMeet_btn.setVisibility(View.VISIBLE);
            SearchTomeet = "";
        }
        if (searchvehicle) {
            searchvehicle = false;
            Til_SearchVehicle.setVisibility(View.GONE);
            et_SearchVehicle.setText("");
            SearchByVehicle_btn.setVisibility(View.VISIBLE);
            SearchVehicle = "";
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
        Intent intent = new Intent(this, ViewAdhocVisitors.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(ViewAdhocVisitors.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(ViewAdhocVisitors.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ViewAdhocVisitors.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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
        ConnectingTask.AdhocSmartCheckInOut checkOut = task.new AdhocSmartCheckInOut(detailsValue, result, Organization_ID, CheckingUser);
        checkOut.execute();
        dialog = ProgressDialog.show(ViewAdhocVisitors.this, "", "Checking...", true);
        dialog.setCancelable(true);
        visitorsthread = null;
        Runnable runnable = new ViewAdhocVisitors.VisitorsTimer();
        visitorsthread = new Thread(runnable);
        visitorsthread.start();
    }

    private void smartCardStatus(Context context, String Message) {
        functionCalls.ringtone(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("SmartCard Result");
        builder.setMessage(Message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adhocvisitordetails();
            }
        });
        AlertDialog smartalert = builder.create();
        smartalert.show();
        ((AlertDialog) smartalert).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    }

}
