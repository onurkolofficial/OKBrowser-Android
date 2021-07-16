package com.onurkol.app.browser.fragments.browser.tabs.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.browser.tabs.IncognitoTabListAdapter;
import com.onurkol.app.browser.interfaces.browser.tabs.TabSettings;

import java.util.ArrayList;
import java.util.List;

public class IncognitoTabListFragment extends Fragment implements TabSettings {
    // Elements
    GridView incognitoTabGridList;

    public static boolean isChanged=false;
    public static List<Integer> changedIndexList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.page_tablist_incognito, container, false);

        // Get Elements
        incognitoTabGridList=view.findViewById(R.id.incognitoTabGridList);

        // Set Event Listeners
        incognitoTabGridList.setOnTouchListener(clickToCloseListener);

        // Set Adapter
        incognitoTabGridList.setAdapter(new IncognitoTabListAdapter(getActivity(), incognitoTabGridList, BROWSER_INCOGNITO_TABDATA_LIST));

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
