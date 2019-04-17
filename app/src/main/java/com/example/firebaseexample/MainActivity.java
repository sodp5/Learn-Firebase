package com.example.firebaseexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvID, tvKey, tvValue;
    private EditText edtID, edtKey, edtValue;
    private Button btnPush, btnRead, btnCreateId;

    private String myKey;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addButtonEvent();
        initInstance();

    }

    private void initView() {
        tvID = findViewById(R.id.tvID);
        tvKey = findViewById(R.id.tvKey);
        tvValue = findViewById(R.id.tvValue);

        edtID = findViewById(R.id.edtID);
        edtKey = findViewById(R.id.edtKey);
        edtValue = findViewById(R.id.edtValue);

        btnPush = findViewById(R.id.btnPush);
        btnRead = findViewById(R.id.btnRead);
        btnCreateId = findViewById(R.id.btnCreateId);
    }

    private void initInstance() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void addButtonEvent() {
        btnPush.setOnClickListener(btnPushClickEvent);
        btnRead.setOnClickListener(btnReadClickEvent);
        btnCreateId.setOnClickListener(btnCreateIdClickEvent);
    }

    private View.OnClickListener btnCreateIdClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference.child("MemberList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("키값", ds.getKey());
                        Log.d("밸류값", ds.getValue() + "");
//                        if(ds.getKey().equals("1"))
//                            break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    private View.OnClickListener btnPushClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            databaseReference = FirebaseDatabase.getInstance().getReference(edtID.getText().toString());
        }
    };

    private View.OnClickListener btnReadClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}

