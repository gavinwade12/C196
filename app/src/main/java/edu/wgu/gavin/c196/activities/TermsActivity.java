package edu.wgu.gavin.c196.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.TermsCursorAdapter;
import edu.wgu.gavin.c196.data.WGUContract;

public class TermsActivity extends AppCompatActivity {

    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mCursor = getContentResolver().query(
                WGUContract.Terms.CONTENT_URI,
                WGUContract.Terms.COLUMNS,
                null,
                null,
                "_id ASC");
        if (mCursor == null) {
            Log.d(this.getLocalClassName(), "Terms query failed.");
            onBackPressed();
        }

        TermsCursorAdapter adapter = new TermsCursorAdapter(this, mCursor, 0);
        ListView termsList = findViewById(R.id.listTerms);
        termsList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null)
            mCursor.close();
        super.onDestroy();
    }
}
