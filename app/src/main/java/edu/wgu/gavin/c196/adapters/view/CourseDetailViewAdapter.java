package edu.wgu.gavin.c196.adapters.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.Assessment;
import edu.wgu.gavin.c196.models.Course;
import edu.wgu.gavin.c196.models.CourseMentor;
import edu.wgu.gavin.c196.models.CourseNote;

public class CourseDetailViewAdapter implements ViewAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Context mContext;
    private Course mCourse;
    private String mTermTitle;
    private View mView;

    private CourseDetailViewAdapter(Context context, Course course, String termTitle, View view) {
        mContext = context;
        mCourse = course;
        mTermTitle = termTitle;
        mView = view;
    }

    public static CourseDetailViewAdapter from(Context context, Course course, String termTitle, View view) {
        return new CourseDetailViewAdapter(context, course, termTitle, view);
    }

    public void render() {
        TextView titleView = mView.findViewById(R.id.course_title);
        TextView startDateView = mView.findViewById(R.id.course_startDate);
        TextView endDateView = mView.findViewById(R.id.course_endDate);
        TextView statusView = mView.findViewById(R.id.course_status);
        TextView termTitleView = mView.findViewById(R.id.course_term);

        titleView.setText(mCourse.title);
        startDateView.setText(mDateFormat.format(mCourse.startDate));
        endDateView.setText(mDateFormat.format(mCourse.anticipatedEndDate));
        statusView.setText(mCourse.status.toString());
        termTitleView.setText(mTermTitle);

        ViewGroup mentorListView = mView.findViewById(R.id.course_mentors);
        mentorListView.removeAllViews();
        for (CourseMentor mentor : mCourse.mentors) {
            View mentorView = LayoutInflater.from(mContext).inflate(R.layout.listitem_coursementor, null);
            CourseMentorViewAdapter.from(mentor, mentorView).render();
            mentorListView.addView(mentorView);
        }

        ViewGroup assessmentListView = mView.findViewById(R.id.course_assessments);
        assessmentListView.removeAllViews();
        for (Assessment assessment : mCourse.assessments) {
            View assessmentView = LayoutInflater.from(mContext).inflate(R.layout.listitem_assessment, null);
            AssessmentViewAdapter.from(assessment, assessmentView).render();
            assessmentListView.addView(assessmentView);
        }

        ViewGroup notesListView = mView.findViewById(R.id.course_notes);
        notesListView.removeAllViews();
        for (CourseNote note : mCourse.notes) {
            View notesView = LayoutInflater.from(mContext).inflate(R.layout.listitem_coursenote, null);
            CourseNoteViewAdapter.from(note, notesView).render();
            notesListView.addView(notesView);
        }
    }

}
