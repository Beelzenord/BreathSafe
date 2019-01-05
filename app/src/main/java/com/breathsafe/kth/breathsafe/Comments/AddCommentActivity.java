package com.breathsafe.kth.breathsafe.Comments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Model.CommentsModel.Comment;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCommentActivity extends AppCompatActivity {
    private EditText titleText;
    private EditText textText;
    private Button cancelButton;
    private Button addButton;
    private String commentLocationID;
    private String commentLocationName;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Bundle extras = getIntent().getExtras();
        commentLocationID = extras.getString(getResources().getString(R.string.intent_extra_location_id));
        commentLocationName = extras.getString(getResources().getString(R.string.intent_extra_location_name));
        titleText = findViewById(R.id.add_comment_title);
        textText = findViewById(R.id.add_comment_text);
        cancelButton = findViewById(R.id.add_comment_cancel_btn);
        addButton = findViewById(R.id.add_comment_add_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNewComment();
            }
        });

    }

    private void handleNewComment() {
        if (titleText.length() <= 0) {
            showToast("You need a title");
            return;
        }
        if (textText.length() <= 0) {
            showToast("You need a text");
            return;
        }
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment comment = new Comment(
                commentLocationID, commentLocationName, currentUser.getUid(), currentUser.getDisplayName(),
                titleText.getText().toString(), textText.getText().toString(), System.currentTimeMillis()
        );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference newRef = myRef.child(getResources().getString(R.string.firebase_table_comments)).push();
        newRef.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Your comment has been added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
