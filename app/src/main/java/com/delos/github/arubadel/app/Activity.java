package com.delos.github.arubadel.app;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.delos.github.arubadel.R;
import com.delos.github.arubadel.util.ShellUtils;

/**
 * Created by: veli
 * Date: 10/20/16 4:05 PM
 */

public class Activity extends AppCompatActivity
{
    private ShellUtils mShellInstance;

    public ShellUtils getShellSession()
    {
        if (this.mShellInstance == null || this.mShellInstance.getSession() == null)
            loadShell();

        return this.mShellInstance;
    }

    protected void loadShell()
    {
        this.mShellInstance = new ShellUtils(this);
    }

    protected void init()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // try to find device code name
        if (!sp.contains("device_code_name"))
        {
            String deviceCode = null;

            if (Build.MODEL.equals("GT-I8552") || Build.MODEL.equals("I8552") || Build.MODEL.equals("GT-I8550") || Build.MODEL.equals("SAMSUNG I-8552"))
                deviceCode = "delos3geur";
            else if (Build.MODEL.equals("GT-I8262") || Build.MODEL.equals("GT-I8260"))
                deviceCode = "arubaslim";

            if (deviceCode == null)
                Toast.makeText(this, R.string.device_code_error, Toast.LENGTH_LONG).show();
            else
                sp.edit().putString("device_code_name", deviceCode).commit();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mShellInstance != null)
        {
            mShellInstance.closeSession();
            mShellInstance = null;
        }
    }
}
