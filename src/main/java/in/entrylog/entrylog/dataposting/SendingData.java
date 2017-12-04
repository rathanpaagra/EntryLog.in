package in.entrylog.entrylog.dataposting;

import android.media.Image;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import in.entrylog.entrylog.values.FunctionCalls;

/**
 * Created by Admin on 06-Jun-16.
 */
public class SendingData {

    String BASE_URL = DataAPI.BASE_URL;
    FunctionCalls functionCalls = new FunctionCalls();

    public String PostLogin(String Organization, String Username, String Password) {
        String responsestr = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_identity", Organization);
        datamap.put("security_guards_username", Username);
        datamap.put("security_guards_password", Password);
        try {
            responsestr = UrlPostConnection("Check_login_api", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responsestr;
    }

    public String Logout(String GuardID) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("security_guards_id", GuardID);
        datamap.put("is_logged", "0");
        try {
            response = UrlPostConnection("Logout", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetFields(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Fetch_fields", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetStaffs(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Staff1", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetStaffsAddVisitor(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Staff2", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String VisitorsCheckIn(String Name, String Email, String Mobile, String Address, String Tomeet, String VehicleNo,
                                  String ImagefileName, String Organization_ID, String Security_ID, String Barcode,
                                  String visitor_Designation, String department, String purpose, String house_number,
                                  String flat_number, String block, String no_Visitor, String aClass, String section,
                                  String student_Name, String ID_Card, String Visitor_Entry,
                                  String Current_Time, String ID_Card_Type/*, String Blood_group*/,String tm_hours,String tm_minutes) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("visitors_name", Name);
        datamap.put("visitors_email", Email);
        datamap.put("visitors_mobile", Mobile);
        datamap.put("visitors_address", Address);
        datamap.put("to_meet", Tomeet);
        datamap.put("visitors_vehicle_number", Barcode);
        datamap.put("visitors_photo", VehicleNo);
        datamap.put("organization_id", ImagefileName);
        datamap.put("security_guards_id", Security_ID);
        datamap.put("visitors_bar_code", Organization_ID);
        functionCalls.LogStatus("BarCode: " + Organization_ID);
        datamap.put("visitor_designation", visitor_Designation);
        datamap.put("department", department);
        datamap.put("purpose", purpose);
        datamap.put("house_number", house_number);
        datamap.put("flat_number", flat_number);
        datamap.put("block", block);
        datamap.put("no_visitor", no_Visitor);
        datamap.put("class", aClass);
        datamap.put("section", section);
        datamap.put("student_name", student_Name);
        datamap.put("id_card_number", ID_Card);
        datamap.put("verification_status_id", Visitor_Entry);
        datamap.put("checked_in_time", Current_Time);
        datamap.put("id_card_type", ID_Card_Type);
        //datamap.put("blood_group", Blood_group);
        datamap.put("in_hours",tm_hours);
        datamap.put("in_minutes",tm_minutes);
        try {
            response = UrlPostConnection("Check_in_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String VisitorsCheckOut(String MobileNo, String Organization_id, String Security_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("visitors_bar_code", MobileNo);
        datamap.put("organization_id", Organization_id);
        datamap.put("security_guards_id", Security_id);
        try {
            response = UrlPostConnection("Check_out_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String CheckVisitors(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Checked_in_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AllVisitors(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String VisitorManualCheckout(String VisitorID, String OrganizationID, String Checkoutby) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("visitors_id", VisitorID);
        datamap.put("organization_id", OrganizationID);
        datamap.put("check_out_by", Checkoutby);
        try {
            response = UrlPostConnection("Manual_checkout", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String MobileAutoSuggest(String Organization_id, String Mobile) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        datamap.put("visitors_mobile", Mobile);
        try {
            response = UrlPostConnection("Mobile_autosuggest", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String SearchVisitors(String Organization_id, String checkin_date, String checkout_date, String visitor_name,
                                 String visitor_mobile, String visitor_email, String visitor_tomeet, String visitor_vehicleNo,
                                 String User_Status) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        datamap.put("check_in_time", checkin_date);
        datamap.put("check_out_time", checkout_date);
        datamap.put("visitors_mobile", visitor_mobile);
        datamap.put("visitors_vehicle_number", visitor_vehicleNo);
        datamap.put("to_meet", visitor_tomeet);
        datamap.put("visitors_name", visitor_name);
        datamap.put("visitors_email", visitor_email);
        datamap.put("check_status_id", User_Status);
        try {
            response = UrlPostConnection("Search_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*public String OvernightStay_Visitors (String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Overnight_stay_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogStatus("OverNightstay: "+response);
        return response;
    }*/

    public String OTPGeneration(String Mobile, String OTP, String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("visitors_mobile", Mobile);
        datamap.put("otp", OTP);
        datamap.put("organization_id", Organization_id);

        try {
            response = UrlPostConnection("Send_otp", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }



    public String Printable_Fields(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Printable_fields", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String Permissions(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Organization_permissions", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String UpdatedApk() {
        String response = "";
        try {
            response = UrlGetConnection("Updated_apk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetTime() {
        String response = "";
        try {
            response = UrlGetConnection("Get_time");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response = "";
        functionCalls.LogStatus("Connecting URL: " + BASE_URL + Post_Url);
        URL url = new URL(BASE_URL + Post_Url);
        functionCalls.LogStatus("URL Connection 1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        functionCalls.LogStatus("URL Connection 2");
        conn.setReadTimeout(15000);
        functionCalls.LogStatus("URL Connection 3");
        conn.setConnectTimeout(15000);
        functionCalls.LogStatus("URL Connection 4");
        conn.setRequestMethod("POST");
        functionCalls.LogStatus("URL Connection 5");
        conn.setDoInput(true);
        functionCalls.LogStatus("URL Connection 6");
        conn.setDoOutput(true);
        functionCalls.LogStatus("URL Connection 7");

        OutputStream os = conn.getOutputStream();
        functionCalls.LogStatus("URL Connection 8");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        functionCalls.LogStatus("URL Connection 9");
        writer.write(getPostDataString(datamap));
        functionCalls.LogStatus("URL Connection 10");
        writer.flush();
        functionCalls.LogStatus("URL Connection 11");
        writer.close();
        functionCalls.LogStatus("URL Connection 12");
        os.close();
        functionCalls.LogStatus("URL Connection 13");
        int responseCode = conn.getResponseCode();
        functionCalls.LogStatus("URL Connection 14");
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            functionCalls.LogStatus("URL Connection 15");
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            functionCalls.LogStatus("URL Connection 16");
            while ((line = br.readLine()) != null) {
                response += line;
            }
            functionCalls.LogStatus("URL Connection 17");
        } else {
            response = "";
            functionCalls.LogStatus("URL Connection 18");
        }
        functionCalls.LogStatus("URL Connection Response: " + response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            functionCalls.LogStatus(result.toString());
        }

        return result.toString();
    }

    private String UrlGetConnection(String Get_Url) throws IOException {
        String response = "";
        functionCalls.LogStatus("Connecting URL: " + BASE_URL + Get_Url);
        URL url = new URL(BASE_URL + Get_Url);
        functionCalls.LogStatus("URL Get Connection 1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        functionCalls.LogStatus("URL Get Connection 2");
        conn.setReadTimeout(15000);
        functionCalls.LogStatus("URL Get Connection 3");
        conn.setConnectTimeout(15000);
        functionCalls.LogStatus("URL Get Connection 4");
        int responseCode = conn.getResponseCode();
        functionCalls.LogStatus("URL Get Connection 5");
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            functionCalls.LogStatus("URL Get Connection 6");
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            functionCalls.LogStatus("URL Get Connection 7");
            while ((line = br.readLine()) != null) {
                response += line;
            }
            functionCalls.LogStatus("URL Get Connection 8");
        } else {
            response = "";
            functionCalls.LogStatus("URL Get Connection 9");
        }
        functionCalls.LogStatus("URL Get Connection Response: " + response);
        return response;
    }

    public String SmartCheckInOut(String SmartID, String Organization_id, String Security_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("rfid_number", SmartID);
        functionCalls.LogStatus("SmartID: " + SmartID);
        datamap.put("organization_id", Organization_id);
        datamap.put("security_guards_id", Security_id);
        try {
            response = UrlPostConnection("Rfid_status2", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AllAppointments(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Staff_appointments", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String SearchAppointments(String Organization_id, String name, String mobile, String tomeet, String date) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        datamap.put("visitor_name", name);
        datamap.put("visitor_mobile", mobile);
        datamap.put("staff_id", tomeet);
        datamap.put("appointment_date", date);


        try {
            response = UrlPostConnection("Staff_appointments_search", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    //Adhoc visitors starts

    public String adhoc_check(String adhoc_password, String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_admin_password", adhoc_password);
        datamap.put("organization_id", Organization_id);

        try {
            response = UrlPostConnection("Check_organization_admin_password", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocVisitorsCheckIn(String Name, String Email, String Mobile, String Address, String Tomeet, String VehicleNo,
                                  String ImagefileName, String Organization_ID, String Security_ID, String Barcode,
                                  String visitor_Designation, String department, String purpose, String house_number,
                                  String flat_number, String block, String no_Visitor, String aClass, String section,
                                  String student_Name, String ID_Card, String Expiry_date, String Issued_date, String ID_Card_Type, String Adhoc_visitors_blood_group,String Adhoc_contractor) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("adhoc_visitors_name", Name);
        datamap.put("adhoc_visitors_email", Email);
        datamap.put("adhoc_visitors_mobile", Mobile);
        datamap.put("adhoc_visitors_address", Address);
        datamap.put("adhoc_visitors_to_meet", Tomeet);
        datamap.put("adhoc_visitors_vehicle_number",VehicleNo);
        datamap.put("adhoc_visitors_photo", ImagefileName);
        datamap.put("organization_id", Organization_ID);
        datamap.put("user_id", Security_ID);
        datamap.put("rfid_number",Barcode);
        functionCalls.LogStatus("BarCode: " + Barcode);
        datamap.put("adhoc_visitor_designation", visitor_Designation);
        datamap.put("adhoc_visitors_department", department);
        datamap.put("adhoc_visitors_purpose", purpose);
        datamap.put("adhoc_visitors_house_number", house_number);
        datamap.put("adhoc_visitors_flat_number", flat_number);
        datamap.put("adhoc_visitors_block", block);
        datamap.put("adhoc_visitors_no_visitor", no_Visitor);
        datamap.put("adhoc_visitors_class", aClass);
        datamap.put("adhoc_visitors_section", section);
        datamap.put("adhoc_visitors_student_name", student_Name);
        datamap.put("adhoc_visitors_id_card_number", ID_Card);
        datamap.put("adhoc_visitors_id_card_type", ID_Card_Type);
        datamap.put("adhoc_visitors_blood_group", Adhoc_visitors_blood_group);
        datamap.put("issued_date", Issued_date);
        datamap.put("expiry_date", Expiry_date);
        datamap.put("contractors_id", Adhoc_contractor);
        try {
            response = UrlPostConnection("Add_adhoc_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocOTPGeneration(String Mobile, String OTP, String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("adhoc_visitors_mobile", Mobile);
        datamap.put("otp", OTP);
        datamap.put("organization_id", Organization_id);

        try {
            response = UrlPostConnection("Send_adhoc_otp", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocSmartCheckInOut(String SmartID, String Organization_id, String Security_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();

        datamap.put("rfid_number", SmartID);
        functionCalls.LogStatus("SmartID: " + SmartID);
        datamap.put("organization_id", Organization_id);
        datamap.put("user_id", Security_id);
        try {
            response = UrlPostConnection("Adhoc_visitors_log", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocGetFields(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Fetch_fields_adhoc", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocMobileAutoSuggest(String Organization_id, String Mobile) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        datamap.put("adhoc_visitors_mobile", Mobile);
        try {
            response = UrlPostConnection("Mobile_autosuggest_adhoc", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //adhoc visitors

    public String AllAdhocVisitors(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Adhoc_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocSearchVisitors(String Organization_id, String checkin_date, String checkout_date, String visitor_name,
                                 String visitor_mobile, String visitor_email, String visitor_tomeet, String visitor_vehicleNo) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        datamap.put("issued_date", checkin_date);
        datamap.put("expiry_date", checkout_date);
        datamap.put("adhoc_visitors_mobile", visitor_mobile);
        datamap.put("adhoc_visitors_vehicle_number", visitor_vehicleNo);
        datamap.put("adhoc_visitors_to_meet", visitor_tomeet);
        datamap.put("adhoc_visitors_name", visitor_name);
        datamap.put("adhoc_visitors_email", visitor_email);
        try {
            response = UrlPostConnection("Search_adhoc_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String AdhocDelete(String adhoc_visitor_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("adhoc_visitors_id", adhoc_visitor_id);

        try {
            response = UrlPostConnection("Delete_adhoc_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String GetAdhocContractorFetching(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Contractors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String PurposeGetStaffs(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Autosuggest_values", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String FetchTimeBoundFields(String organizationid) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", organizationid);
        try {
            response = UrlPostConnection("Check_timebound_status", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    //TimeBound overstay Visitors
    public String OverStayVisitors(String Organization_id) {
        String response = "";
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("organization_id", Organization_id);
        try {
            response = UrlPostConnection("Overstay_visitors", datamap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
