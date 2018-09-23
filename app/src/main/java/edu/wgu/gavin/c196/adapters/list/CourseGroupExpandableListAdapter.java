package edu.wgu.gavin.c196.adapters.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import edu.wgu.gavin.c196.R;
import edu.wgu.gavin.c196.adapters.view.CourseViewAdapter;
import edu.wgu.gavin.c196.models.Course;
import edu.wgu.gavin.c196.models.Term;

public class CourseGroupExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Term> mTermList;

    public CourseGroupExpandableListAdapter(Context context, List<Term> termList) {
        mContext = context;
        mTermList = termList;
    }

    @Override
    public int getGroupCount() {
        return mTermList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mTermList.get(i).courses.size();
    }

    @Override
    public Term getGroup(int i) {
        return mTermList.get(i);
    }

    @Override
    public Course getChild(int i, int j) {
        return mTermList.get(i).courses.get(j);
    }

    @Override
    public long getGroupId(int i) {
        return mTermList.get(i).id;
    }

    @Override
    public long getChildId(int i, int j) {
        return mTermList.get(i).courses.get(j).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean expanded, View convertView, ViewGroup parent) {
        Term term = getGroup(i);
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_coursegroup, parent, false);

        TextView titleView = convertView.findViewById(R.id.term_title);
        titleView.setText(term.title);

        return convertView;
    }

    @Override
    public View getChildView(int i, int j, boolean lastChild, View convertView, ViewGroup parent) {
        Course course = getChild(i, j);
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_course, parent, false);

        CourseViewAdapter.from(mContext, course, convertView, null).render();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
