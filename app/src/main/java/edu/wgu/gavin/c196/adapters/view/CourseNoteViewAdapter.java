package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.CourseNote;

public class CourseNoteViewAdapter implements ViewAdapter {

    private CourseNote mCourseNote;
    private View mView;
    private View.OnClickListener mOnEditClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    private CourseNoteViewAdapter(CourseNote note, View view,
                                  View.OnClickListener onEditClickListener,
                                  View.OnClickListener onDeleteClickListener) {
        mCourseNote = note;
        mView = view;
        mOnEditClickListener = onEditClickListener;
        mOnDeleteClickListener = onDeleteClickListener;
    }

    public static CourseNoteViewAdapter from(CourseNote note, View view) {
        return from(note, view, null, null);
    }

    public static CourseNoteViewAdapter from(CourseNote note, View view,
                                               View.OnClickListener onEditClickListener,
                                               View.OnClickListener onDeleteClickListener) {
        return new CourseNoteViewAdapter(note, view, onEditClickListener, onDeleteClickListener);
    }

    public void render() {
        TextView noteView = mView.findViewById(R.id.note);
        noteView.setText(mCourseNote.note);

        if (mOnEditClickListener != null) {
            Button editBtn = mView.findViewById(R.id.note_edit);
            editBtn.setVisibility(View.VISIBLE);
            editBtn.setOnClickListener(mOnEditClickListener);
        }

        if (mOnDeleteClickListener != null) {
            Button deleteBtn = mView.findViewById(R.id.note_delete);
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(mOnDeleteClickListener);
        }
    }

}
