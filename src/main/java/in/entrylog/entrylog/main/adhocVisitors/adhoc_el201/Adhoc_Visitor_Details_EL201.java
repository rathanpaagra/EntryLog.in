package in.entrylog.entrylog.main.adhocVisitors.adhoc_el201;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.main.CustomVolleyRequest;
import in.entrylog.entrylog.main.apponitments.Appointment_Details;
import in.entrylog.entrylog.main.el101_102.Visitor_Details_EL101;
import in.entrylog.entrylog.main.services.AdhocFieldService;
import in.entrylog.entrylog.main.services.FieldsService;
import in.entrylog.entrylog.values.DataPrinting;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL101_102;
import in.entrylog.entrylog.values.FunctionCalls;

public class Adhoc_Visitor_Details_EL201 extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int END_DLG = 6;

    LinearLayout maincontent, emailayout, designationlayout, departmentlayout,
            purposelayout, housenolayout, flatnolayout, blocklayout, noofvisitorlayout, classlayout, sectionlayout,
            studentnamelayout, idcardlayout,idcardtypelayout, bloodgrouplayout;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String Visitor_Name, Visitor_Mobile, Visitor_Fromaddress, Visitor_ToMeet, Visitor_CheckinTime, Visitor_CheckoutTime,
            Visitor_VehicleNo, Visitor_EntryGate, Visitor_Photo, ContextView, Visitor_id, Organization_ID, CheckingUser,
            HeaderPath, DataPath, OrganizationPath, EmptyPath, OrganizationName, BarCodeValue, CheckinUser="", CheckoutUser="",
            Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section,
            Student_Name, ID_Card, Email, Vehicleno,  Device = "", PrinterType = "", Issued_date,
            Expiry_date, Blood_group, ID_Card_Type, Contractors;
    NetworkImageView Visitor_image;
    ImageLoader imageLoader;
    TextView tv_name, tv_mobile, tv_address, tv_tomeet, tv_contractor,tv_checkintime, tv_checkouttime, tv_vehicleno, tv_entry, tv_exit,
            tv_email, tv_designation, tv_department, tv_purpose, tv_houseno, tv_flatno, tv_block, tv_noofvisitor, tv_class,
            tv_section, tv_studentname, tv_idcardno, tv_idcardtype, tv_issued_date, tv_expiry_date, tv_blood_group;
    Button Checkout_btn, Print_btn;
    ConnectingTask task;
    DetailsValue detailsValue;
    FunctionCalls functionCalls;
    DataPrinting dataPrinting;

    Thread checkingoutthread;
    boolean barcodeprinting = false, reprint = false;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    AdhocFieldService fieldsService;
    EL101_102 el101_102device;
    StringBuilder printdetails;

    static ProgressDialog dialog = null;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;

    //Delete Adhoc

    Button Delete_adhoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc__visitor__details__el201);

        int screenSize1 = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation1 = this.getWindowManager().getDefaultDisplay().getRotation();
        switch(screenSize1) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                setTheme(R.style.AppTheme);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation1) {
                    case Surface.ROTATION_0:
                        setTheme(R.style.MyTheme);
                        break;
                    case Surface.ROTATION_90:
                        setTheme(R.style.AppTheme);
                        break;
                    case Surface.ROTATION_270:
                        setTheme(R.style.AppTheme);
                        break;
                }
                break;
        }

        task = new ConnectingTask();
        detailsValue = new DetailsValue();
        functionCalls = new FunctionCalls();
        dataPrinting = new DataPrinting();

        fieldsService = new AdhocFieldService();
        el101_102device = new EL101_102();

        functionCalls.OrientationView(Adhoc_Visitor_Details_EL201.this);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor=settings.edit();


        Intent intent = getIntent();
        Bundle bnd = intent.getExtras();
        //region Intent Values
        ContextView = bnd.getString("View");
        Visitor_Photo = bnd.getString("Image");
        Organization_ID = bnd.getString("OrganizationID");
        PrinterType=bnd.getString("Printertype");
        Visitor_id = bnd.getString("VisitorID");
        Visitor_Name = bnd.getString("Name");
        Visitor_Mobile = bnd.getString("Mobile");
        Visitor_Fromaddress = bnd.getString("From");
        Visitor_ToMeet = bnd.getString("ToMeet");
        Visitor_VehicleNo = bnd.getString("VehicleNo");
        Email = bnd.getString("Email");
        Vehicleno = bnd.getString("VehicleNo");
        Visitor_Designation = bnd.getString("visitor_designation");
        Department = bnd.getString("department");
        Purpose = bnd.getString("purpose");
        House_number = bnd.getString("house_number");
        Flat_number = bnd.getString("flat_number");
        Block = bnd.getString("block");
        No_Visitor = bnd.getString("no_visitor");
        aClass = bnd.getString("class");
        Section = bnd.getString("section");
        Student_Name = bnd.getString("student_name");
        ID_Card = bnd.getString("id_card_number");
        ID_Card_Type=bnd.getString("id_card_type");
        Issued_date=bnd.getString("Issued_date");
        Expiry_date=bnd.getString("Expiry_date");
        Blood_group=bnd.getString("Blood_group");
        Contractors=bnd.getString("Contractors");
        //endregion
        OrganizationName = settings.getString("OrganizationName", "");


        //region Linear Layout Initialization

        emailayout = (LinearLayout) findViewById(R.id.adhoc_detailsemaillayout);
        designationlayout = (LinearLayout) findViewById(R.id.adhoc_detailsvisitordesignation_layout);
        departmentlayout = (LinearLayout) findViewById(R.id.adhoc_detailsdepartmentlayout);
        purposelayout = (LinearLayout) findViewById(R.id.adhoc_detailspurposelayout);
        housenolayout = (LinearLayout) findViewById(R.id.adhoc_detailshousenolayout);
        flatnolayout = (LinearLayout) findViewById(R.id.adhoc_detailsflatnolayout);
        blocklayout = (LinearLayout) findViewById(R.id.adhoc_detailsblocklayout);
        noofvisitorlayout = (LinearLayout) findViewById(R.id.adhoc_detailsnoofvisitorlayout);
        classlayout = (LinearLayout) findViewById(R.id.adhoc_detailsclasslayout);
        sectionlayout = (LinearLayout) findViewById(R.id.adhoc_detailssectionlayout);
        studentnamelayout = (LinearLayout) findViewById(R.id.adhoc_detailsstudentnamelayout);
        idcardlayout = (LinearLayout) findViewById(R.id.adhoc_detailsidcardnolayout);
        idcardtypelayout = (LinearLayout) findViewById(R.id.adhoc_detailsidcardtypelayout);
        bloodgrouplayout=(LinearLayout) findViewById(R.id.adhoc_bloodgroup_layout);
        //endregion

        //region TextView Initialization
        tv_name = (TextView) findViewById(R.id.adhoc_visitor_name);
        tv_mobile = (TextView) findViewById(R.id.adhoc_visitor_mobile);
        tv_address = (TextView) findViewById(R.id.adhoc_visitor_fromaddress);
        tv_contractor= (TextView) findViewById(R.id.adhoc_visitor_contractor);
        tv_tomeet = (TextView) findViewById(R.id.adhoc_visitor_tomeet);
        tv_vehicleno = (TextView) findViewById(R.id.adhoc_visitor_vehicleno);
        tv_email = (TextView) findViewById(R.id.adhoc_visitor_email);
        tv_designation = (TextView) findViewById(R.id.adhoc_visitor_designation);
        tv_department = (TextView) findViewById(R.id.adhoc_visitor_department);
        tv_purpose = (TextView) findViewById(R.id.adhoc_visitor_purpose);
        tv_houseno = (TextView) findViewById(R.id.adhoc_visitor_houseno);
        tv_flatno = (TextView) findViewById(R.id.adhoc_visitor_flatno);
        tv_block = (TextView) findViewById(R.id.adhoc_visitor_block);
        tv_noofvisitor = (TextView) findViewById(R.id.adhoc_noofvisitor);
        tv_class = (TextView) findViewById(R.id.adhoc_visitor_class);
        tv_section = (TextView) findViewById(R.id.adhoc_visitor_section);
        tv_studentname = (TextView) findViewById(R.id.adhoc_visitor_section);
        tv_idcardno = (TextView) findViewById(R.id.adhoc_visitor_idcardno);
        tv_idcardtype = (TextView) findViewById(R.id.adhoc_visitor_idcardtype);
        tv_issued_date= (TextView) findViewById(R.id.adhoc_visitor_issueddate);
        tv_expiry_date= (TextView) findViewById(R.id.adhoc_visitor_expirydate);
        tv_blood_group= (TextView) findViewById(R.id.adhoc_bloood_group);
        //endregion

        //region to initialise Button
        Delete_adhoc= (Button) findViewById(R.id.adhoc_delete_btn);
        checkingoutthread = null;
        Runnable runnable = new Adhoc_Visitor_Details_EL201.ADHOCTimer();
        checkingoutthread = new Thread(runnable);
        checkingoutthread.start();


        Visitor_image = (NetworkImageView) findViewById(R.id.adhoc_visitor_image);
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation) {
                    case Surface.ROTATION_0:
                        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.adhoc_coordinator_content);
                        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.adhoc_collapsing);
                        collapsingToolbarLayout.setTitle(Visitor_Name);
                        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                        break;
                    case Surface.ROTATION_90:
                        maincontent = (LinearLayout) findViewById(R.id.main_content);
                        break;
                    case Surface.ROTATION_270:
                        maincontent = (LinearLayout) findViewById(R.id.main_content);
                        break;
                }
                break;
        }
        CheckingUser = settings.getString("GuardID", "");

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }
        OrganizationPath = functionCalls.filepath("Textfile") + File.separator + "Organization.txt";
        HeaderPath = functionCalls.filepath("Textfile") + File.separator + "Header.txt";
        DataPath = functionCalls.filepath("Textfile") + File.separator + "Data.txt";
        EmptyPath = functionCalls.filepath("Textfile") + File.separator + "Empty.txt";
        printdetails = new StringBuilder();

        Delete_adhoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectingTask.AdhocDelete addhocUser = task.new AdhocDelete(Visitor_id, Organization_ID, Adhoc_Visitor_Details_EL201.this, detailsValue);
                addhocUser.execute();
                // Log.d("debug", "Stated");
                dialog= ProgressDialog.show(Adhoc_Visitor_Details_EL201.this, "", "Deleteting please wait..", true);
                //adhocstatus();
                checkingoutthread = null;
                Runnable runnable = new ADHOCTimer();
                checkingoutthread = new Thread(runnable);
                checkingoutthread.start();
            }
        });

        imageLoader.get(Visitor_Photo, ImageLoader.getImageListener(Visitor_image, R.drawable.blankperson,
                R.drawable.blankperson));
        Visitor_image.setImageUrl(Visitor_Photo, imageLoader);
        tv_name.setText(Visitor_Name);
        tv_mobile.setText(Visitor_Mobile);
        tv_address.setText(Visitor_Fromaddress);
        tv_contractor.setText(Contractors);
        tv_tomeet.setText(Visitor_ToMeet);
        tv_vehicleno.setText(Visitor_VehicleNo);
        tv_issued_date.setText(Issued_date);
        tv_expiry_date.setText(Expiry_date);
        //tv_blood_group.setText(Blood_group);
        DisplayFields();


    }

    private void DisplayFields() {
        functionCalls.LogStatus("Display field Started");
        HashSet<String> hashSet = new HashSet<>();
        hashSet = fieldsService.fieldset;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(hashSet);
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                String value = arrayList.get(i).toString();
                if (value.equals("Visitor Email")) {
                    emailayout.setVisibility(View.VISIBLE);
                    tv_email.setText(Email);
                }
                if (value.equals("Visitor Designation")) {
                    designationlayout.setVisibility(View.VISIBLE);
                    tv_designation.setText(Visitor_Designation);
                }
                if (value.equals("Department")) {
                    departmentlayout.setVisibility(View.VISIBLE);
                    tv_department.setText(Department);
                }
                if (value.equals("Purpose")) {
                    purposelayout.setVisibility(View.VISIBLE);
                    tv_purpose.setText(Purpose);
                }
                if (value.equals("House No")) {
                    housenolayout.setVisibility(View.VISIBLE);
                    tv_houseno.setText(House_number);
                }
                if (value.equals("Flat No")) {
                    flatnolayout.setVisibility(View.VISIBLE);
                    tv_flatno.setText(Flat_number);
                }
                if (value.equals("Block")) {
                    blocklayout.setVisibility(View.VISIBLE);
                    tv_block.setText(Block);
                }
                if (value.equals("Number of Visitor")) {
                    noofvisitorlayout.setVisibility(View.VISIBLE);
                    tv_noofvisitor.setText(No_Visitor);
                }
                if (value.equals("Class")) {
                    classlayout.setVisibility(View.VISIBLE);
                    tv_class.setText(aClass);
                }
                if (value.equals("Section")) {
                    sectionlayout.setVisibility(View.VISIBLE);
                    tv_section.setText(Section);
                }
                if (value.equals("Student Name")) {
                    studentnamelayout.setVisibility(View.VISIBLE);
                    tv_studentname.setText(Student_Name);
                }
                if (value.equals("ID Card No")) {
                    idcardlayout.setVisibility(View.VISIBLE);
                    tv_idcardno.setText(ID_Card);
                }
                if (value.equals("Blood Group")) {
                    bloodgrouplayout.setVisibility(View.VISIBLE);
                    tv_blood_group.setText(Blood_group);
                }

            }
        }
    }

    private void LogStatus(String str) {
        Log.d("debug", str);
    }


    class ADHOCTimer implements Runnable {

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
                    if (detailsValue.isAdhocDeleteSuccess()){
                        detailsValue.setAdhocDeleteSuccess(false);
                        dialog.dismiss();
                        checkingoutthread.interrupt();
                        Toast.makeText(Adhoc_Visitor_Details_EL201.this, "Successfully Deleted Adhoc User", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (detailsValue.isAdhocDeleteFailure()) {
                        detailsValue.setAdhocDeleteFailure(false);
                        dialog.dismiss();
                        checkingoutthread.interrupt();
                        Toast.makeText(Adhoc_Visitor_Details_EL201.this, "Deleting Adhoc Failed Please try again", Toast.LENGTH_SHORT).show();
                    }
                    String Message = "";
                    if (detailsValue.isAdhocSmartIn()) {
                        checkingoutthread.interrupt();
                        detailsValue.setAdhocSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(Adhoc_Visitor_Details_EL201.this, Message);
                    }
                    if (detailsValue.isAdhocSmartOut()) {
                        checkingoutthread.interrupt();
                        detailsValue.setAdhocSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(Adhoc_Visitor_Details_EL201.this, Message);
                    }
                    if (detailsValue.isAdhocSmartError()) {
                        checkingoutthread.interrupt();
                        detailsValue.setAdhocSmartError(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Adhoc Visitor is Expired......";
                        functionCalls.smartCardStatus(Adhoc_Visitor_Details_EL201.this, Message);
                    }
                    if (detailsValue.isAdhocSmartInvalidCard()) {
                        checkingoutthread.interrupt();
                        detailsValue.setAdhocSmartInvalidCard(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Please Check in Again......";
                        functionCalls.smartCardStatus(Adhoc_Visitor_Details_EL201.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Adhoc_Visitor_Details_EL201.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(Adhoc_Visitor_Details_EL201.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(Adhoc_Visitor_Details_EL201.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Adhoc_Visitor_Details_EL201.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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
        dialog = ProgressDialog.show(Adhoc_Visitor_Details_EL201.this, "", "Checking...", true);
        dialog.setCancelable(true);
        checkingoutthread = null;
        Runnable runnable = new Adhoc_Visitor_Details_EL201.ADHOCTimer();
        checkingoutthread = new Thread(runnable);
        checkingoutthread.start();
    }


}
