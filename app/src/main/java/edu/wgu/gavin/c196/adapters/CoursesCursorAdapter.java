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
import edu.wgu.gavin.c196.activities.CourseDetailActivity;
import edu.wgu.gavin.c196.data.WGUContract;

public class CoursesCursorAdapter extends CursorAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public CoursesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.listitem_course, parent, false
        );
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final long id = cursor.getInt(cursor.getColumnIndex(WGUContract.Courses._ID));
        String title = cursor.getString(cursor.getColumnIndex(WGUContract.Courses.TITLE));
        String status = cursor.getString(cursor.getColumnIndex(WGUContract.Courses.STATUS));
        Date startDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Courses.START_DATE)) * 1000);
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndex(WGUContract.Courses.END_DATE)) * 1000);

        TextView titleView = view.findViewById(R.id.course_title);
        TextView statusView = view.findViewById(R.id.course_status);
        TextView startDateView = view.findViewById(R.id.course_startDate);
        TextView endDateView = view.findViewById(R.id.course_endDate);

        titleView.setText(title);
        statusView.setText(status);
        startDateView.setText(mDateFormat.format(startDate));
        endDateView.setText(mDateFormat.format(endDate));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra(WGUContract.Courses._ID, id);
                context.startActivity(intent);
            }
        });
    }
}
