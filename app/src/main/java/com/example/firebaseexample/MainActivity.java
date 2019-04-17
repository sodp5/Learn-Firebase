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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView tvID, tvKey, tvValue;
    private EditText edtID, edtKey, edtValue;
    private Button btnPush, btnRead, btnCreateId, btnLogin, btnLogOut;

    private String myKey;
    private boolean isLogin;

    private DatabaseReference databaseReference;

    private HashMap<String, String> memberListMap;
    private HashMap<String, String> memberDataMap;

    private final static String MEMBER_LIST = "MemberList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addButtonEvent();
        initInstance();
        setChildMap(memberListMap, MEMBER_LIST);

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
        btnLogin = findViewById(R.id.btnLogin);
        btnLogOut = findViewById(R.id.btnLogOut);
    }

    private void initInstance() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        memberListMap = new HashMap<>();
        memberDataMap = new HashMap<>();
    }

    private void addButtonEvent() {
        btnPush.setOnClickListener(btnPushClickEvent);
        btnRead.setOnClickListener(btnReadClickEvent);
        btnCreateId.setOnClickListener(btnCreateIdClickEvent);
        btnLogin.setOnClickListener(btnLoginClickEvent);
        btnLogOut.setOnClickListener(btnLogOutClickEvent);
    }

    private View.OnClickListener btnCreateIdClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String myId = edtID.getText().toString();
            setChildMap(memberListMap, MEMBER_LIST);

            // password를 묻는 문장도 추가 되어야 할것.
            if (isExistId(myId))
                Toast.makeText(MainActivity.this, "이미 존재하는 ID 입니다!", Toast.LENGTH_SHORT).show();
            else if (myId.length() <= 0) {
                Toast.makeText(MainActivity.this, "ID를 입력 해 주세요!", Toast.LENGTH_SHORT).show();
            }
            else {
                databaseReference.child(MEMBER_LIST).child(myId).setValue("password");
            }
        }
    };

    private View.OnClickListener btnPushClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isLogin) {
                Toast.makeText(MainActivity.this, "로그인을 먼저 해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edtKey.getText().toString().length() <= 0) {
                Toast.makeText(MainActivity.this, "key를 입력해주세요!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edtValue.getText().toString().length() <= 0) {
                Toast.makeText(MainActivity.this, "value를 입력해주세요!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseReference.child(myKey).child(edtKey.getText().toString()).setValue(edtValue.getText().toString());
            Toast.makeText(MainActivity.this, "값을 추가했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener btnReadClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setChildMap(memberDataMap, makePathString(MEMBER_LIST, myKey));
        }
    };

    private View.OnClickListener btnLoginClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String id = edtID.getText().toString();

            if (id.length() <= 0) {
                Toast.makeText(MainActivity.this, "ID를 입력 해주세요!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isLogin) {
                // 패스워드도 일치하는지 체크하는 코드가 생겨야 할 것
                if (isExistId(id)) {
                    isLogin = true;
                    myKey = id;
                    Toast.makeText(MainActivity.this, id + "님 로그인 되셨습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "존재하지 않는 ID 입니다!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "이미 로그인 중입니다!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener btnLogOutClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLogin) {
                isLogin = false;
                Toast.makeText(MainActivity.this, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MainActivity.this, "로그인중이 아닙니다!", Toast.LENGTH_SHORT).show();
        }
    };


    private void setChildMap(final HashMap<String, String> map, String key) {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    map.put(ds.getKey(), ds.getValue(String.class));

                    Log.d("키값", ds.getKey());
                    Log.d("밸류값", map.get(ds.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String makePathString(String... str) {
        StringBuilder sb = new StringBuilder();

        sb.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            sb.append("/").append(str[i]);
        }

        return sb.toString();
    }

    private boolean isExistId(String id) {
        return memberListMap.containsKey(id);
    }
}

