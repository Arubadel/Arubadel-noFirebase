package com.delos.github.arubadel.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delos.github.arubadel.R;
import com.delos.github.arubadel.util.ShellExecuter;

/**
 * Created by sumit on 28/10/16.
 */

public class AboutDevice extends Fragment
{
    private TextView model_text;
    private TextView mplatform_release_text;
    private TextView mRil_class;
    private TextView mBoard;
    private TextView mKernel_version;
    private TextView mBuild_description;
    private TextView mBuild_fingerprint;
    private TextView mRoot_Status;
    private ShellExecuter Shell;
    String App_version = "";
    TextView mApp_version;

    public String model (){
        Shell.command="getprop ro.product.model";
        return Shell.runAsRoot();
}
    public String platform_release (){
        Shell.command="getprop ro.build.version.release";
        return Shell.runAsRoot();
    }
    public String ril_class (){
        Shell.command="getprop ro.telephony.ril_class";
        return Shell.runAsRoot();
    }
    public String board (){
        Shell.command="getprop ro.product.board";
        return Shell.runAsRoot();
    }
    public String kernel_version (){
        Shell.command="cat /proc/version";
        return Shell.runAsRoot();
    }
    public String build_description(){
        Shell.command="getprop ro.build.description";
        return Shell.runAsRoot();
    }
    public String build_fingerprint (){
        Shell.command="getprop ro.build.fingerprint";
        return Shell.runAsRoot();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_about_device, container, false);
        model_text=(TextView)rootView.findViewById(R.id.model_text);
        mplatform_release_text=(TextView)rootView.findViewById(R.id.platform_release_text);
        mRil_class=(TextView)rootView.findViewById(R.id.Ril_class);
        mBuild_fingerprint=(TextView)rootView.findViewById(R.id.build_fingerprint);
        mBuild_description=(TextView)rootView.findViewById(R.id.build_discription_text);
        mKernel_version=(TextView)rootView.findViewById(R.id.kernel_version_text);
        mBoard=(TextView)rootView.findViewById(R.id.board_text);
        mRoot_Status=(TextView)rootView.findViewById(R.id.root_status);
         mApp_version = (TextView) rootView.findViewById(R.id.app_version);


        try
        {
            App_version = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getApplicationContext().getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        mApp_version.setText("App Version :- "+ App_version+"\n");

        model_text.setText("Model Number :- " + model()+"\n");
        mplatform_release_text.setText("Device Version :- " + platform_release()+"\n");
        mRil_class.setText("Ril Class :- " + ril_class()+"\n");
        mBoard.setText("Device Board :- " + board()+"\n");
        mKernel_version.setText("Kernel Version :- " + kernel_version()+"\n");
        mBuild_description.setText("Build Description :- " + build_description()+"\n");
        mBuild_fingerprint.setText("Build FingerPrint :- " + build_fingerprint()+"\n");
        if(Shell.hasRoot()) {
            mRoot_Status.setText("Root Status :- Rooted" + "\n");
        }
        else {
            mRoot_Status.setText("Root Status :- Not Rooted :(" + "\n");
        }
        return rootView;
    }

}
