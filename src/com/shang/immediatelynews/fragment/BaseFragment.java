package com.shang.immediatelynews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initFragmentView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initFragmentData(savedInstanceState);
	}
	
	public abstract void initFragmentData(Bundle savedInstanceState);

	public abstract View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
}
