package edu.wgu.gavin.c196.adapters.cursor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.activities.TermDetailActivity;
import edu.wgu.gavin.c196.activities.TermsActivity;
import edu.wgu.gavin.c196.adapters.view.TermViewAdapter;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Term;

public class TermsCursorAdapter extends CursorAdapter {

    public TermsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.listitem_term, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Term term = Term.fromCursor(cursor);
        TermViewAdapter.from(term, view).render();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermDetailActivity.class);
                intent.putExtra(WGUContract.Terms._ID, term.id);
                if (!(context instanceof TermsActivity)) {
                    context.startActivity(intent);
                    return;
                }

                TermsActivity activity = (TermsActivity)context;
                activity.startActivityForResult(intent, TermsActivity.VIEW_TERM_REQUEST);
            }
        });
    }

}
