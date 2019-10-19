package com.example.nunezjonathan_poc.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.models.Member;
import com.example.nunezjonathan_poc.ui.viewModels.MembersViewModel;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LinkedAccountsFragment extends Fragment {

    private MembersViewModel viewModel;
    private ListView listView;
    private List<Member> membersList;
    private ArrayAdapter<Member> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_linked_accounts, container, false);
        viewModel = ViewModelProviders.of(this).get(MembersViewModel.class);
        viewModel.getMembers().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(List<Member> members) {
                membersList = members;
                adapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, membersList);
                listView.setAdapter(adapter);
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.account_manage_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Remove Member");
                builder.setMessage("You are about to remove a member. " +
                        "They will lose the ability to view items in your Family. " +
                        "You can always invite them to join your Family at a later time.");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.removeMember(membersList.get(position));
                        membersList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_member) {
            FirebaseUser user = FirestoreDatabase.getCurrentUser();
            if (user != null && getContext() != null && OptionalServices.cloudSyncEnabled(getContext())) {
                SharedPreferences sharedPrefs = getContext().getSharedPreferences("familyData", Context.MODE_PRIVATE);
                String familyId = sharedPrefs.getString("family_id", null);
                if (familyId != null) {
                    String messageToSend = "You have been asked to participate with " +
                            user.getDisplayName() + " to track a little one's activities using NüBaby. " +
                            "Download the app and use the provided code, found below, to " +
                            "to link up and start tracking. \n\n" +
                            "Code: " + familyId;
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
                    intent.putExtra("sms_body", messageToSend);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
