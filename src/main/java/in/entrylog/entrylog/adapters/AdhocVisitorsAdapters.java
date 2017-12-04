package in.entrylog.entrylog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.main.CustomVolleyRequest;
import in.entrylog.entrylog.main.adhocVisitors.adhoc_el201.Adhoc_Visitor_Details_EL201;
import in.entrylog.entrylog.main.bluetooth.Visitor_Details_Bluetooth;
import in.entrylog.entrylog.main.el101_102.Visitor_Details_EL101;
import in.entrylog.entrylog.main.el201.Visitor_Details_EL201;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL101_102;
import in.entrylog.entrylog.values.FunctionCalls;

public class AdhocVisitorsAdapters extends RecyclerView.Adapter<AdhocVisitorsAdapters.VisitorsViewHolder> {

    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;
    ArrayList<DetailsValue> arrayList = new ArrayList<DetailsValue>();
    Context context;
    String ContextView, Organization_ID, CheckingUser, Device, PrinterType;
    FunctionCalls functionCalls = new FunctionCalls();
    EL101_102 el101_102device = new EL101_102();

    public AdhocVisitorsAdapters(ArrayList<DetailsValue> arrayList, Context context, String contextview, String organization_ID,
                                 String checkingUser, String device, String printertype) {
        this.arrayList = arrayList;
        this.context = context;
        this.ContextView = contextview;
        this.Organization_ID = organization_ID;
        this.CheckingUser = checkingUser;
        this.Device = device;
        this.PrinterType = printertype;
    }

    @Override
    public VisitorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adhoc_visitors_stay_cardview, parent, false);
        VisitorsViewHolder viewHolder = new VisitorsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VisitorsViewHolder holder, int position) {
        DetailsValue details = arrayList.get(position);
        Picasso.with(context).load(details.getVisitors_Photo()).into(holder.Visitor_Image);
        holder.imageLoader.get(details.getAdhocVisitors_Photo(), ImageLoader.getImageListener(holder.Visitor_Image,
                R.drawable.blankperson, R.drawable.blankperson));
        holder.Visitor_Image.setImageUrl(details.getAdhocVisitors_Photo(), holder.imageLoader);
        holder.tv_adhoc_visitor_name.setText(details.getAdhocVisitors_Name());
        holder.tv_adhoc_visitor_mobile.setText(details.getAdhocVisitors_Mobile());
        holder.tv_adhoc_visitor_issued_date.setText(details.getAdhoc_issued_date());
        holder.tv_adhoc_visitor_expiry_date.setText(details.getAdhoc_expiry_date());

        /*try {
            if (!details.getVisitors_CheckOutTime().equals("")) {
                holder.tv_adhoc_visitor_expiry_date.setText(functionCalls.Convertdate(details.getVisitors_CheckOutTime()));
            } else {
                holder.tv_adhoc_visitor_expiry_date.setText("Visitor still not checked out");
            }
        } catch (Exception e) {
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VisitorsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView Visitor_Image;
        ImageLoader imageLoader;
        TextView tv_adhoc_visitor_name, tv_adhoc_visitor_mobile, tv_adhoc_visitor_issued_date, tv_adhoc_visitor_expiry_date;

        public VisitorsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Visitor_Image = (NetworkImageView) itemView.findViewById(R.id.adhoc_visitors_card_image);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            tv_adhoc_visitor_name = (TextView) itemView.findViewById(R.id.adhoc_visitor_card_name);
            tv_adhoc_visitor_mobile = (TextView) itemView.findViewById(R.id.adhoc_visitor_card_mobile_number);
            tv_adhoc_visitor_issued_date = (TextView) itemView.findViewById(R.id.adhoc_visitor_card_issued_date);
            tv_adhoc_visitor_expiry_date = (TextView) itemView.findViewById(R.id.adhoc_visitor_card_expiry_date);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            DetailsValue details = arrayList.get(pos);

            /*String checkouttime = "";
            try {
                if (!details.getVisitors_CheckOutTime().equals("")) {
                    checkouttime = functionCalls.Convertdate(details.getVisitors_CheckOutTime());
                }
            } catch (Exception e) {
            }*/

            Intent intent ;

            intent = new Intent(context, Adhoc_Visitor_Details_EL201.class);

            intent.putExtra("View", ContextView);
            intent.putExtra("OrganizationID", Organization_ID);
            intent.putExtra("Printertype", PrinterType);
            intent.putExtra("Image", details.getAdhocVisitors_Photo());
            intent.putExtra("VisitorID", details.getAdhocVisitor_ID());
            intent.putExtra("Name", details.getAdhocVisitors_Name());
            intent.putExtra("Mobile", details.getAdhocVisitors_Mobile());
            intent.putExtra("Email", details.getAdhocVisitors_Email());
            intent.putExtra("VehicleNo", details.getAdhocVisitors_VehicleNo());
            intent.putExtra("From", details.getAdhocVisitors_Address());
            intent.putExtra("ToMeet", details.getAdhocVisitors_tomeet());
            intent.putExtra("VehicleNo", details.getAdhocVisitors_VehicleNo());
            intent.putExtra("visitor_designation", details.getAdhocVisitor_Designation());
            intent.putExtra("department", details.getAdhocDepartment());
            intent.putExtra("purpose", details.getAdhocPurpose());
            intent.putExtra("house_number", details.getAdhocHouse_number());
            intent.putExtra("flat_number", details.getAdhocFlat_number());
            intent.putExtra("block", details.getAdhocBlock());
            intent.putExtra("no_visitor", details.getAdhocNo_Visitor());
            intent.putExtra("class", details.getAdhocaClass());
            intent.putExtra("section", details.getAdhocSection());
            intent.putExtra("student_name", details.getAdhocStudent_Name());
            intent.putExtra("id_card_number", details.getAdhocID_Card());
            intent.putExtra("id_card_type",details.getAdhocID_Card_Type());
            intent.putExtra("Issued_date",details.getAdhoc_issued_date());
            intent.putExtra("Expiry_date",details.getAdhoc_expiry_date());
            intent.putExtra("Blood_group",details.getAdhoc_Visitor_blood_group());
            intent.putExtra("Contractors",details.getAdhoc_Contractor());
            /*context.startActivity(intent);*/
            ((Activity) context).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
        }
    }
}
