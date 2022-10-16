package Outdated;

import io.CourseDataReader;
import logic.Course;
import java.io.File;

public class MainConsole {
    public static void main(String[] args) {
        Course course= CourseDataReader.readStudentData(new File("src/main/resources/data/data.txt"));
        course.removeWorstGrade();
        ConsoleOutput.outputAllGrades(course);
    }
}
