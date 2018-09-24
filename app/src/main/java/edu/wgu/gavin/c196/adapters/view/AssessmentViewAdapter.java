package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.Assessment;

public class AssessmentViewAdapter implements ViewAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Assessment mAssessment;
    private View mView;
    private View.OnClickListener mOnEditClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    private AssessmentViewAdapter(Assessment assessment, View view,
                                  View.OnClickListener onEditClickListener,
                                  View.OnClickListener onDeleteClickListener) {
        mAssessment = assessment;
        mView = view;
        mOnEditClickListener = onEditClickListener;
        mOnDeleteClickListener = onDeleteClickListener;
    }

    public static AssessmentViewAdapter from(Assessment assessment, View view) {
        return from(assessment, view, null, null);
    }

    public static AssessmentViewAdapter from(Assessment assessment, View view,
                                             View.OnClickListener onEditClickListener,
                                             View.OnClickListener onDeleteClickListener) {
        return new AssessmentViewAdapter(assessment, view, onEditClickListener, onDeleteClickListener);
    }

    public void render() {
        TextView titleView = mView.findViewById(R.id.assessment_title);
        TextView typeView = mView.findViewById(R.id.assessment_type);
        TextView dueDateView = mView.findViewById(R.id.assessment_dueDate);

        titleView.setText(mAssessment.title);
        typeView.setText(mAssessment.type.toString());
        dueDateView.setText(mDateFormat.format(mAssessment.dueDate));

        if (mOnEditClickListener != null) {
            Button editBtn = mView.findViewById(R.id.assessment_edit);
            editBtn.setVisibility(View.VISIBLE);
            editBtn.setOnClickListener(mOnEditClickListener);
        }

        if (mOnDeleteClickListener != null) {
            Button deleteBtn = mView.findViewById(R.id.assessment_delete);
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(mOnDeleteClickListener);
        }
    }

}
