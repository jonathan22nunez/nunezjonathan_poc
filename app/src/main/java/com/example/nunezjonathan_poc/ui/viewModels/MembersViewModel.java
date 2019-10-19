package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.models.Member;
import com.example.nunezjonathan_poc.repos.MembersRepository;

import java.util.List;

public class MembersViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final MembersRepository mRepository;
    private LiveData<List<Member>> members;

    public MembersViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
        mRepository = new MembersRepository(mApplication);
        members = mRepository.getMembersList();
    }

    public LiveData<List<Member>> getMembers() {
        return members;
    }

    public void removeMember(Member member) {
        mRepository.removeMember(member);
    }
}
