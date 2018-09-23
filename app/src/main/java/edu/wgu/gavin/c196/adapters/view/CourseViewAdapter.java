package edu.wgu.gavin.c196.adapters.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.activities.CourseDetailActivity;
import edu.wgu.gavin.c196.activities.CoursesActivity;
import edu.wgu.gavin.c196.data.WGUContract;
import edu.wgu.gavin.c196.models.Course;

public class CourseViewAdapter implements ViewAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private Context mContext;
    private Course mCourse;
    private View mView;
    private View.OnClickListener mOnRemoveClickListener;

    private CourseViewAdapter(Context context, Course course, View view, View.OnClickListener onRemoveClick) {
        mContext = context;
        mCourse = course;
        mOnRemoveClickListener = onRemoveClick;
        mView = view;
    }

    public static CourseViewAdapter from(Context context, Course course, View view, View.OnClickListener onRemoveClick) {
        return new CourseViewAdapter(context, course, view, onRemoveClick);
    }

    public void render() {
        TextView idView = mView.findViewById(R.id.course_id);
        TextView titleView = mView.findViewById(R.id.course_title);
        TextView statusView = mView.findViewById(R.id.course_status);
        TextView startDateView = mView.findViewById(R.id.course_startDate);
        TextView endDateView = mView.findViewById(R.id.course_endDate);

        idView.setText(String.valueOf(mCourse.id));
        titleView.setText(mCourse.title);
        statusView.setText(mCourse.status.toString());
        startDateView.setText(mDateFormat.format(mCourse.startDate));
        endDateView.setText(mDateFormat.format(mCourse.anticipatedEndDate));

        if (mOnRemoveClickListener != null) {
            Button removeBtn = mView.findViewById(R.id.remove);
            removeBtn.setVisibility(View.VISIBLE);
            removeBtn.setOnClickListener(mOnRemoveClickListener);
        } else {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(mContext instanceof AppCompatActivity))
                        return;

                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra(WGUContract.Courses._ID, mCourse.id);
                    ((AppCompatActivity) mContext).startActivityForResult(intent, CoursesActivity.VIEW_COURSE_REQUEST);
                }
            });
        }
    }

}
