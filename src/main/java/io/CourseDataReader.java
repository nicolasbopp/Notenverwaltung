package io;

import logic.Course;

import java.io.File;

public interface CourseDataReader {
    public Course readData(File courseDataFile) ;
}
