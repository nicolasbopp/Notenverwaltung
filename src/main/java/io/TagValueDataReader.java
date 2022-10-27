package io;

import logic.Course;
import logic.RegularStudent;
import logic.RepeatingStudent;
import logic.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TagValueDataReader implements CourseDataReader{
    @Override
    public Course readData(File courseDataFile) {
        HashMap majorMap = MajorMapReader.readMajorData(new File("src/main/resources/data/major.txt"));
        // Course Variables
        String idCourse = "";
        String nameCourse = "";
        ArrayList<Student> studentList = new ArrayList<Student>();

        //Student Variables
        String name = "";
        String major = "";
        double examGrade = 0;
        boolean repeatStudent = false;

        //Read Variables
        String readLine = "";
        String key = "";
        String value = "";
        boolean newStudent = false;

        try {
            // Kursdaten
            Scanner scanner = new Scanner(courseDataFile);
            readLine = scanner.nextLine();
            idCourse = readLine.substring((readLine.indexOf(":")+2));       // Course ID
            readLine = scanner.nextLine();
            nameCourse = readLine.substring((readLine.indexOf(":")+2));     // Course Name

            // Studenten
            ArrayList<Double> gradeList = new ArrayList<Double>();
            while (scanner.hasNextLine()){
                readLine = scanner.nextLine();
                key = readLine.substring(0,readLine.indexOf(":"));
                value = readLine.substring((readLine.indexOf(":")+2));
                switch (key){
                    case "name":
                        name = value;
                        break;
                    case "major":
                        major = majorMap.get(value.trim()).toString();
                        break;
                    case "is_repeating":
                        if(value.trim().equals("true")){
                            repeatStudent = true;
                        }else{
                            repeatStudent = false;
                        }
                        break;
                    case "exam-grade":
                        examGrade = Double.parseDouble(value);
                        if(repeatStudent){
                            newStudent = true;                      // Letztes Feld von Repeating Student eingelesen > Neuer Student wird erstellt
                        }
                        break;
                    case "pre-grade":
                        String[] tokens = value.split(",");
                        for(String grade : tokens){
                            gradeList.add(Double.parseDouble(grade));
                        }
                        newStudent = true;                          // Letztes Feld von Regular Student eingelesen > Neuer Student wird erstellt
                        break;
                }
                if (newStudent){
                    if(repeatStudent){
                        RepeatingStudent student = new RepeatingStudent(name, major, examGrade);
                        studentList.add(student);
                    }else {
                        RegularStudent student = new RegularStudent(name, major, gradeList, examGrade);
                        student.removeWorstGrade();
                        studentList.add(student);
                    }
                    newStudent = false;
                }
            }
            scanner.close();
        }catch (Exception e){
            System.out.println("Unable to read student data, sorry.");
        }
        Course course = new Course(idCourse, nameCourse, studentList);
        return course;
    }
}
