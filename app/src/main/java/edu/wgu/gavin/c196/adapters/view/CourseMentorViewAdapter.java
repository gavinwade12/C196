package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.TextView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.CourseMentor;

public class CourseMentorViewAdapter implements ViewAdapter {

    private CourseMentor mCourseMentor;
    private View mView;

    private CourseMentorViewAdapter(CourseMentor mentor, View view) {
        mCourseMentor = mentor;
        mView = view;
    }

    public static CourseMentorViewAdapter from(CourseMentor mentor, View view) {
        return new CourseMentorViewAdapter(mentor, view);
    }

    public void render() {
        TextView nameView = mView.findViewById(R.id.mentor_name);
        TextView phoneView = mView.findViewById(R.id.mentor_phone);
        TextView emailView = mView.findViewById(R.id.mentor_email);

        nameView.setText(mCourseMentor.name);
        phoneView.setText(mCourseMentor.phoneNumber);
        emailView.setText(mCourseMentor.emailAddress);
    }

}
