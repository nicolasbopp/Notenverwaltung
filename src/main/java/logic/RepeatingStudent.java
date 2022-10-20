package logic;

public class RepeatingStudent extends Student{

    double examGrade;

    public RepeatingStudent(String name, String major, double examGrade) {
        super(name, major);
        this.examGrade = examGrade;
    }

    public double getFinalGrade(){
        return (examGrade);
    }

    public double getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(double examGrade) {
        this.examGrade = examGrade;
    }
}
