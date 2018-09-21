package edu.wgu.gavin.c196.models;

public class Assessment {
    public int id;
    public String notes;
    public Type type;
    public int courseId;

    public enum Type {
        Objective,
        Performance;

        @Override
        public String toString() {
            if (this == Objective)
                return "Objective";
            else if (this == Performance)
                return "Performance";
            return "";
        }

        public static Type fromString(String type) {
            switch (type) {
                case "Objective":
                    return Objective;
                case "Performance":
                    return Performance;
                default:
                    return null;
            }
        }
    }
}
