package com.breathsafe.kth.breathsafe.Comments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.R;

import java.util.Calendar;

/**
 * Lets the user read a specific comment.
 */
public class ReadCommentActivity extends AppCompatActivity {
    private TextView authorTextView;
    private TextView timestampTextView;
    private TextView titleTextView;
    private TextView textTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comment);
        authorTextView = findViewById(R.id.read_comment_author);
        timestampTextView = findViewById(R.id.read_comment_time);
        titleTextView = findViewById(R.id.read_comment_title);
        textTextView = findViewById(R.id.read_comment_text);
        Bundle extras = getIntent().getExtras();
        String author = extras.getString(getResources().getString(R.string.read_comment_extra_author));
        long timestamp = extras.getLong(getResources().getString(R.string.read_comment_extra_timestamp));
        String title = extras.getString(getResources().getString(R.string.read_comment_extra_title));
        String text = extras.getString(getResources().getString(R.string.read_comment_extra_text));
        StringBuilder sb1 = new StringBuilder();
        sb1.append("Author: "); sb1.append(author);
        authorTextView.setText(sb1);

        sb1 = new StringBuilder();
//        sb1.append("Created: "); sb1.append(createTimestampString(timestamp));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        sb1.append("Created: "); sb1.append(calendar.getTime().toString());
        timestampTextView.setText(sb1);

        sb1 = new StringBuilder();
        sb1.append("Title: "); sb1.append(title);
        titleTextView.setText(sb1);

        textTextView.setText(text);
    }

    /**
     * Creates a string date using a long timestamp.
     * @param timestamp The timestamp in milliseconds since 1970-01-01.
     * @return A string date.
     */
    private String createTimestampString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR)); sb.append("-");
        sb.append(extraZero(calendar.get(Calendar.MONTH))); sb.append("-");
        sb.append(extraZero(calendar.get(Calendar.DAY_OF_MONTH)));
        return sb.toString();
    }

    private String extraZero(int i) {
        if (i < 10)
            return "0"+i;
        return ""+i;
    }
}
