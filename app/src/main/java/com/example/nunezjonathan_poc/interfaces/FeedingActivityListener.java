package com.example.nunezjonathan_poc.interfaces;

import android.os.Bundle;

public interface FeedingActivityListener {
    void viewLog();
    void viewChildren();
    void manualNurseEntry();
    void manualBottleEntry();
    void inputBottleDetails(Bundle bundle);
}
