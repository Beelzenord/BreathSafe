package com.breathsafe.kth.breathsafe.Comments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Model.CommentsModel.Comment;
import com.breathsafe.kth.breathsafe.Model.CommentsModel.User;
import com.breathsafe.kth.breathsafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Activity to show a list of comments of a specific Location.
 */
public class CommentsActivity extends AppCompatActivity  implements CommentRecyclerAdapter.ItemClickListener {
    private static final String TAG = "CommentsActivity";
    private RecyclerView mRecyclerView;
    private CommentRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Comment> commentsToDisplay;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference comments;
    private FirebaseUser currentUser;
    private TextView locationName;
    private Button addCommentBtn;
    private String commentLocationID;
    private String commentLocationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Bundle extras = getIntent().getExtras();
        commentLocationID = extras.getString(getResources().getString(R.string.intent_extra_location_id));
        commentLocationName = extras.getString(getResources().getString(R.string.intent_extra_location_name));
        locationName = findViewById(R.id.comment_location_text);
        locationName.setText(commentLocationName);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        tryAddUserToDatabase();
        addCommentBtn = findViewById(R.id.comments_main_add_btn);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCommentActivity.class);
                intent.putExtra(getResources().getString(R.string.intent_extra_location_id), commentLocationID);
                intent.putExtra(getResources().getString(R.string.intent_extra_location_name), commentLocationName);
                startActivity(intent);
            }
        });

        createRecyclerView();
        findComments();
    }

    /**
     * Creates the recycler view to show comments.
     */
    private void createRecyclerView() {
        mRecyclerView = findViewById(R.id.comments_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        commentsToDisplay = new ArrayList<>();
        mAdapter = new CommentRecyclerAdapter(this, commentsToDisplay);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Finds comments for a specific location and puts them in the recycler view.
     */
    private void findComments() {
        DatabaseReference comments = myRef.child(getResources().getString(R.string.firebase_table_comments));
        Query query = comments.orderByChild("locationID").equalTo(commentLocationID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildAdded: key: " + dataSnapshot.getKey());
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentsToDisplay.add(comment);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Adds a new user to the database.
     */
    private void tryAddUserToDatabase() {
        Query query = myRef.child(getResources().getString(R.string.firebase_table_users)).child(currentUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: " + dataSnapshot.getKey());
                User u = dataSnapshot.getValue(User.class);
                if (u == null) {
                    Log.i(TAG, "onDataChange: u is null");
                    User newUser = new User(currentUser.getDisplayName(), currentUser.getEmail());
                    myRef.child(getResources().getString(R.string.firebase_table_users)).child(currentUser.getUid()).setValue(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: ");
            }
        });
    }

    /**
     * If a comment is clicked, show the whole comment in ReadCommentActivity.
     * @param view The view.
     * @param position The position of the item that was clicked.
     */
    @Override
    public void onItemClick(View view, int position) {
        Comment comment = mAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), ReadCommentActivity.class);
        intent.putExtra(getResources().getString(R.string.read_comment_extra_author), comment.getUsername());
        intent.putExtra(getResources().getString(R.string.read_comment_extra_timestamp), comment.getTimestamp());
        intent.putExtra(getResources().getString(R.string.read_comment_extra_title), comment.getTitle());
        intent.putExtra(getResources().getString(R.string.read_comment_extra_text), comment.getText());
        startActivity(intent);
    }
}
