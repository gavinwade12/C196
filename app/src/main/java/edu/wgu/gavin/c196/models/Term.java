package edu.wgu.gavin.c196.models;

import java.util.ArrayList;
import java.util.Date;

public class Term {
    public int id;
    public String title;
    public Date startDate;
    public Date endDate;
    public ArrayList<Course> courses;
}
