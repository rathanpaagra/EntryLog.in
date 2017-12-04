package in.entrylog.entrylog.dataposting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;

import in.entrylog.entrylog.adapters.AdhocVisitorsAdapters;
import in.entrylog.entrylog.adapters.AppointmentAdapters;
import in.entrylog.entrylog.adapters.VisitorsAdapters;
import in.entrylog.entrylog.values.DetailsValue;

/**
 * Created by Admin on 06-Jun-16.
 */
public class ConnectingTask {
    SendingData sendingData = new SendingData();
    ReceivingData receivingData = new ReceivingData();

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show, Context context, final View mProgressBar) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public class LoginData extends AsyncTask<String, String, String> {
        String result = "", Username, Password, Organization;
        DetailsValue details;

        public LoginData(String username, String password, String organization, DetailsValue detail) {
            this.Username = username;
            this.Password = password;
            this.Organization = organization;
            this.details = detail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.PostLogin(Organization, Username, Password);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", "Result: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.LoginDetails(result, details);
        }
    }

    public class DisplayingFields extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public DisplayingFields(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.GetFields(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.DisplayFields(result, details, HashSet);
        }
    }

    public class StaffFetching extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public StaffFetching(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.GetStaffs(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.GetStaffStatus(result, details, HashSet);
        }
    }

    public class StaffFetchingAddVisitor extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public StaffFetchingAddVisitor(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.GetStaffsAddVisitor(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.GetStaffStatusAddVisitor(result, details, HashSet);
        }
    }

    public class LogoutUser extends AsyncTask<String, String, String> {
        String Guard_ID, result = "";
        Context context;
        DetailsValue detailsValue;

        public LogoutUser(String guard_ID, Context context, DetailsValue value) {
            Guard_ID = guard_ID;
            this.context = context;
            this.detailsValue = value;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.Logout(Guard_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.LogoutDetails(result, detailsValue);
        }
    }

    public class VisitorsCheckIn extends AsyncTask<String, String, String> {
        DetailsValue details;
        String Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_FromAddress, Visitors_ToMeet, BarCode,
                Visitors_VehicleNo, Visitors_ImageFileName, Security_ID, Organization_ID, Visitor_Designation, Department,
                Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section, Student_Name, ID_Card, Visitor_Entry,
                result = "", Current_Time, ID_Card_Type, Blood_Group, Timebound_hr, Timebound_min;

        public VisitorsCheckIn(DetailsValue details, String visitors_Name, String visitors_Email, String visitors_Mobile,
                               String visitors_FromAddress, String visitors_ToMeet, String barCode, String visitors_VehicleNo,
                               String visitors_ImageFileName, String security_ID, String organization_ID,
                               String visitor_Designation, String department, String purpose, String house_number,
                               String flat_number, String block, String no_Visitor, String aclass, String section,
                               String student_Name, String ID_Card, String visitor_entry, String current_Time, String id_card_type/*, String blood_group*/, String tm_hour, String tm_minute) {
            this.details = details;
            Visitors_Name = visitors_Name;
            Visitors_Email = visitors_Email;
            Visitors_Mobile = visitors_Mobile;
            Visitors_FromAddress = visitors_FromAddress;
            Visitors_ToMeet = visitors_ToMeet;
            BarCode = barCode;
            Visitors_VehicleNo = visitors_VehicleNo;
            Visitors_ImageFileName = visitors_ImageFileName;
            Security_ID = security_ID;
            Organization_ID = organization_ID;
            Visitor_Designation = visitor_Designation;
            Department = department;
            Purpose = purpose;
            House_number = house_number;
            Flat_number = flat_number;
            Block = block;
            No_Visitor = no_Visitor;
            aClass = aclass;
            Section = section;
            Student_Name = student_Name;
            this.ID_Card = ID_Card;
            Visitor_Entry = visitor_entry;
            Current_Time = current_Time;
            ID_Card_Type = id_card_type;
            // Blood_Group=blood_group;
            Timebound_hr = tm_hour;
            Timebound_min = tm_minute;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.VisitorsCheckIn(Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_FromAddress,
                        Visitors_ToMeet, Visitors_VehicleNo, Visitors_ImageFileName, Organization_ID, Security_ID, BarCode,
                        Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                        Section, Student_Name, ID_Card, Visitor_Entry, Current_Time, ID_Card_Type/*, Blood_Group*/, Timebound_hr, Timebound_min);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.VisitorsCheckinStatus(result, details);
        }
    }

    public class VisitorsCheckOut extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String Visitors_mobile, Organization_ID, Security_ID, result = "";

        public VisitorsCheckOut(DetailsValue detailsValue, String visitors_mobile, String organization_ID, String security_ID) {
            this.detailsValue = detailsValue;
            Visitors_mobile = visitors_mobile;
            Organization_ID = organization_ID;
            Security_ID = security_ID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.VisitorsCheckOut(Visitors_mobile, Organization_ID, Security_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.VisitorsCheckOutStatus(result, detailsValue);
        }
    }


    public class CheckVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        VisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";
        Context context;

        public CheckVisitors(ArrayList<DetailsValue> arrayList, VisitorsAdapters adapters, DetailsValue detailsValue,
                             String organization_ID, Context context) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.CheckVisitors(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.CheckVisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }

    public class AllVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        VisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";
        Context context;

        public AllVisitors(ArrayList<DetailsValue> arrayList, VisitorsAdapters adapters, DetailsValue detailsValue,
                           String organization_ID, Context context) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AllVisitors(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AllVisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }

    public class VisitorManualCheckout extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String Organization_ID, result = "", Visitor_ID, CheckoutBy;
        Context context;

        public VisitorManualCheckout(DetailsValue detailsValue, String organization_ID, String visitor_ID, String checkoutBy,
                                     Context context) {
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            Visitor_ID = visitor_ID;
            this.context = context;
            this.CheckoutBy = checkoutBy;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.VisitorManualCheckout(Visitor_ID, Organization_ID, CheckoutBy);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.VisitorManualCheckout(result, detailsValue);
        }
    }

    public class MobileAutoSuggest extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String Organization_ID, MobileNumber, result = "";
        View ProgressBar;
        Context context;

        public MobileAutoSuggest(DetailsValue detailsValue, String organization_ID, String mobileNumber,
                                 View progressBar, Context context) {
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            MobileNumber = mobileNumber;
            ProgressBar = progressBar;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true, context, ProgressBar);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.MobileAutoSuggest(Organization_ID, MobileNumber);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            showProgress(false, context, ProgressBar);
            receivingData.MobileAutoSuggestStatus(result, detailsValue);
        }
    }

    public class SearchVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        VisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, Checkin_date, Checkout_date, Visitor_name, Visitor_mobile, Visitor_email, Visitor_tomeet,
                Visitor_vehicleNo, User_Status, result = "";
        Context context;

        public SearchVisitors(ArrayList<DetailsValue> arrayList, VisitorsAdapters adapters, DetailsValue detailsValue,
                              String organization_ID, String checkin_date, String checkout_date, String visitor_name,
                              String visitor_mobile, String visitor_email, String visitor_tomeet, Context context,
                              String visitor_vehicleNo, String user_Status) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            Checkin_date = checkin_date;
            Checkout_date = checkout_date;
            Visitor_name = visitor_name;
            Visitor_mobile = visitor_mobile;
            Visitor_email = visitor_email;
            Visitor_tomeet = visitor_tomeet;
            this.context = context;
            Visitor_vehicleNo = visitor_vehicleNo;
            User_Status = user_Status;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.SearchVisitors(Organization_ID, Checkin_date, Checkout_date, Visitor_name,
                        Visitor_mobile, Visitor_email, Visitor_tomeet, Visitor_vehicleNo, User_Status);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            receivingData.VisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }

    public class OvernightStay_Visitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        VisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";
        Context context;

        public OvernightStay_Visitors(ArrayList<DetailsValue> arrayList, VisitorsAdapters adapters, DetailsValue detailsValue,
                                      String organization_ID, Context context) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                /*result = sendingData.OvernightStay_Visitors(Organization_ID);*/
                result = sendingData.CheckVisitors(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            /*receivingData.VisitorsStatus(result, detailsValue, arrayList, adapters);*/
            receivingData.CheckVisitorsStatus(result, detailsValue, arrayList, adapters);


        }
    }

    public class SMSOTP extends AsyncTask<String, String, String> {
        String Mobile, OTP, Organization_Id, result = "";
        DetailsValue detailsValue;

        public SMSOTP(String mobile, String OTP, String organization_Id, DetailsValue detailsvalue) {
            Mobile = mobile;
            Organization_Id = organization_Id;
            this.OTP = OTP;
            this.detailsValue = detailsvalue;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.OTPGeneration(Mobile, OTP, Organization_Id);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            receivingData.SMSExpiry(result, detailsValue);
        }
    }


    public class Printing_Fields extends AsyncTask<String, String, String> {
        String result = "", OrganizationID;
        DetailsValue details;
        HashSet<String> OrderSet, DisplaySet;

        public Printing_Fields(String organizationID, DetailsValue details, HashSet<String> orderSet, HashSet<String> displaySet) {
            OrganizationID = organizationID;
            this.details = details;
            OrderSet = orderSet;
            DisplaySet = displaySet;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.Printable_Fields(OrganizationID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.PrintingFields_Status(result, details, OrderSet, DisplaySet);
        }
    }

    public class OrganizationPermissions extends AsyncTask<String, String, String> {
        String result = "", OrganizationID;
        DetailsValue details;

        public OrganizationPermissions(String organizationID, DetailsValue details) {
            OrganizationID = organizationID;
            this.details = details;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.Permissions(OrganizationID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.PermissionStatus(result, details);
        }
    }

    public class CheckUpdatedApk extends AsyncTask<String, String, String> {
        String result = "";
        DetailsValue details;

        public CheckUpdatedApk(DetailsValue details) {
            this.details = details;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.UpdatedApk();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.ApkStatus(result, details);
        }
    }

    public class GetTime extends AsyncTask<String, String, String> {
        String result = "";
        DetailsValue details;

        public GetTime(DetailsValue details) {
            this.details = details;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.GetTime();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.TimeStatus(result, details);
        }
    }

    public class SmartCheckinout extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String SmartID, Organization_ID, Security_ID, result = "";

        public SmartCheckinout(DetailsValue detailsValue, String smartID, String organization_ID, String security_ID) {
            this.detailsValue = detailsValue;
            SmartID = smartID;
            Organization_ID = organization_ID;
            Security_ID = security_ID;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.SmartCheckInOut(SmartID, Organization_ID, Security_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.SmartCheckInOutStatus(result, detailsValue);
        }
    }

    public class AllAppointments extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        AppointmentAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";

        public AllAppointments(ArrayList<DetailsValue> arrayList, AppointmentAdapters adapters, DetailsValue detailsValue,
                               String organization_ID) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AllAppointments(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AllAppointmentsDetails(result, detailsValue, arrayList, adapters);
        }
    }

    public class SearchAppointments extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        AppointmentAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "", Name, Mobile, Tomeet, Date;

        public SearchAppointments(ArrayList<DetailsValue> arrayList, AppointmentAdapters adapters, DetailsValue detailsValue,
                                  String organization_ID, String name, String mobile, String tomeet, String date) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            Name = name;
            Mobile = mobile;
            Tomeet = tomeet;
            Date = date;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.SearchAppointments(Organization_ID, Name, Mobile, Tomeet, Date);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AllAppointmentsSearchDetails(result, detailsValue, arrayList, adapters);
        }
    }

    //10-mar-2017
    public class Addhoc_user extends AsyncTask<String, String, String> {
        String addhoc_pass, Organization_Id, result = "";
        Context context;
        DetailsValue detailsValue;

        public Addhoc_user(String mobile, String organization_Id, Context context, DetailsValue detailsValue) {
            addhoc_pass = mobile;
            Organization_Id = organization_Id;
            this.context = context;
            this.detailsValue = detailsValue;


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.adhoc_check(addhoc_pass, Organization_Id);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.Adhoc_check_admin_password(result, detailsValue);
        }
    }

    // Adhoc Visitor start
    public class AdhocVisitorsCheckIn extends AsyncTask<String, String, String> {
        DetailsValue details;
        String Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_FromAddress, Visitors_ToMeet, BarCode,
                Visitors_VehicleNo, Visitors_ImageFileName, Security_ID, Organization_ID, Visitor_Designation, Department,
                Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section, Student_Name, ID_Card, Expiry_Date,
                result = "", Issued_Date, ID_Card_Type, Adhoc_Visitors_Blood_Group, Adhoc_contractor;

        public AdhocVisitorsCheckIn(DetailsValue details, String visitors_Name, String visitors_Email, String visitors_Mobile,
                                    String visitors_FromAddress, String visitors_ToMeet, String barCode, String visitors_VehicleNo,
                                    String visitors_ImageFileName, String security_ID, String organization_ID,
                                    String visitor_Designation, String department, String purpose, String house_number,
                                    String flat_number, String block, String no_Visitor, String aclass, String section,
                                    String student_Name, String ID_Card, String Expiry_date, String Issued_date, String id_card_type, String adhoc_visitors_blood_group, String adhoc_contractor) {
            this.details = details;
            Visitors_Name = visitors_Name;
            Visitors_Email = visitors_Email;
            Visitors_Mobile = visitors_Mobile;
            Visitors_FromAddress = visitors_FromAddress;
            Visitors_ToMeet = visitors_ToMeet;
            BarCode = barCode;
            Visitors_VehicleNo = visitors_VehicleNo;
            Visitors_ImageFileName = visitors_ImageFileName;
            Security_ID = security_ID;
            Organization_ID = organization_ID;
            Visitor_Designation = visitor_Designation;
            Department = department;
            Purpose = purpose;
            House_number = house_number;
            Flat_number = flat_number;
            Block = block;
            No_Visitor = no_Visitor;
            aClass = aclass;
            Section = section;
            Student_Name = student_Name;
            this.ID_Card = ID_Card;
            Expiry_Date = Expiry_date;
            Issued_Date = Issued_date;
            ID_Card_Type = id_card_type;
            Adhoc_Visitors_Blood_Group = adhoc_visitors_blood_group;
            Adhoc_contractor = adhoc_contractor;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocVisitorsCheckIn(Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_FromAddress,
                        Visitors_ToMeet, Visitors_VehicleNo, Visitors_ImageFileName, Organization_ID, Security_ID, BarCode,
                        Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                        Section, Student_Name, ID_Card, Expiry_Date, Issued_Date, ID_Card_Type, Adhoc_Visitors_Blood_Group, Adhoc_contractor);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AdhocVisitorsCheckinStatus(result, details);
        }
    }

    public class ADHOCSMSOTP extends AsyncTask<String, String, String> {
        String Mobile, OTP, Organization_Id, result = "";

        public ADHOCSMSOTP(String mobile, String OTP, String organization_Id) {
            Mobile = mobile;
            Organization_Id = organization_Id;
            this.OTP = OTP;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.AdhocOTPGeneration(Mobile, OTP, Organization_Id);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public class AdhocSmartCheckInOut extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String SmartID, Organization_ID, Security_ID, result = "";

        public AdhocSmartCheckInOut(DetailsValue detailsValue, String smartID, String organization_ID, String security_ID) {
            this.detailsValue = detailsValue;
            SmartID = smartID;
            Organization_ID = organization_ID;
            Security_ID = security_ID;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocSmartCheckInOut(SmartID, Organization_ID, Security_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AdhocSmartCheckInOutStatus(result, detailsValue);
        }
    }

    //fetching fields
    public class AdhocDisplayingFields extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public AdhocDisplayingFields(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocGetFields(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AdhocDisplayFields(result, details, HashSet);
        }
    }

    public class AdhocMobileAutoSuggest extends AsyncTask<String, String, String> {
        DetailsValue detailsValue;
        String Organization_ID, MobileNumber, result = "";
        View ProgressBar;
        Context context;

        public AdhocMobileAutoSuggest(DetailsValue detailsValue, String organization_ID, String mobileNumber,
                                      View progressBar, Context context) {
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            MobileNumber = mobileNumber;
            ProgressBar = progressBar;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true, context, ProgressBar);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocMobileAutoSuggest(Organization_ID, MobileNumber);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            showProgress(false, context, ProgressBar);
            receivingData.AdhocMobileAutoSuggestStatus(result, detailsValue);
        }
    }


    public class AllAdhocVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        AdhocVisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";
        Context context;

        public AllAdhocVisitors(ArrayList<DetailsValue> arrayList, AdhocVisitorsAdapters adapters, DetailsValue detailsValue,
                                String organization_ID, Context context) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AllAdhocVisitors(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.AllAdhocVisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }

    public class AdhocSearchVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        AdhocVisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, Checkin_date, Checkout_date, Visitor_name, Visitor_mobile, Visitor_email, Visitor_tomeet,
                Visitor_vehicleNo, User_Status, result = "";
        Context context;

        public AdhocSearchVisitors(ArrayList<DetailsValue> arrayList, AdhocVisitorsAdapters adapters, DetailsValue detailsValue,
                                   String organization_ID, String checkin_date, String checkout_date, String visitor_name,
                                   String visitor_mobile, String visitor_email, String visitor_tomeet, Context context,
                                   String visitor_vehicleNo) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            Checkin_date = checkin_date;
            Checkout_date = checkout_date;
            Visitor_name = visitor_name;
            Visitor_mobile = visitor_mobile;
            Visitor_email = visitor_email;
            Visitor_tomeet = visitor_tomeet;
            this.context = context;
            Visitor_vehicleNo = visitor_vehicleNo;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocSearchVisitors(Organization_ID, Checkin_date, Checkout_date, Visitor_name,
                        Visitor_mobile, Visitor_email, Visitor_tomeet, Visitor_vehicleNo);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            receivingData.AdhocVisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }

    //Delete Adhoc User
    public class AdhocDelete extends AsyncTask<String, String, String> {

        String adhoc_visitor_id, Organization_Id, result = "";
        Context context;
        DetailsValue detailsValue;

        public AdhocDelete(String Adhoc_visitor_id, String organization_Id, Context context, DetailsValue detailsValue) {
            adhoc_visitor_id = Adhoc_visitor_id;
            Organization_Id = organization_Id;
            this.context = context;
            this.detailsValue = detailsValue;

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.AdhocDelete(adhoc_visitor_id);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.Adhoc_Delete(result, detailsValue);
        }
    }

    public class AdhocContractorFetching extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public AdhocContractorFetching(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.GetAdhocContractorFetching(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.GetContractorStatus(result, details, HashSet);
        }
    }
    //Purpose Fiels Fetching

    public class PurposeStaffFetching extends AsyncTask<String, String, String> {
        String result = "", Organizationid;
        DetailsValue details;
        HashSet<String> HashSet;

        public PurposeStaffFetching(String organizationid, DetailsValue detailsValue, HashSet<String> hashSet) {
            this.Organizationid = organizationid;
            this.details = detailsValue;
            this.HashSet = hashSet;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.PurposeGetStaffs(Organizationid);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.PurposeGetStaffStatus(result, details, HashSet);
        }
    }

    //Time Bound Check
    public class FetchTimeBoundFields extends AsyncTask<String, String, String> {
        String result = "", OrganizationID;
        DetailsValue details;

        public FetchTimeBoundFields(String organizationID, DetailsValue details) {
            OrganizationID = organizationID;
            this.details = details;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.FetchTimeBoundFields(OrganizationID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.TimeBoundStatus(result, details);
        }
    }

    //Time Bound OverStay Visitors
    public class OverStayVisitors extends AsyncTask<String, String, String> {
        ArrayList<DetailsValue> arrayList;
        VisitorsAdapters adapters;
        DetailsValue detailsValue;
        String Organization_ID, result = "";
        Context context;

        public OverStayVisitors(ArrayList<DetailsValue> arrayList, VisitorsAdapters adapters, DetailsValue detailsValue,
                             String organization_ID, Context context) {
            this.arrayList = arrayList;
            this.adapters = adapters;
            this.detailsValue = detailsValue;
            Organization_ID = organization_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result = sendingData.OverStayVisitors(Organization_ID);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.OverstayVisitorsStatus(result, detailsValue, arrayList, adapters);
        }
    }
}
