package edu.wgu.gavin.c196.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wgu.gavin.c196.data.WGUContract;

public class Term {
    public long id;
    public String title;
    public Date startDate;
    public Date endDate;
    public List<Course> courses;

    public Term() {
        courses = new ArrayList<>();
    }

    public static Term fromCursor(Cursor cursor) {
        Term term = new Term();
        term.id = cursor.getLong(cursor.getColumnIndex(WGUContract.Terms._ID));
        term.title = cursor.getString(cursor.getColumnIndex(WGUContract.Terms.TITLE));
        term.startDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Terms.START_DATE)) * 1000);
        term.endDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Terms.END_DATE)) * 1000);
        return term;
    }
}
