package com.anarul.skwhatsapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anarul.skwhatsapp.Adapters.StatusAdapter;
import com.anarul.skwhatsapp.Adapters.UsersAdapter;
import com.anarul.skwhatsapp.R;
import com.anarul.skwhatsapp.SettingActivity;
import com.anarul.skwhatsapp.databinding.FragmentStatusBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StatusFragment extends Fragment {



    public StatusFragment() {
        // Required empty public constructor
    }
     FirebaseStorage storage;
    FirebaseAuth auth;
     private FragmentStatusBinding binding;

    StatusAdapter statusAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentStatusBinding.inflate(inflater,container,false);
        getProfile();




        return binding.getRoot();
    }
    public void getProfile()
    {
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        String userId=auth.getCurrentUser().getUid();

        final StorageReference reference=storage.getReference().child("profile_pictures")
                .child(userId);

            }


}