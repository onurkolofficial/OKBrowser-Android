package com.onurkol.app.browser.fragments.tabs.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.tabs.TabListAdapter;
import com.onurkol.app.browser.interfaces.browser.tabs.TabSettings;

import java.util.ArrayList;
import java.util.List;

public class TabListFragment extends Fragment implements TabSettings {
    // Elements
    GridView tabGridList;

    public static boolean isChanged=false;
    public static List<Integer> changedIndexList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.page_tablist, container, false);

        // Get Elements
        tabGridList=view.findViewById(R.id.tabGridList);

        // Set Event Listeners
        tabGridList.setOnTouchListener(clickToCloseListener);

        // Set Adapter
        tabGridList.setAdapter(new TabListAdapter(getActivity(), tabGridList, BROWSER_TABDATA_LIST, BROWSER_CLASSES_TABDATA_LIST));

        return view;
    }

    // Listeners
    boolean isScrolled=false;
    View.OnTouchListener clickToCloseListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                isScrolled = true;
            }
            else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                if(!isScrolled)
                    getActivity().finish();
                else
                    isScrolled=false;
            }
            view.performContextClick();
            return false;
        }
    };
}