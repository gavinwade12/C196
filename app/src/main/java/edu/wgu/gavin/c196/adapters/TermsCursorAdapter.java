package edu.wgu.gavin.c196.adapters;

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
import edu.wgu.gavin.c196.data.WGUContract;

public class TermsCursorAdapter extends CursorAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public TermsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.listitem_term, parent, false
        );
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final long id = cursor.getInt(cursor.getColumnIndex(WGUContract.Terms._ID));
        String title = cursor.getString(cursor.getColumnIndex(WGUContract.Terms.TITLE));
        Date startDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Terms.START_DATE)) * 1000);
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Terms.END_DATE)) * 1000);

        TextView titleView = view.findViewById(R.id.term_title);
        TextView startDateView = view.findViewById(R.id.term_startDate);
        TextView endDateView = view.findViewById(R.id.term_endDate);

        titleView.setText(title);
        startDateView.setText(mDateFormat.format(startDate));
        endDateView.setText(mDateFormat.format(endDate));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TermDetailActivity.class);
                intent.putExtra(WGUContract.Terms._ID, id);
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
