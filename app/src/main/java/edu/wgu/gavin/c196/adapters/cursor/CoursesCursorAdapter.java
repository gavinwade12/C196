package edu.wgu.gavin.c196.adapters.cursor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.view.CourseViewAdapter;
import edu.wgu.gavin.c196.models.Course;

public class CoursesCursorAdapter extends CursorAdapter {

    private View.OnClickListener mOnRemoveClickListener;

    public CoursesCursorAdapter(Context context, Cursor cursor, int flags, View.OnClickListener onRemoveClick) {
        super(context, cursor, flags);
        mOnRemoveClickListener = onRemoveClick;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.listitem_course, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Course course = Course.fromCursor(cursor);
        CourseViewAdapter.from(context, course, view, mOnRemoveClickListener).render();
    }
}
