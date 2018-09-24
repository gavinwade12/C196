package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.CourseMentor;

public class CourseMentorViewAdapter implements ViewAdapter {

    private CourseMentor mCourseMentor;
    private View mView;
    private View.OnClickListener mOnEditClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    private CourseMentorViewAdapter(CourseMentor mentor, View view,
                                    View.OnClickListener onEditClickListener,
                                    View.OnClickListener onDeleteClickListener) {
        mCourseMentor = mentor;
        mView = view;
        mOnEditClickListener = onEditClickListener;
        mOnDeleteClickListener = onDeleteClickListener;
    }

    public static CourseMentorViewAdapter from(CourseMentor mentor, View view) {
        return from(mentor, view, null, null);
    }

    public static CourseMentorViewAdapter from(CourseMentor mentor, View view,
                                               View.OnClickListener onEditClickListener,
                                               View.OnClickListener onDeleteClickListener) {
        return new CourseMentorViewAdapter(mentor, view, onEditClickListener, onDeleteClickListener);
    }

    public void render() {
        TextView nameView = mView.findViewById(R.id.mentor_name);
        TextView phoneView = mView.findViewById(R.id.mentor_phone);
        TextView emailView = mView.findViewById(R.id.mentor_email);

        nameView.setText(mCourseMentor.name);
        phoneView.setText(mCourseMentor.phoneNumber);
        emailView.setText(mCourseMentor.emailAddress);

        if (mOnEditClickListener != null) {
            Button editBtn = mView.findViewById(R.id.mentor_edit);
            editBtn.setVisibility(View.VISIBLE);
            editBtn.setOnClickListener(mOnEditClickListener);
        }

        if (mOnDeleteClickListener != null) {
            Button deleteBtn = mView.findViewById(R.id.mentor_delete);
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(mOnDeleteClickListener);
        }
    }

}
