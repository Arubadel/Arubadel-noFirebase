package com.delos.github.arubadel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.delos.github.arubadel.R;
import com.delos.github.arubadel.app.Activity;
import com.delos.github.arubadel.util.CPUTools;
import com.delos.github.arubadel.util.Config;
import com.delos.github.arubadel.util.ShellExecuter;
import com.delos.github.arubadel.util.ShellUtils;
import com.genonbeta.core.util.NetworkUtils;

import java.util.List;
import com.delos.github.arubadel.util.CPUInfo;
import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Sumit on 19.10.2016.
 */

public class OverAllDeviceInfo extends Fragment
{
    private ShellUtils mShell;
    private SwitchCompat mMSM_Hotplug;
    private ShellExecuter Shell;
    private TextView mGpuStatus;
    private TextView mBatteryVoltage;
    private TextView mBatteryTmp;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.mShell = ((Activity) getActivity()).getShellSession();

        View view = inflater.inflate(R.layout.fragment_all_device_info, container, false);
        mGpuStatus=(TextView)view.findViewById(R.id.Gpu_status_textview);
        mBatteryTmp=(TextView)view.findViewById(R.id.battery_tmp);
        mGpuStatus.setVisibility(View.GONE);
        mBatteryVoltage=(TextView)view.findViewById(R.id.battery_voltage);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };

        t.start();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    public String gpuStatus(){
        Shell.command="cat /sys/class/kgsl/kgsl-3d0/gpuclk ";
        return Shell.runAsRoot();
    }
    private String batterytmp(){
        Shell.command="cat /sys/class/power_supply/battery/batt_temp";
        return Shell.runAsRoot() ;
    }

    private String batteryvol(){
        Shell.command="cat /sys/class/power_supply/battery/batt_vol";
        return Shell.runAsRoot() ;
    }

    private void updateTextView() {
        if (Shell.hasGpu())
        {
            mGpuStatus.setVisibility(View.VISIBLE);
            mGpuStatus.setText("Gpu Status :- "+gpuStatus()+" hz");
        }
        mBatteryTmp.setText("Battery Tmp:- "+batterytmp());
        mBatteryVoltage.setText("Battery Voltage:- "+batteryvol()+"muv");
    }


}
