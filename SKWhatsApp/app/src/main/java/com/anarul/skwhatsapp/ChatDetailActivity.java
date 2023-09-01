package com.anarul.skwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anarul.skwhatsapp.Adapters.ChatAdapter;
import com.anarul.skwhatsapp.Models.MessageModel;
import com.anarul.skwhatsapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;// used for retrive user id to authentication site
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();//remove head SKWhatsApp tool bar

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

       final String senderId=auth.getUid();
        String recieveId =getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilepic");

        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_profile).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);//for backarrow click ,control transfer to main activity
                startActivity(intent);
            }
        });
        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,recieveId);
        binding.chatRecyclarView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclarView.setLayoutManager(layoutManager);


        final String senderRoom=senderId + recieveId;
        final String receiverRoom=recieveId + senderId;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    MessageModel model=snapshot1.getValue(MessageModel.class);

                    model.setMessageId(snapshot1.getKey());
                    messageModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();//recent chat views in recycle view
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etMessage.getText().toString().isEmpty()){
                    binding.etMessage.setError("Plz Enter Any Message");
                    return;}
                  String message=binding.etMessage.getText().toString();
                  final MessageModel model=new MessageModel(senderId,message);
                  model.setTimestamp(new Date().getTime());//time er jonno use korlam
                  binding.etMessage.setText("");// sender por text view ke empty korar jonno use kor6i

                database.getReference().child("Chats").child(senderRoom).push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
}