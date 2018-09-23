package edu.wgu.gavin.c196.adapters.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.models.Term;

public class TermViewAdapter implements ViewAdapter {

    private static DateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    private Term mTerm;
    private View mView;

    private TermViewAdapter(Term term, View view) {
        mTerm = term;
        mView = view;
    }

    public static TermViewAdapter from(Term term, View view) {
        return new TermViewAdapter(term, view);
    }

    public void render() {
        TextView titleView = mView.findViewById(R.id.term_title);
        TextView startDateView = mView.findViewById(R.id.term_startDate);
        TextView endDateView = mView.findViewById(R.id.term_endDate);

        titleView.setText(mTerm.title);
        startDateView.setText(mDateFormat.format(mTerm.startDate));
        endDateView.setText(mDateFormat.format(mTerm.endDate));
    }

}
