package com.delos.sumit.arubadel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delos.sumit.arubadel.R;

/**
 * Created by Veli on 18.10.2016.
 */

public class KernelUpdatesFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View mainView = inflater.inflate(R.layout.fragment_kernelupdates, container, false);

        return mainView;
    }
}
