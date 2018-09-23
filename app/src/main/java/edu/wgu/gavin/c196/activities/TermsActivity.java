package edu.wgu.gavin.c196.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.cursor.TermsCursorAdapter;
import edu.wgu.gavin.c196.data.WGUContract;

public class TermsActivity extends AppCompatActivity {

    public static int CREATE_TERM_REQUEST = 1;
    public static int VIEW_TERM_REQUEST = 2;

    private Cursor mCursor;
    private ListView mTermsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        mTermsList = findViewById(R.id.listTerms);
        displayTerms();
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null && !mCursor.isClosed())
            mCursor.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_new)
            return super.onOptionsItemSelected(item);

        Intent intent = new Intent(this, EditTermActivity.class);
        startActivityForResult(intent, CREATE_TERM_REQUEST);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CREATE_TERM_REQUEST && requestCode != VIEW_TERM_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        displayTerms();
    }

    private void displayTerms() {
        if (mCursor != null)
            mCursor.close();

        mCursor = getContentResolver().query(
                WGUContract.Terms.CONTENT_URI,
                WGUContract.Terms.COLUMNS,
                null,
                null,
                "_id ASC");
        if (mCursor == null) {
            Log.d(this.getLocalClassName(), "Terms query failed.");
            finish();
            return;
        }

        TermsCursorAdapter adapter = new TermsCursorAdapter(this, mCursor, 0);
        mTermsList.setAdapter(adapter);
    }
}
