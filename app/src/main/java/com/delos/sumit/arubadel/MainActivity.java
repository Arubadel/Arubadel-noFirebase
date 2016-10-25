package com.delos.sumit.arubadel;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.delos.sumit.arubadel.app.Activity;
import com.delos.sumit.arubadel.fragment.CPUToolsFragment;
import com.delos.sumit.arubadel.fragment.CreditsFragment;
import com.delos.sumit.arubadel.fragment.KernelUpdatesFragment;
import com.delos.sumit.arubadel.fragment.MiscFragment;
import com.delos.sumit.arubadel.fragment.PowerFragment;
import com.delos.sumit.arubadel.fragment.PowerOptionsDialogFragment;
import com.delos.sumit.arubadel.fragment.AppRelease;
import com.delos.sumit.arubadel.fragment.RecoveryUpdatesFragment;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener
{
    // Keep fragments in memory and load once to use less memory
    public KernelUpdatesFragment mFragmentKernelUpdates;
    public CPUToolsFragment mFragmentCPUTools;
    public CreditsFragment mFragmentCredits;
    public PowerFragment mFragmentPower;
    public MiscFragment mMisc;
    private FloatingActionButton mFAB;
    public Fragment mAppRelease;
    public Fragment mFragmentrecoveryUpdates;

    public Fragment mActiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.loadShell();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.mFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        this.mFragmentKernelUpdates = new KernelUpdatesFragment();
        this.mFragmentCPUTools = new CPUToolsFragment();
        this.mFragmentCredits = new CreditsFragment();
        this.mFragmentPower = new PowerFragment();
        this.mMisc = new MiscFragment();
        this.mAppRelease = new AppRelease();
        this.mFragmentrecoveryUpdates = new RecoveryUpdatesFragment();

        // register click listener for fab
        this.mFAB.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PowerOptionsDialogFragment fragment = new PowerOptionsDialogFragment();
                        fragment.show(getSupportFragmentManager(), "power_dialog_fragment");
                    }
                }
        );

        this.updateFragment(this.mFragmentCPUTools);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_fragment1)
        {
            this.updateFragment(this.mFragmentCPUTools);
        }
        else if (id == R.id.nav_power)
        {
            this.updateFragment(this.mFragmentPower);
        }
        else if (id == R.id.nav_misc)
        {
            this.updateFragment(this.mMisc);
        }

        else if (id == R.id.nav_AppRelease)
        {
            this.updateFragment(this.mAppRelease);
        }

        else if (id == R.id.nav_kernel_updates)
        {
            this.updateFragment(this.mFragmentKernelUpdates);
        }
        else if (id == R.id.nav_recovery)
        {
            this.updateFragment(this.mFragmentrecoveryUpdates);
        }
        else if (id == R.id.nav_credits)
        {
            this.updateFragment(this.mFragmentCredits);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    protected void updateFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}
