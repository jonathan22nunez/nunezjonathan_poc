package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;

public class Member {

    public String id;
    public String name;
    public String email;

    public Member() {}

    @NonNull
    @Override
    public String toString() {
        return name + "\nemail: " + email;
    }
}
