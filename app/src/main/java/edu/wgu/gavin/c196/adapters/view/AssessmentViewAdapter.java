package edu.wgu.gavin.c196.adapters.view;

import android.view.View;
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

    private AssessmentViewAdapter(Assessment assessment, View view) {
        mAssessment = assessment;
        mView = view;
    }

    public static AssessmentViewAdapter from(Assessment assessment, View view) {
        return new AssessmentViewAdapter(assessment, view);
    }

    public void render() {
        TextView titleView = mView.findViewById(R.id.assessment_title);
        TextView typeView = mView.findViewById(R.id.assessment_type);
        TextView dueDateView = mView.findViewById(R.id.assessment_dueDate);

        titleView.setText(mAssessment.title);
        typeView.setText(mAssessment.type.toString());
        dueDateView.setText(mDateFormat.format(mAssessment.dueDate));
    }

}
