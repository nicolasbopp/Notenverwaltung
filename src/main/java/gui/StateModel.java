package gui;

import javafx.scene.layout.CornerRadii;
import logic.Course;

public class StateModel {
    private Course course;
    private double preGradeFactor;
    private boolean sortByGrade;

    public void setCourse(Course course) {
        this.course = course;
    }
}
