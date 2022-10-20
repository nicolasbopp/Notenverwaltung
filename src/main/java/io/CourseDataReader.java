package io;
import logic.Course;
import logic.RegularStudent;
import logic.RepeatingStudent;
import logic.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CourseDataReader {
    public static Course readStudentData(File courseDataFile ){
        HashMap majorMap = MajorMapReader.readMajorData(new File("src/main/resources/data/major.txt"));
        ArrayList<Student> studentList = new ArrayList<Student>();
        String name = "";
        String major = "";
        String idCourse = "";
        String nameCourse = "";
        double examGrade = 0;
        int counter;
        try{
            Scanner scanner = new Scanner(courseDataFile);
            idCourse = scanner.nextLine();
            nameCourse = scanner.nextLine();
            while (scanner.hasNextLine()){
                ArrayList<Double> gradeList = new ArrayList<Double>();
                String text = scanner.nextLine();
                String[] tokens = text.split(",");
                counter = 0;
                boolean repeatStudent = false;
                for(String t: tokens){
                    switch (counter){
                        case 0:
                            name = t;
                            break;
                        case 1:
                            major = majorMap.get(t.trim()).toString();
                            break;
                        case 2:
                            if(t.equals("r")){
                                repeatStudent = true;
                            }
                            break;
                        default:
                            if(repeatStudent = true){
                                examGrade = Double.parseDouble(t);
                            }else {
                                if(counter == 3){
                                    examGrade = Double.parseDouble(t);
                                }else {
                                    gradeList.add(Double.parseDouble(t));
                                }
                            }
                    }
                    counter =  counter + 1;
                }
                if(repeatStudent){
                    RepeatingStudent student = new RepeatingStudent(name, major, examGrade);
                    studentList.add(student);
                }else {
                    RegularStudent student = new RegularStudent(name, major, gradeList, examGrade);
                    studentList.add(student);
                }
            }
            scanner.close();
        }catch (IOException e){
            System.out.println("Unable to read student data, sorry.");
        }
        Course course = new Course(idCourse, nameCourse, studentList);
        return course;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
