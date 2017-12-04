package in.entrylog.entrylog.dataposting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import in.entrylog.entrylog.adapters.AdhocVisitorsAdapters;
import in.entrylog.entrylog.adapters.AppointmentAdapters;
import in.entrylog.entrylog.adapters.VisitorsAdapters;
import in.entrylog.entrylog.values.DetailsValue;

/**
 * Created by Admin on 06-Jun-16.
 */
public class ReceivingData {

    String Image_Url = DataAPI.Image_Url;
    String Image_Url2 = DataAPI.Image_Url2;

    String Apk_Url = DataAPI.Apk_Url;

    public void LoginDetails(String result, DetailsValue details) {
        Log.d("debug", result);
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Response = jo.getString("message");
                if (Response.equals("Success")) {
                    String ID = jo.getString("organization_id");
                    details.setOrganizationID(ID);
                    String GuardID = jo.getString("security_guards_id");
                    details.setGuardID(GuardID);
                    String OrganizationName = jo.getString("organization_name");
                    details.setOrganizationName(OrganizationName);
                    String OverNightTime = jo.getString("overnight_stay_time");
                    details.setOverNightStay_Time(OverNightTime);
                    String BarCode = jo.getString("visitors_bar_code");
                    details.setVisitors_BarCode(BarCode);
                    details.setLoginSuccess(true);
                } else if (Response.equals("Failure")) {
                    details.setLoginFailure(true);
                } else if (Response.equals("Account Blocked")) {
                    details.setAccountblocked(true);
                } else {
                    details.setLoginExist(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void LogoutDetails(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Response = jo.getString("message");
                if (Response.equals("Success")) {
                    details.setLoginSuccess(true);
                } else {
                    details.setLoginFailure(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void DisplayFields(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String field = jo.getString("get_organization_fields_name");
                    if (!field.equals("No Data")) {
                        list.add(field);
                        if (i == (ja.length()-1)) {
                            details.setFieldsexists(true);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoFieldsExists(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetStaffStatus(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String message = jo.getString("message");
                    if (message.equals("Success")) {
                        String Staff = jo.getString("staff_name");
                        String StaffId = jo.getString("staff_id");
                        list.add(Staff+","+StaffId);
                        if (i == (ja.length()-1)) {
                            details.setStaffExists(true);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoStaffExist(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetStaffStatusAddVisitor(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String message = jo.getString("message");
                    if (message.equals("Success")) {
                        String Staff = jo.getString("staff_name");
                        String StaffId = jo.getString("staff_id");
                        // list.add(Staff);
                        list.add(Staff/*+","+StaffId*/);

                        if (i == (ja.length()-1)) {
                            details.setStaffExists(true);
                            // hashSet.addAll(list);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoStaffExist(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VisitorsCheckinStatus(String result, DetailsValue details) {
        Log.d("debug", "Visitor Check in Status: "+result);
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setVisitorsId(jo.getString("visitors_id"));
                    details.setVisitorCheckedIn(true);
                } else {
                    details.setVisitorCheckInError(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VisitorsCheckOutStatus(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setVisitorsCheckOutSuccess(true);
                } else if (Status.equals("Failure")){
                    details.setVisitorsCheckOutFailure(true);
                } else {
                    details.setVisitorsCheckOutDone(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CheckVisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                    VisitorsAdapters adapters) {
        String Visitor_id, Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo, Visitors_CheckInTime, Visitors_CheckInBy, Visitors_BarCode, Check_in_User, Check_out_User,
                Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                Section, Student_Name, ID_Card, Blood_Group,Timebound_timings;
        Log.d("debug", "Check Visitors Status "+result);
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setVisitorsFound(true);
                        details = new DetailsValue();
                        Visitor_id = jo.getString("visitors_id");
                        details.setVisitor_ID(Visitor_id);
                        Visitors_Name = jo.getString("visitors_name");
                        details.setVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("visitors_email");
                        details.setVisitors_Email(Visitors_Email);
                        Visitors_Mobile = jo.getString("visitors_mobile");
                        details.setVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("visitors_address");
                        details.setVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("to_meet");
                        details.setVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("visitors_vehicle_number");
                        details.setVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("visitors_photo");
                        Visitors_Photo = Image_Url + Image;
                        details.setVisitors_Photo(Visitors_Photo);
                        Visitors_CheckInTime = jo.getString("checked_in_time");
                        details.setVisitors_CheckInTime(Visitors_CheckInTime);
                        Visitors_CheckInBy = jo.getString("check_in_by");
                        details.setVisitors_CheckInBy(Visitors_CheckInBy);
                        Visitors_BarCode = jo.getString("visitors_bar_code");
                        details.setVisitors_BarCode(Visitors_BarCode);
                        Check_in_User = jo.getString("check_in_by_name");
                        details.setCheck_in_User(Check_in_User);
                        Check_out_User = jo.getString("check_out_by_name");
                        details.setCheck_out_User(Check_out_User);
                        Visitor_Designation = jo.getString("visitor_designation");
                        details.setVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("department");
                        details.setDepartment(Department);
                        Purpose = jo.getString("purpose");
                        details.setPurpose(Purpose);
                        House_number = jo.getString("house_number");
                        details.setHouse_number(House_number);
                        Flat_number = jo.getString("flat_number");
                        details.setFlat_number(Flat_number);
                        Block = jo.getString("block");
                        details.setBlock(Block);
                        No_Visitor = jo.getString("no_visitor");
                        details.setNo_Visitor(No_Visitor);
                        aClass = jo.getString("class");
                        details.setaClass(aClass);
                        Section = jo.getString("section");
                        details.setSection(Section);
                        Student_Name = jo.getString("student_name");
                        details.setStudent_Name(Student_Name);
                        ID_Card = jo.getString("id_card_number");
                        details.setID_Card(ID_Card);
                       /* Blood_Group = jo.getString("blood_group");
                        details.setVisitor_blood_group(Blood_Group);*/
                        Timebound_timings=jo.optString("timebound_visitor_timings");
                        details.setTimeBound_timings(Timebound_timings);
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AllVisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                  VisitorsAdapters adapters) {
        String Visitor_id, Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo, Visitors_CheckInTime, Visitors_CheckInBy, Visitors_CheckOutTime, Visitors_BarCode, Check_in_User,
                Check_out_User, Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                Section, Student_Name, ID_Card,Blood_Group, Timebound_timings;
        Log.d("debug", "All Visitors Status "+result);
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setVisitorsFound(true);
                        details = new DetailsValue();
                        Visitor_id = jo.getString("visitors_id");
                        details.setVisitor_ID(Visitor_id);
                        Visitors_Name = jo.getString("visitors_name");
                        details.setVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("visitors_email");
                        details.setVisitors_Email(Visitors_Email);
                        Visitors_Mobile = jo.getString("visitors_mobile");
                        details.setVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("visitors_address");
                        details.setVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("to_meet");
                        details.setVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("visitors_vehicle_number");
                        details.setVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("visitors_photo");
                        Visitors_Photo = Image_Url + Image;
                        details.setVisitors_Photo(Visitors_Photo);
                        Visitors_CheckInTime = jo.getString("checked_in_time");
                        details.setVisitors_CheckInTime(Visitors_CheckInTime);
                        Visitors_CheckInBy = jo.getString("check_in_by");
                        details.setVisitors_CheckInBy(Visitors_CheckInBy);
                        Visitors_CheckOutTime = jo.getString("checked_out_time");
                        details.setVisitors_CheckOutTime(Visitors_CheckOutTime);
                        Visitors_BarCode = jo.getString("visitors_bar_code");
                        details.setVisitors_BarCode(Visitors_BarCode);
                        Check_in_User = jo.getString("check_in_by_name");
                        details.setCheck_in_User(Check_in_User);
                        Check_out_User = jo.getString("check_out_by_name");
                        details.setCheck_out_User(Check_out_User);
                        Visitor_Designation = jo.getString("visitor_designation");
                        details.setVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("department");
                        details.setDepartment(Department);
                        Purpose = jo.getString("purpose");
                        details.setPurpose(Purpose);
                        House_number = jo.getString("house_number");
                        details.setHouse_number(House_number);
                        Flat_number = jo.getString("flat_number");
                        details.setFlat_number(Flat_number);
                        Block = jo.getString("block");
                        details.setBlock(Block);
                        No_Visitor = jo.getString("no_visitor");
                        details.setNo_Visitor(No_Visitor);
                        aClass = jo.getString("class");
                        details.setaClass(aClass);
                        Section = jo.getString("section");
                        details.setSection(Section);
                        Student_Name = jo.getString("student_name");
                        details.setStudent_Name(Student_Name);
                        ID_Card = jo.getString("id_card_number");
                        details.setID_Card(ID_Card);
                       /* Blood_Group = jo.getString("blood_group");
                        details.setVisitor_blood_group(Blood_Group);*/
                        Timebound_timings=jo.optString("timebound_visitor_timings");
                        details.setTimeBound_timings(Timebound_timings);
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VisitorManualCheckout(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setVisitorsCheckOutSuccess(true);
                } else {
                    details.setVisitorsCheckOutFailure(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MobileAutoSuggestStatus(String result, DetailsValue details) {
        Log.d("debug", result);
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {

                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setMobileAutoSuggestSuccess(true);
                    details.setVisitors_Name(jo.getString("visitors_name"));
                    details.setVisitors_Email(jo.getString("visitors_email"));
                    details.setVisitors_Address(jo.getString("visitors_address"));
                    details.setVisitors_tomeet(jo.getString("to_meet"));
                    details.setVisitors_Photo(jo.getString("visitors_photo"));
                    details.setVisitors_VehicleNo(jo.getString("visitors_vehicle_number"));
                    details.setVisitor_Designation(jo.getString("visitor_designation"));
                    details.setDepartment(jo.getString("department"));
                    details.setPurpose(jo.getString("purpose"));
                    details.setHouse_number(jo.getString("house_number"));
                    details.setFlat_number(jo.getString("flat_number"));
                    details.setBlock(jo.getString("block"));
                    details.setNo_Visitor(jo.getString("no_visitor"));
                    details.setaClass(jo.getString("class"));
                    details.setSection(jo.getString("section"));
                    details.setStudent_Name(jo.getString("student_name"));
                    details.setID_Card(jo.getString("id_card_number"));
                    details.setID_Card_Type(jo.getString("id_card_type"));
                   // details.setVisitor_blood_group(jo.getString("blood_group"));
                    details.setTimebound_hour(jo.getString("default_hours"));
                    details.setTimebound_minute(jo.getString("default_minutes"));


                } else if (Status.equals("Failure")){
                    details.setTimebound_hour(jo.getString("default_hours"));
                    details.setTimebound_minute(jo.getString("default_minutes"));
                    details.setMobileAutoSuggestFailure(true);
                } else if (Status.equals("Already Logged")){
                    details.setMobileNoExist(true);
                }else{
                    details.setMobileNoSPAM(true);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                               VisitorsAdapters adapters) {
        Log.d("debug", "Search result: "+result);
        String Visitor_id, Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo, Visitors_CheckInTime, Visitors_CheckInBy, Visitors_BarCode, Check_in_User, Check_out_User,
                Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section,
                Student_Name, ID_Card, Visitors_CheckOutTime,Blood_Group;
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setVisitorsFound(true);
                        details = new DetailsValue();
                        Visitor_id = jo.getString("visitors_id");
                        details.setVisitor_ID(Visitor_id);
                        Visitors_Name = jo.getString("visitors_name");
                        details.setVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("visitors_email");
                        details.setVisitors_Email(Visitors_Email);
                        Visitors_Mobile = jo.getString("visitors_mobile");
                        details.setVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("visitors_address");
                        details.setVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("to_meet");
                        details.setVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("visitors_vehicle_number");
                        details.setVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("visitors_photo");
                        Visitors_Photo = Image_Url + Image;
                        details.setVisitors_Photo(Visitors_Photo);
                        Visitors_CheckInTime = jo.getString("checked_in_time");
                        details.setVisitors_CheckInTime(Visitors_CheckInTime);
                        Visitors_CheckInBy = jo.getString("check_in_by");
                        details.setVisitors_CheckInBy(Visitors_CheckInBy);
                        Visitors_CheckOutTime = jo.getString("checked_out_time");
                        details.setVisitors_CheckOutTime(Visitors_CheckOutTime);
                        Visitors_BarCode = jo.getString("visitors_bar_code");
                        details.setVisitors_BarCode(Visitors_BarCode);
                        Check_in_User = jo.getString("check_in_by_name");
                        details.setCheck_in_User(Check_in_User);
                        Check_out_User = jo.getString("check_out_by_name");
                        details.setCheck_out_User(Check_out_User);
                        Visitor_Designation = jo.getString("visitor_designation");
                        details.setVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("department");
                        details.setDepartment(Department);
                        Purpose = jo.getString("purpose");
                        details.setPurpose(Purpose);
                        House_number = jo.getString("house_number");
                        details.setHouse_number(House_number);
                        Flat_number = jo.getString("flat_number");
                        details.setFlat_number(Flat_number);
                        Block = jo.getString("block");
                        details.setBlock(Block);
                        No_Visitor = jo.getString("no_visitor");
                        details.setNo_Visitor(No_Visitor);
                        aClass = jo.getString("class");
                        details.setaClass(aClass);
                        Section = jo.getString("section");
                        details.setSection(Section);
                        Student_Name = jo.getString("student_name");
                        details.setStudent_Name(Student_Name);
                        ID_Card = jo.getString("id_card_number");
                        details.setID_Card(ID_Card);
                      /*  Blood_Group = jo.getString("blood_group");
                        details.setVisitor_blood_group(Blood_Group);*/
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PrintingFields_Status(String result, DetailsValue details, HashSet<String> OrderSet, HashSet<String> DisplaySet) {
        ArrayList<String> orderlist = new ArrayList<>();
        ArrayList<String> displaylist = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        String Visitors_Name = jo.getString("visitors_name");
                        if (!Visitors_Name.equals("")) {
                            orderlist.add(jo.getString("visitors_name_order"));
                            displaylist.add(jo.getString("visitors_name_order")+"_Name");
                        }
                        String Visitors_Mobile = jo.getString("visitors_mobile");
                        if (!Visitors_Mobile.equals("")) {
                            orderlist.add(jo.getString("visitors_mobile_order"));
                            displaylist.add(jo.getString("visitors_mobile_order")+"_Mobile");
                        }
                        String Visitors_Address = jo.getString("visitors_address");
                        if (!Visitors_Address.equals("")) {
                            orderlist.add(jo.getString("visitors_address_order"));
                            displaylist.add(jo.getString("visitors_address_order")+"_From");
                        }
                        String To_Meet = jo.getString("to_meet");
                        if (!To_Meet.equals("")) {
                            orderlist.add(jo.getString("to_meet_order"));
                            displaylist.add(jo.getString("to_meet_order")+"_To Meet");
                        }
                        String Check_in_time = jo.getString("checked_in_time");
                        if (!Check_in_time.equals("")) {
                            orderlist.add(jo.getString("checked_in_time_order"));
                            displaylist.add(jo.getString("checked_in_time_order")+"_Date");
                        }
                        String Visitors_Email = jo.getString("visitors_email");
                        if (!Visitors_Email.equals("")) {
                            orderlist.add(jo.getString("visitors_email_order"));
                            displaylist.add(jo.getString("visitors_email_order")+"_Email");
                        }
                        String visitors_vehicle_number = jo.getString("visitors_vehicle_number");
                        if (!visitors_vehicle_number.equals("")) {
                            orderlist.add(jo.getString("visitors_vehicle_number_order"));
                            displaylist.add(jo.getString("visitors_vehicle_number_order")+"_Vehicle Number");
                        }
                        String visitors_designation = jo.getString("visitors_designation");
                        if (!visitors_designation.equals("")) {
                            orderlist.add(jo.getString("visitors_designation_order"));
                            displaylist.add(jo.getString("visitors_designation_order")+"_Designation");
                        }
                        String department = jo.getString("department");
                        if (!department.equals("")) {
                            orderlist.add(jo.getString("department_order"));
                            displaylist.add(jo.getString("department_order")+"_Department");
                        }
                        String purpose = jo.getString("purpose");
                        if (!purpose.equals("")) {
                            orderlist.add(jo.getString("purpose_order"));
                            displaylist.add(jo.getString("purpose_order")+"_Purpose");
                        }
                        String house_no = jo.getString("house_no");
                        if (!house_no.equals("")) {
                            orderlist.add(jo.getString("house_no_order"));
                            displaylist.add(jo.getString("house_no_order")+"_House No");
                        }
                        String flat_no = jo.getString("flat_no");
                        if (!flat_no.equals("")) {
                            orderlist.add(jo.getString("flat_no_order"));
                            displaylist.add(jo.getString("flat_no_order")+"_Flat no");
                        }
                        String block = jo.getString("block");
                        if (!block.equals("")) {
                            orderlist.add(jo.getString("block_order"));
                            displaylist.add(jo.getString("block_order")+"_Block");
                        }

                        String no_visitor = jo.getString("no_visitor");
                        if (!no_visitor.equals("")) {
                            orderlist.add(jo.getString("no_visitor_order"));
                            displaylist.add(jo.getString("no_visitor_order")+"_Visitors");
                        }
                        String aclass = jo.getString("class");
                        if (!aclass.equals("")) {
                            orderlist.add(jo.getString("class_order"));
                            displaylist.add(jo.getString("class_order")+"_Class");
                        }
                        String section = jo.getString("section");
                        if (!section.equals("")) {
                            orderlist.add(jo.getString("section_order"));
                            displaylist.add(jo.getString("section_order")+"_Section");
                        }
                        String student_name = jo.getString("student_name");
                        if (!student_name.equals("")) {
                            orderlist.add(jo.getString("student_name_order"));
                            displaylist.add(jo.getString("student_name_order")+"_Student");
                        }
                        String id_card = jo.getString("id_card");
                        if (!id_card.equals("")) {
                            orderlist.add(jo.getString("id_card_order"));
                            displaylist.add(jo.getString("id_card_order")+"_Id Card");
                        }
                        String id_card_type = jo.getString("id_card_type");
                        if (!id_card_type.equals("")) {
                            orderlist.add(jo.getString("id_card_type_order"));
                            displaylist.add(jo.getString("id_card_type_order")+"_Id Card Type");
                        }

                        String Entry = jo.getString("entry_name");
                        if (!Entry.equals("")) {
                            orderlist.add(jo.getString("entry_order"));
                            displaylist.add(jo.getString("entry_order")+"_Entry");
                        }
                        /*String blood_group = jo.getString("blood_group");
                        if (!blood_group.equals("")) {
                            orderlist.add(jo.getString("blood_group_order"));
                            displaylist.add(jo.getString("blood_group_order")+"_Blood Group");
                        }*/
                        String Timebound = jo.getString("timebound_visitor_timings");
                        if (!Timebound.equals("")) {
                            orderlist.add(jo.getString("timebound_visitor_timings_order"));
                            displaylist.add(jo.getString("timebound_visitor_timings_order")+"_Time Bound");
                        }
                        OrderSet.addAll(orderlist);
                        DisplaySet.addAll(displaylist);
                        details.setPrinterOrderSuccess(true);
                    } else {
                        details.setPrinterOrderNoData(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PermissionStatus(String result, DetailsValue details) {
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Message = jo.getString("message");
                    if (Message.equals("Success")) {
                        details.setOTPAccess(jo.getString("otp_access"));
                        details.setImageAccess(jo.getString("image_access"));
                        details.setPrintertype(jo.getString("printer_type_name"));
                        details.setScannertype(jo.getString("scanner_name"));
                        details.setRfidStatus(jo.getString("rfid_status_name"));
                        details.setDeviceModel(jo.getString("device_model_name"));
                        Log.d("debug", "Device Model: "+jo.getString("device_model_name"));
                        details.setCameratype(jo.getString("camera_name"));
                        details.setPermissionSuccess(true);
                    } else {
                        details.setPermissionFailure(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ApkStatus(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String updateapk = jo.getString("apk");
                details.setApkfile(updateapk);
                String apkpath = Apk_Url + updateapk;
                details.setApkdownloadUrl(apkpath);
                details.setApkfilexist(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void TimeStatus(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                if (jo.getString("message").equals("Success")) {
                    details.setServerTime(jo.getString("current_time"));
                    details.setGotTime(true);
                } else {
                    details.setNoTime(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SmartCheckInOutStatus(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Checked In")) {
                    details.setSmartIn(true);
                } else if (Status.equals("Checked Out")){
                    details.setSmartOut(true);
                } else {
                    details.setSmartError(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AllAppointmentsDetails(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                       AppointmentAdapters adapters) {
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setAppointmentsFound(true);
                        details = new DetailsValue();
                        details.setVisitors_Name(jo.getString("visitor_name"));
                        details.setVisitors_Mobile(jo.getString("visitor_mobile"));
                        details.setVisitors_Email(jo.getString("visitor_email"));
                        details.setVisitors_tomeet(jo.getString("to_meet"));
                        details.setAppointmentDate(jo.getString("appointment_date"));
                        details.setAppointmentTime(jo.getString("appointment_time"));
                        details.setPurpose(jo.getString("purpose"));
                        details.setAppointmentLocation(jo.getString("location"));
                        details.setAppointmentFrom(jo.getString("visitors_from"));
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setAppointmentsNotFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AllAppointmentsSearchDetails(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                             AppointmentAdapters adapters) {
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setAppointmentsFound(true);
                        details = new DetailsValue();
                        details.setVisitors_Name(jo.getString("visitor_name"));
                        details.setVisitors_Mobile(jo.getString("visitor_mobile"));
                        details.setVisitors_Email(jo.getString("visitor_email"));
                        details.setVisitors_tomeet(jo.getString("to_meet"));
                        details.setAppointmentDate(jo.getString("appointment_date"));
                        details.setAppointmentTime(jo.getString("appointment_time"));
                        details.setPurpose(jo.getString("purpose"));
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setAppointmentsNotFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Adhoc visitor starts
    public void Adhoc_check_admin_password(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                /*String Response = jo.getString("message");
                if (Response.equals("Success")) {
                    details.setAdhocLoginSuccess(true);

                } else (Response.equals("Failure")) {
                    details.setAdhocLoginFailure(true);
                }*/

                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setAdhocLoginSuccess(true);
                } else if (Status.equals("Failure")){
                    details.setAdhocLoginFailure(true);
                }/* else {
                    details.setAdhocLoginFailure(true);
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void AdhocVisitorsCheckinStatus(String result, DetailsValue details) {
        Log.d("debug", "Adhoc Visitor Check in Status: "+result);
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setAdhocVisitorsId(jo.getString("adhoc_visitors_id"));
                    details.setAdhocVisitorCheckedIn(true);
                } else {
                    details.setAdhocVisitorCheckInError(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void AdhocSmartCheckInOutStatus(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Checked In")) {
                    details.setAdhocSmartIn(true);
                } else if (Status.equals("Checked Out")){
                    details.setAdhocSmartOut(true);
                } else if(Status.equals("Adhoc Visitor Expired")){
                    details.setAdhocSmartError(true);
                }else {
                    details.setAdhocSmartInvalidCard(true);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void AdhocDisplayFields(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String field = jo.getString("get_organization_fields_name");
                    if (!field.equals("No Data")) {
                        list.add(field);
                        if (i == (ja.length()-1)) {
                            details.setAdhocFieldsexists(true);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoAdhocFieldsExists(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AdhocMobileAutoSuggestStatus(String result, DetailsValue details) {
        Log.d("debug", result);
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setAdhocMobileAutoSuggestSuccess(true);
                    details.setAdhocVisitors_Name(jo.getString("adhoc_visitors_name"));
                    details.setAdhocVisitors_Email(jo.getString("adhoc_visitors_email"));
                    details.setAdhocVisitors_Address(jo.getString("adhoc_visitors_address"));
                    details.setAdhocVisitors_tomeet(jo.getString("adhoc_visitors_to_meet"));
                    details.setAdhoc_Contractor(jo.getString("contractors_name"));
                    details.setAdhoc_Contractor_id(jo.getString("contractors_id"));
                   // details.setAdhocVisitors_Photo(jo.getString("adhoc_visitors_photo"));
                    String Image = jo.getString("adhoc_visitors_photo");
                    String Visitors_Photo = Image_Url2 + Image;
                    details.setAdhocVisitors_Photo(Visitors_Photo);
                    details.setAdhocVisitors_VehicleNo(jo.getString("adhoc_visitors_vehicle_number"));
                    details.setAdhocVisitor_Designation(jo.getString("adhoc_visitor_designation"));
                    details.setAdhocDepartment(jo.getString("adhoc_visitors_department"));
                    details.setAdhocPurpose(jo.getString("adhoc_visitors_purpose"));
                    details.setAdhocHouse_number(jo.getString("adhoc_visitors_house_number"));
                    details.setAdhocFlat_number(jo.getString("adhoc_visitors_flat_number"));
                    details.setAdhocBlock(jo.getString("adhoc_visitors_block"));
                    details.setAdhocNo_Visitor(jo.getString("adhoc_visitors_no_visitor"));
                    details.setAdhocaClass(jo.getString("adhoc_visitors_class"));
                    details.setAdhocSection(jo.getString("adhoc_visitors_section"));
                    details.setAdhocStudent_Name(jo.getString("adhoc_visitors_student_name"));
                    details.setAdhocID_Card(jo.getString("adhoc_visitors_id_card_number"));
                    details.setAdhocID_Card_Type(jo.getString("adhoc_visitors_id_card_type"));
                    details.setAdhoc_issued_date(jo.getString("issued_date"));
                    details.setAdhoc_expiry_date(jo.getString("expiry_date"));
                    details.setAdhoc_Visitor_blood_group(jo.getString("adhoc_visitors_blood_group"));
                } else if (Status.equals("Failure")){
                    details.setAdhocMobileAutoSuggestFailure(true);
                } else {
                    details.setAdhocMobileNoExist(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//aadhoc view visitors
    public void AllAdhocVisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                  AdhocVisitorsAdapters adapters) {
        String Visitor_id, Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo, Visitors_Issued_Date, Visitors_CheckInBy, Visitors_Expiry_Date, Visitors_BarCode, Check_in_User,
                Check_out_User, Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                Section, Student_Name, ID_Card,ID_Card_Type,Blood_Group, Contractor;
        Log.d("debug", "All Visitors Status "+result);
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setAdhocVisitorsFound(true);
                        details = new DetailsValue();
                        Visitors_Name = jo.getString("adhoc_visitors_name");
                        details.setAdhocVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("adhoc_visitors_email");
                        details.setAdhocVisitors_Email(Visitors_Email);
                        Visitor_id=jo.getString("adhoc_visitors_id");
                        details.setAdhocVisitor_ID(Visitor_id);
                        Visitors_Mobile = jo.getString("adhoc_visitors_mobile");
                        details.setAdhocVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("adhoc_visitors_address");
                        details.setAdhocVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("adhoc_visitors_to_meet");
                        details.setAdhocVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("adhoc_visitors_vehicle_number");
                        details.setAdhocVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("adhoc_visitors_photo");
                        Visitors_Photo = Image_Url2 + Image;
                        details.setAdhocVisitors_Photo(Visitors_Photo);
                        Visitors_Issued_Date = jo.getString("issued_date");
                        details.setAdhoc_issued_date(Visitors_Issued_Date);
                        Visitors_Expiry_Date = jo.getString("expiry_date");
                        details.setAdhoc_expiry_date(Visitors_Expiry_Date);
                        Visitor_Designation = jo.getString("adhoc_visitor_designation");
                        details.setAdhocVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("adhoc_visitors_department");
                        details.setAdhocDepartment(Department);
                        Purpose = jo.getString("adhoc_visitors_purpose");
                        details.setAdhocPurpose(Purpose);
                        House_number = jo.getString("adhoc_visitors_house_number");
                        details.setAdhocHouse_number(House_number);
                        Flat_number = jo.getString("adhoc_visitors_flat_number");
                        details.setAdhocFlat_number(Flat_number);
                        Block = jo.getString("adhoc_visitors_block");
                        details.setAdhocBlock(Block);
                        No_Visitor = jo.getString("adhoc_visitors_no_visitor");
                        details.setAdhocNo_Visitor(No_Visitor);
                        aClass = jo.getString("adhoc_visitors_class");
                        details.setAdhocaClass(aClass);
                        Section = jo.getString("adhoc_visitors_section");
                        details.setAdhocSection(Section);
                        Student_Name = jo.getString("adhoc_visitors_student_name");
                        details.setAdhocStudent_Name(Student_Name);
                        ID_Card = jo.getString("adhoc_visitors_id_card_number");
                        details.setAdhocID_Card(ID_Card);
                        ID_Card_Type = jo.getString("adhoc_visitors_id_card_type");
                        details.setAdhocID_Card_Type(ID_Card_Type);
                        Blood_Group = jo.getString("adhoc_visitors_blood_group");
                        details.setAdhoc_Visitor_blood_group(Blood_Group);
                        Contractor=jo.getString("contractors_name");
                        details.setAdhoc_Contractor(Contractor);
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoAdhocVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AdhocVisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                               AdhocVisitorsAdapters adapters) {
        Log.d("debug", "Search result: "+result);
        String Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo,Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section,
                Student_Name, ID_Card,ID_Card_Type,Blood_group,Issued_date,Expiry_Date;
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setAdhocVisitorsFound(true);
                        details = new DetailsValue();
                        Visitors_Name = jo.getString("adhoc_visitors_name");
                        details.setAdhocVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("adhoc_visitors_email");
                        details.setAdhocVisitors_Email(Visitors_Email);
                        Visitors_Mobile = jo.getString("adhoc_visitors_mobile");
                        details.setAdhocVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("adhoc_visitors_address");
                        details.setAdhocVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("adhoc_visitors_to_meet");
                        details.setAdhocVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("adhoc_visitors_vehicle_number");
                        details.setAdhocVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("adhoc_visitors_photo");
                        Visitors_Photo = Image_Url2 + Image;
                        details.setAdhocVisitors_Photo(Visitors_Photo);
                        Visitor_Designation = jo.getString("adhoc_visitor_designation");
                        details.setAdhocVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("adhoc_visitors_department");
                        details.setAdhocDepartment(Department);
                        Purpose = jo.getString("adhoc_visitors_purpose");
                        details.setAdhocPurpose(Purpose);
                        House_number = jo.getString("adhoc_visitors_house_number");
                        details.setAdhocHouse_number(House_number);
                        Flat_number = jo.getString("adhoc_visitors_flat_number");
                        details.setAdhocFlat_number(Flat_number);
                        Block = jo.getString("adhoc_visitors_block");
                        details.setAdhocBlock(Block);
                        No_Visitor = jo.getString("adhoc_visitors_no_visitor");
                        details.setAdhocNo_Visitor(No_Visitor);
                        aClass = jo.getString("adhoc_visitors_class");
                        details.setAdhocaClass(aClass);
                        Section = jo.getString("adhoc_visitors_section");
                        details.setAdhocSection(Section);
                        Student_Name = jo.getString("adhoc_visitors_student_name");
                        details.setAdhocStudent_Name(Student_Name);
                        ID_Card = jo.getString("adhoc_visitors_id_card_number");
                        details.setAdhocID_Card(ID_Card);

                        ID_Card_Type = jo.getString("adhoc_visitors_id_card_type");
                        details.setAdhocID_Card_Type(ID_Card_Type);
                        Blood_group = jo.getString("adhoc_visitors_blood_group");
                        details.setAdhoc_Visitor_blood_group(Blood_group);
                        Issued_date = jo.getString("issued_date");
                        details.setAdhoc_issued_date(Issued_date);
                        Expiry_Date = jo.getString("expiry_date");
                        details.setAdhoc_expiry_date(Expiry_Date);

                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoAdhocVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Adhoc_Delete(String result, DetailsValue details) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
               String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setAdhocDeleteSuccess(true);
                } else if (Status.equals("Failure")){
                    details.setAdhocDeleteFailure(true);
                }/* else {
                    details.setAdhocLoginFailure(true);
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void GetContractorStatus(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String message = jo.getString("message");
                    if (message.equals("Success")) {
                        String Staff = jo.getString("contractors_name");
                        String StaffId = jo.getString("contractors_id");
                        list.add(Staff+","+StaffId);
                        if (i == (ja.length()-1)) {
                            details.setContactorsExists(true);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoContractorsExist(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Purpose field Fetching
    public void PurposeGetStaffStatus(String result, DetailsValue details, HashSet<String> hashSet) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String message = jo.getString("message");
                    if (message.equals("Success")) {
                        String Staff = jo.getString("auto_suggest_field_values_name");
                        list.add(Staff);
                        if (i == (ja.length()-1)) {
                            details.setStaffExists(true);
                            hashSet.addAll(list);
                        }
                    } else {
                        details.setNoStaffExist(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //SMS Expired Message

    public void SMSExpiry(String result, DetailsValue details) {

        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setSMSOTPSuccces(true);
                } else if(Status.equals("Failure")) {
                    details.setSMSOTPSucccesFailure(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void TimeBoundStatus(String result, DetailsValue details) {

        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Status = jo.getString("message");
                if (Status.equals("Success")) {
                    details.setTimeBoundSuccess(true);
                } else if(Status.equals("Failure")) {
                    details.setTimeBoundFailure(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Time Bound Overstay Visitors
    public void OverstayVisitorsStatus(String result, DetailsValue details, ArrayList<DetailsValue> arrayList,
                                    VisitorsAdapters adapters) {
        String Visitor_id, Visitors_Name, Visitors_Email, Visitors_Mobile, Visitors_Address, Visitors_tomeet, Visitors_VehicleNo = "",
                Visitors_Photo, Visitors_CheckInTime, Visitors_CheckInBy, Visitors_BarCode, Check_in_User, Check_out_User,
                Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass,
                Section, Student_Name, ID_Card, Blood_Group,Timebound_timings;
        Log.d("debug", "Overstay Visitors Status "+result);
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    String Status = jo.getString("message");
                    if (Status.equals("Success")) {
                        details.setVisitorsFound(true);
                        details = new DetailsValue();
                        Visitor_id = jo.getString("visitors_id");
                        details.setVisitor_ID(Visitor_id);
                        Visitors_Name = jo.getString("visitors_name");
                        details.setVisitors_Name(Visitors_Name);
                        Visitors_Email = jo.getString("visitors_email");
                        details.setVisitors_Email(Visitors_Email);
                        Visitors_Mobile = jo.getString("visitors_mobile");
                        details.setVisitors_Mobile(Visitors_Mobile);
                        Visitors_Address = jo.getString("visitors_address");
                        details.setVisitors_Address(Visitors_Address);
                        Visitors_tomeet = jo.getString("to_meet");
                        details.setVisitors_tomeet(Visitors_tomeet);
                        Visitors_VehicleNo = jo.getString("visitors_vehicle_number");
                        details.setVisitors_VehicleNo(Visitors_VehicleNo);
                        String Image = jo.getString("visitors_photo");
                        Visitors_Photo = Image_Url + Image;
                        details.setVisitors_Photo(Visitors_Photo);
                        Visitors_CheckInTime = jo.getString("checked_in_time");
                        details.setVisitors_CheckInTime(Visitors_CheckInTime);
                        Visitors_CheckInBy = jo.getString("check_in_by");
                        details.setVisitors_CheckInBy(Visitors_CheckInBy);
                        Visitors_BarCode = jo.getString("visitors_bar_code");
                        details.setVisitors_BarCode(Visitors_BarCode);
                        Check_in_User = jo.getString("check_in_by_name");
                        details.setCheck_in_User(Check_in_User);
                        Check_out_User = jo.getString("check_out_by_name");
                        details.setCheck_out_User(Check_out_User);
                        Visitor_Designation = jo.getString("visitor_designation");
                        details.setVisitor_Designation(Visitor_Designation);
                        Department = jo.getString("department");
                        details.setDepartment(Department);
                        Purpose = jo.getString("purpose");
                        details.setPurpose(Purpose);
                        House_number = jo.getString("house_number");
                        details.setHouse_number(House_number);
                        Flat_number = jo.getString("flat_number");
                        details.setFlat_number(Flat_number);
                        Block = jo.getString("block");
                        details.setBlock(Block);
                        No_Visitor = jo.getString("no_visitor");
                        details.setNo_Visitor(No_Visitor);
                        aClass = jo.getString("class");
                        details.setaClass(aClass);
                        Section = jo.getString("section");
                        details.setSection(Section);
                        Student_Name = jo.getString("student_name");
                        details.setStudent_Name(Student_Name);
                        ID_Card = jo.getString("id_card_number");
                        details.setID_Card(ID_Card);
                       /* Blood_Group = jo.getString("blood_group");
                        details.setVisitor_blood_group(Blood_Group);*/
                        Timebound_timings=jo.optString("timebound_visitor_timings");
                        details.setTimeBound_timings(Timebound_timings);
                        arrayList.add(details);
                        adapters.notifyDataSetChanged();
                    } else {
                        details.setNoVisitorsFound(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
