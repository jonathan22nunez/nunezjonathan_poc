package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.repos.ChildRepository;

import java.util.List;

public class ChildrenViewModel extends AndroidViewModel {

    private final ChildRepository mRepository;
    private final LiveData<List<Child>> children;

    public ChildrenViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ChildRepository(application);
        children = mRepository.getChildren();
    }

    public LiveData<List<Child>> getChildren() {
        return children;
    }

    public void insert(Child child) {
        mRepository.insertChildData(child);
    }

    public void delete(Child child) { mRepository.deleteChildData(child); }
}
