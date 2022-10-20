package logic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Course {
    private String id;
    private String name;
    private ArrayList<Student> students;

    public Course(String id, String name, ArrayList<Student> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Student> getStudents() {
        return students;
    }

    /*
    public double totalGradeCourse(){
        double summe = 0;
        for(int i = 0; i < this.students.size(); i++){
            summe = summe + this.getStudents().get(i).computeGradeAverage();
        }
        final DecimalFormat df = new DecimalFormat("0.0");
        return Double.parseDouble(df.format(summe/this.students.size()));
    }

    public void removeWorstGrade(){
        for(int i =0; i < this.getStudents().size(); i++){
            students.get(i).getGrades().remove(students.get(i).getGrades().indexOf(Collections.min(getStudents().get(i).getGrades())));
        }
        this.students = students;
    }

     */
}
