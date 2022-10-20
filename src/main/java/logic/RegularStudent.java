package logic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RegularStudent extends Student{

    private ArrayList<Double> grades;
    private double examGrade;

    public RegularStudent(String name, String major, ArrayList<Double> grades, double examGrade) {
        super(name, major);
        this.grades = grades;
        this.examGrade = examGrade;
    }

    public double getFinalGrade(double preGradeFactor){
        final DecimalFormat df = new DecimalFormat("0.0");
        return Double.parseDouble(df.format(preGradeFactor * this.computeGradeAverage() + (1-preGradeFactor) * examGrade));
    }

    private void removeWorstGrade(){
        grades.remove(grades.indexOf(Collections.min(grades)));
        this.grades = grades;
    }

    public double computeGradeAverage(){
        removeWorstGrade();
        double sum = 0;
        for(int i = 0; i < grades.size(); i++){
            sum = sum + grades.get(i);
        }
        return sum/grades.size();
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Double> grades) {
        this.grades = grades;
    }

    public double getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(double examGrade) {
        this.examGrade = examGrade;
    }
}
