package com.example.instclone.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.instclone.R;
import com.example.instclone.adapters.MyAdapterFriendRequests;
import com.example.instclone.adapters.MyAdapterMyFriends;
import com.example.instclone.objects.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myFriendsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapterMyFriends myAdapterMyFriends;
    private ArrayList<user> users;
    private DatabaseReference databaseReference;
    private DatabaseReference findUser;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        setUpWidgets();
        showFriends();
    }

    private void showFriends() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String userId = snapshot1.getKey();
                    findUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot11:snapshot.getChildren()){
                                user User = snapshot11.getValue(user.class);
                                if(User.getUserId().equals(userId)){
                                    users.add(User);
                                    break;
                                }
                            }
                            myAdapterMyFriends.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    myAdapterMyFriends.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        recyclerView.setAdapter(myAdapterMyFriends);
    }

    private void setUpWidgets() {
        recyclerView = findViewById(R.id.myFriendsRecView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        myAdapterMyFriends = new MyAdapterMyFriends(getApplicationContext(),users);
        databaseReference = FirebaseDatabase.getInstance().getReference("SocialNetwork").child("Users").child(user.getUid()).child("Friends");
        findUser =FirebaseDatabase.getInstance().getReference("SocialNetwork").child("Users");

    }
}