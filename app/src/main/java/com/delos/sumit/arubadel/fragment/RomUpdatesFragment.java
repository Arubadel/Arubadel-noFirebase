package com.delos.sumit.arubadel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.delos.sumit.arubadel.R;
import com.delos.sumit.arubadel.adapter.SimpleFragmentPagerAdapter;
import com.delos.sumit.arubadel.util.Config;

/**
 * Created by sumit on 25/10/16.
 */

public class RomUpdatesFragment extends Fragment
{
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private Fragment mFragmentBetaReleases;
    private Fragment mFragmentStableReleases;
    private SimpleFragmentPagerAdapter mFragmentPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_rom, container, false);

        this.mPager = (ViewPager) view.findViewById(R.id.fragment_rom_view_pager);
        this.mTabLayout = (TabLayout) view.findViewById(R.id.fragment_rom_tab_layout);

        this.mFragmentBetaReleases = new GithubReleasesFragment().setTargetURL(Config.URL_ROM_STABLE_RELEASE);
        this.mFragmentStableReleases = new GithubReleasesFragment().setTargetURL(Config.URL_ROM_BETA_RELEASE);

        this.mFragmentPager = new SimpleFragmentPagerAdapter(getFragmentManager(), getActivity(), new Fragment[]{mFragmentStableReleases, mFragmentBetaReleases}, new String[]{getString(R.string.stable), getString(R.string.beta)});

        this.mPager.setAdapter(this.mFragmentPager);
        this.mTabLayout.setupWithViewPager(this.mPager);

        return view;
    }

}
