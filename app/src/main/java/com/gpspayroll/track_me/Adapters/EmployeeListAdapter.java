package com.gpspayroll.track_me.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gpspayroll.track_me.AdminFragment.Payroll;
import com.gpspayroll.track_me.ModelClasses.StoreEmployeeData;
import com.gpspayroll.track_me.ModelClasses.StoreEmployees;
import com.gpspayroll.track_me.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreEmployeeData> storeEmployeeDataArrayList;
    DatabaseReference databaseReference;

    public EmployeeListAdapter(Context c, ArrayList<StoreEmployeeData> p) {
        context = c;
        storeEmployeeDataArrayList = p;
    }

    @NonNull
    @Override
    public EmployeeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_list_adapter, parent, false);

        return new EmployeeListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.MyViewHolder holder, int position) {
        StoreEmployeeData storeEmployeeData = storeEmployeeDataArrayList.get(position);

        String name = storeEmployeeData.getUsername();
        String phone = storeEmployeeData.getUserPhone();
        String email = storeEmployeeData.getUserEmail();

        holder.nameText.setText(name);
        holder.phoneText.setText(phone);
        holder.emailText.setText(email);

        holder.deleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Do you want to remove this employee ?");
                alertDialogBuilder.setIcon(R.drawable.exit);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            databaseReference.child(phone).removeValue();
                            Toast.makeText(context, "Employee Removed Permanently", Toast.LENGTH_SHORT).show();

                        } catch (Exception e){
                            Log.i("Error_Db", e.getMessage());
                        }
                    }
                });

                alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeEmployeeDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText, emailText;
        ImageView deleteEmployee;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nameText = itemView.findViewById(R.id.employeeNameListId);
            phoneText = itemView.findViewById(R.id.employeePhoneListId);
            emailText = itemView.findViewById(R.id.employeeEmailListId);

            deleteEmployee = itemView.findViewById(R.id.deleteEmployeeFromListId);
            databaseReference = FirebaseDatabase.getInstance().getReference("Employee Info");
        }
    }
}
