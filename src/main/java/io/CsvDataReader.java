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

public class CsvDataReader implements  CourseDataReader{
    @Override
    public  Course readData(File courseDataFile ){
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
            idCourse = scanner.nextLine();                                  // Course ID
            nameCourse = scanner.nextLine();                                // Course Name
            while (scanner.hasNextLine()){
                ArrayList<Double> gradeList = new ArrayList<Double>();
                String text = scanner.nextLine();
                String[] tokens = text.split(",");
                counter = 0;
                boolean repeatStudent = false;
                for(String t: tokens){
                    switch (counter){
                        case 0:                                             // Student name
                            name = t;
                            break;
                        case 1:
                            major = majorMap.get(t.trim()).toString();      // Student major
                            break;
                        case 2:
                            if(t.trim().equals("r")){
                                repeatStudent = true;                       // Check if repeating Student
                            }
                            break;
                        default:                                            // ---- Read Grades ----
                            if(repeatStudent){                                  // -- Repeating student only read examgrade
                                examGrade = Double.parseDouble(t);
                            }else {
                                if(counter == 3){                               // -- Regular student
                                    examGrade = Double.parseDouble(t);              // Read examgrade
                                }else {
                                    gradeList.add(Double.parseDouble(t));           // Read pregrade
                                }
                            }
                    }
                    counter += 1;
                }
                if(repeatStudent){
                    RepeatingStudent student = new RepeatingStudent(name, major, examGrade);
                    studentList.add(student);
                }else {
                    RegularStudent student = new RegularStudent(name, major, gradeList, examGrade);
                    student.removeWorstGrade();
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
}
