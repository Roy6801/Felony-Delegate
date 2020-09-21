package com.project.felonydelegate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Report extends Fragment {

    Intent it;

    Button reportSuspicion,reportComplaint,reportWantedList,reportSOS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_report, container, false);
        perform(v);
        return  v;
    }

    private void perform(View v) {
        reportSuspicion = v.findViewById(R.id.reportSuspicion);
        reportWantedList = v.findViewById(R.id.reportWantedList);
        reportComplaint = v.findViewById(R.id.reportComplaint);
        reportSOS = v.findViewById(R.id.reportSOS);

        reportSuspicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent(getContext(),MainActivity2.class);
                it.putExtra("keyVal","Suspicion");
                startActivity(it);
                requireActivity().finish();
            }
        });

        reportWantedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent(getContext(),MainActivity2.class);
                it.putExtra("keyVal","Wanted");
                startActivity(it);
                requireActivity().finish();
            }
        });

        reportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent(getContext(),MainActivity2.class);
                it.putExtra("keyVal","Complaint");
                startActivity(it);
                requireActivity().finish();
            }
        });

        reportSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    it = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9769516763"));
                    Toast.makeText(getContext(),"Calling",Toast.LENGTH_SHORT).show();
                    startActivity(it);
                }
                else{
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout")){
            SharedPreferences sp = requireActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.remove("Pwd");
            ed.apply();
            Intent it = new Intent(requireContext(),LoginActivity.class);
            startActivity(it);
            requireActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}