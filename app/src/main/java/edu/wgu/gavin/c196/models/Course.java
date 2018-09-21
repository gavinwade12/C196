package edu.wgu.gavin.c196.models;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    public int id;
    public String title;
    public Date startDate;
    public Date anticipatedEndDate;
    public Status status;
    public Integer termId;
    public ArrayList<CourseMentor> mentors;
    public ArrayList<Assessment> assessments;

    public enum Status {
        InProgress,
        Completed,
        Dropped,
        PlanToTake;

        @Override
        public String toString() {
            switch (this) {
                case InProgress:
                    return "In Progress";
                case Completed:
                    return "Completed";
                case Dropped:
                    return "Dropped";
                case PlanToTake:
                    return "Plan to Take";
            }
            return null;
        }

        public static Status fromString(String status) {
            switch (status) {
                case "In Progress":
                    return InProgress;
                case "Completed":
                    return Completed;
                case "Dropped":
                    return Dropped;
                case "Plan to Take":
                    return PlanToTake;
            }
            return null;
        }
    }
}
