package com.example.nunezjonathan_poc.ui.viewModels;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class OverviewViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> mArrayList;

    public OverviewViewModel() {
        mArrayList = new MutableLiveData<>();
        mArrayList.setValue(new ArrayList<String>(){{
            add("Wet Diaper - 2:00 PM");
            add("Bottle Feeding - 1:15 PM");
            add("Sleep - 11:00 AM");
            add("Bottle Feeding - 10:15 AM");
        }});
    }

    public LiveData<ArrayList<String>> getArrayList() { return mArrayList; }
}
