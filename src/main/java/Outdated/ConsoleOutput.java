package Outdated;
import logic.Course;

public class ConsoleOutput {
    public static void outputAllGrades(Course course){
        System.out.println("Grades for: " + course.getName() + " (" + course.getId() + ")");
        System.out.println("--------------------------------------");
        for(int i = 0; i < course.getStudents().size(); i++){
            System.out.println(course.getStudents().get(i).getName() +  " (" + course.getStudents().get(i).getMajor() + "): " + course.getStudents().get(i).computeGradeAverage());
        }
        System.out.println("--------------------------------------");
    }
}
