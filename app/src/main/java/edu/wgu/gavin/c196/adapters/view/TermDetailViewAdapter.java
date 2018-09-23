package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.cursor.CoursesCursorAdapter;
import edu.wgu.gavin.c196.models.Term;

public class TermDetailViewAdapter implements ViewAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Term mTerm;
    private CoursesCursorAdapter mCoursesAdapter;
    private View mView;

    private TermDetailViewAdapter(Term term, CoursesCursorAdapter coursesAdapter, View view) {
        mTerm = term;
        mCoursesAdapter = coursesAdapter;
        mView = view;
    }

    public static TermDetailViewAdapter from(Term term, CoursesCursorAdapter coursesAdapter, View view) {
        return new TermDetailViewAdapter(term, coursesAdapter, view);
    }

    public void render() {
        TextView titleView = mView.findViewById(R.id.term_title);
        TextView startDateView = mView.findViewById(R.id.term_startDate);
        TextView endDateView = mView.findViewById(R.id.term_endDate);
        ListView coursesList = mView.findViewById(R.id.term_courses);

        titleView.setText(mTerm.title);
        startDateView.setText(mDateFormat.format(mTerm.startDate));
        endDateView.setText(mDateFormat.format(mTerm.endDate));
        coursesList.setAdapter(mCoursesAdapter);
    }

}
