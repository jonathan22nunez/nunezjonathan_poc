package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.repos.ChildRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class ChildrenViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final ChildRepository mRepository;
    private LiveData<List<Child>> children;
    private LiveData<List<Child>> childrenRoom;

    public ChildrenViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new ChildRepository(mApplication);
        children = mRepository.getChildren();
//        childrenRoom = mRepository.getChildrenRoom();
    }

    public LiveData<List<Child>> getChildren() {
        return children;
    }

    public void insert(Child child) {
        mRepository.insertChildData(child);
    }

    public void delete(Child child) { mRepository.deleteChildData(child); }
}
