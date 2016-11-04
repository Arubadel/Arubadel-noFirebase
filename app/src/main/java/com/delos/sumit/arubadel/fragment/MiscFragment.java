package com.delos.sumit.arubadel.fragment;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.delos.sumit.arubadel.R;
import com.delos.sumit.arubadel.app.Activity;
import com.delos.sumit.arubadel.util.CPUTools;
import com.delos.sumit.arubadel.util.Config;
import com.delos.sumit.arubadel.util.ShellUtils;
import com.genonbeta.core.util.NetworkUtils;

import java.util.List;
import com.delos.sumit.arubadel.util.CPUInfo;
import eu.chainfire.libsuperuser.Shell;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Sumit on 19.10.2016.
 */

public class MiscFragment extends Fragment
{
    private ShellUtils mShell;
    private TextView mInfoText;
    private SwitchCompat mADBSwitcher;
    private SwitchCompat mFastChargeSwitcher;
    private SwitchCompat mMPDecision;
    private Button mGovernor;
    private Button mTcp;
    private  SwitchCompat mDeepSleep;
    private  Button mGpuFreq;
    private CPUInfo mCPUInfo = new CPUInfo();
    private SwitchCompat mMSM_Hotplug;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.mShell = ((Activity) getActivity()).getShellSession();

        View view = inflater.inflate(R.layout.fragment_misc, container, false);

        mADBSwitcher = (SwitchCompat) view.findViewById(R.id.fragment_misc_adb_switcher);
        mInfoText = (TextView) view.findViewById(R.id.fragment_misc_info_text);
        mFastChargeSwitcher = (SwitchCompat) view.findViewById(R.id.fragment_misc_fastcharge_switch);
        mMPDecision = (SwitchCompat) view.findViewById(R.id.fragment_cputools_mpdecision_switch);
        mGovernor =(Button)view.findViewById(R.id.governor_button);
        mTcp =(Button)view.findViewById(R.id.tcp_congestion_control);
        mDeepSleep=(SwitchCompat)view.findViewById(R.id.fragment_misc_deep_sleep);
        mGpuFreq=(Button)view.findViewById(R.id.gpu_freq_control);
        mMSM_Hotplug=(SwitchCompat)view.findViewById(R.id.fragment_cputools_msm_mpdecision_hotplug_switch);

        this.mDeepSleep.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mShell.getSession().addCommand("echo " + mCPUInfo.speedMaxAllowed + " > " + Config.PATH_CPUS + "/cpu0/cpufreq/scaling_max_freq");
                        mShell.getSession().addCommand("echo " + "0" + " > " + Config.PATH_CPUS + "/cpu1/online");
                        mShell.getSession().addCommand("echo " + "0" + " > " + Config.PATH_CPUS + "/cpu2/online");
                        mShell.getSession().addCommand("echo " + "0" + " > " + Config.PATH_CPUS + "/cpu3/online");
                        mShell.getSession().addCommand("input keyevent KEYCODE_POWER");
                        getActivity().finish();
                        getActivity().moveTaskToBack(true);
                    }
                }
        );

        this.mGovernor.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        GovernorOptionDialogFragment fragment = new GovernorOptionDialogFragment();
                        fragment.show(getActivity().getSupportFragmentManager(), "power_dialog_fragment");
                    }
                }
        );

        this.mTcp.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        TcpOptionDialogFragment fragment = new TcpOptionDialogFragment();
                        fragment.show(getActivity().getSupportFragmentManager(), "power_dialog_fragment");
                    }
                }
        );


        this.mGpuFreq.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        GpuOptionDialogFragment fragment = new GpuOptionDialogFragment();
                        fragment.show(getActivity().getSupportFragmentManager(), "power_dialog_fragment");
                    }
                }
        );



        {

            mADBSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mShell.getSession().addCommand(((isChecked) ? "setprop service.adb.tcp.port 5555 ; stop adbd ; start adbd" : " setprop service.adb.tcp.port -1 ; stop adbd ; start adbd"));
                }
            });

            mFastChargeSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mShell.getSession().addCommand("echo " + ((isChecked) ? 1 : 0) + " > sys/kernel/fast_charge/force_fast_charge\n");
                }
            });
        }

        // Detected: mpdecision (make visible)
        if (CPUTools.hasMPDecision())
        {

            mMPDecision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mShell.getSession().addCommand(((isChecked) ? "mount -o rw,remount,rw /system ; chmod 777 /system/bin/mpdecision; start mpdecision" : "mount -o rw,remount,rw /system ; chmod 664 /system/bin/mpdecision ; killall mpdecision; stop mpdecision"));
                }
            });
        }

        mMSM_Hotplug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShell.getSession().addCommand("echo " + ((isChecked) ? 1 : 0) + " > /sys/kernel/msm_mpdecision/conf/enabled\n");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

            mShell.getSession().addCommand("getprop service.adb.tcp.port", 10, new Shell.OnCommandResultListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                    if (output.size() > 0)
                        mADBSwitcher.setChecked(!"-1".equals(output.get(0)));
                }
            });

            mShell.getSession().addCommand("cat sys/kernel/fast_charge/force_fast_charge", 10, new Shell.OnCommandResultListener() {
                @Override
                public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                    if (output.size() > 0)
                        mFastChargeSwitcher.setChecked("1".equals(output.get(0)));
                }
            });


        mShell.getSession().addCommand("cat /sys/kernel/msm_mpdecision/conf/enabled", 10, new Shell.OnCommandResultListener() {
            @Override
            public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                if (output.size() > 0)
                    mMSM_Hotplug.setChecked("1".equals(output.get(0)));
            }
        });

            List<String> availableNetworks = NetworkUtils.getInterfacesWithOnlyIp(true, new String[]{"rmnet"});

            if (availableNetworks.size() > 0)
                mInfoText.setText("adb connect " + availableNetworks.get(0) + ":5555");
            mShell.getSession().addCommand("pgrep mpdecision", 10, new Shell.OnCommandResultListener()
            {
                @Override
                public void onCommandResult(int commandCode, int exitCode, List<String> output)
                {
                    if (exitCode == 0)
                        mMPDecision.setChecked(output.size() > 0);
                }
            });


    }
}
