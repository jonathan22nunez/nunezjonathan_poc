package com.example.nunezjonathan_poc.interfaces;

import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Sleep;

public interface DatabaseListener {
    void createChildProfile(final Child child);
    void saveSleepActivity(final Sleep sleep);
}
