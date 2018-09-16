package edu.wgu.gavin.c196.models;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    public int id;
    public String title;
    public Date startDate;
    public Date anticipatedEndDate;
    public String status;
    public String notes;
    public ArrayList<CourseMentor> mentors;
    public ArrayList<Assessment> assessments;
}
