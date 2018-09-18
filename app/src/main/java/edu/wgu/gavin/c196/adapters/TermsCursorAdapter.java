package edu.wgu.gavin.c196.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.Date;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.data.WGUContract;

public class TermsCursorAdapter extends CursorAdapter {

    public TermsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.listitem_note, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(WGUContract.Terms._ID));
        String title = cursor.getString(cursor.getColumnIndex(WGUContract.Terms.TITLE));
        Date startDate = new Date(cursor.getInt(cursor.getColumnIndex(WGUContract.Terms.START_DATE)));
        Date endDate = new Date(cursor.getInt(cursor.getColumnIndex(WGUContract.Terms.END_DATE)));
    }

}
