package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.TextView;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.CourseNote;

public class CourseNoteViewAdapter implements ViewAdapter {

    private CourseNote mCourseNote;
    private View mView;

    private CourseNoteViewAdapter(CourseNote note, View view) {
        mCourseNote = note;
        mView = view;
    }

    public static CourseNoteViewAdapter from(CourseNote note, View view) {
        return new CourseNoteViewAdapter(note, view);
    }

    public void render() {
        TextView noteView = mView.findViewById(R.id.note);
        noteView.setText(mCourseNote.note);
    }

}
