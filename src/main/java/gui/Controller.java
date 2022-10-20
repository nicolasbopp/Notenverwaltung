package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import io.CourseDataReader;
import logic.Course;
import logic.Student;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    private Label amountStudentLabel;
    @FXML
    private ListView<String> studentListView;
    @FXML
    private Label averageGradeLabel;
    @FXML
    private CheckBox worstGradeCheckbox;
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private BarChart<String, Number> diagramGrades;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label courseIdLabel;
    public File fileName;

    // File selection
    public void btnChooseFile(ActionEvent event) throws IOException {
        File fileSave;
        fileSave = fileName;
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        try {
            FileChooser fc = new FileChooser();
            this.fileName = fc.showOpenDialog(thisStage);
            if(fileName.exists()){
                drawWindow(fileName);
            }
        }catch (Exception e){
            System.out.println("Keine Datei ausgewählt.");
            fileName = fileSave;
        }
    }

    public void checkBoxWorstGrade() {
        drawWindow(fileName);
    }

    public void drawWindow(File file){
        worstGradeCheckbox.setDisable(false);
        studentListView.setDisable(false);

        Course course = CourseDataReader.readStudentData(file);                             // Read Data
        if(worstGradeCheckbox.isSelected()){
            //course.removeWorstGrade();                                                      // Remove worst grade
        }
    loadLabel(course);                                                                      // Load label
    drawAverageDiagram(course.getStudents());                                               // Diagram
    studentListView.getItems().clear();                                                     // Clear list
    loadAverageList(course.getStudents());                                                  // Build list
    }

    // ------------------------------- Label
    public void loadLabel(Course course){
        amountStudentLabel.setText("Anzahl Studierende: " + course.getStudents().size());   // Student amount
        courseIdLabel.setText(course.getId());                                              // Course ID
        courseNameLabel.setText(course.getName());                                          // Course Name
        //averageGradeLabel.setText("Gesamtschnitt: " + course.totalGradeCourse());           // Total Grade Average
    }

    // ------------------------------- List
    public void loadAverageList(ArrayList<Student> studentList){
        String[] studentArray = new String[studentList.size()];
        for(int i = 0; i < studentList.size(); i++){
            studentArray[i] = (studentList.get(i).getName() + " (" + studentList.get(i).getMajor() + "): " + studentList.get(i).getFinalGrade());
        }
        studentListView.getItems().addAll(studentArray);
    }
    // ------------------------------- Diagram
    public void drawAverageDiagram(ArrayList<Student> studentList){
        styleDiagram();
        XYChart.Series<String,Number> series1 = new XYChart.Series();
        for(int i = 0; i < studentList.size(); i++){
            XYChart.Data data = new XYChart.Data<>(studentList.get(i).getName(), studentList.get(i).getFinalGrade());
            series1.getData().add(data);
        }
        diagramGrades.getData().add(series1);
    }

    public void styleDiagram(){
        diagramGrades.getData().clear();
        yAxis.setLowerBound(1);
        yAxis.setUpperBound(6);
        yAxis.setTickUnit(0.5);
        yAxis.setAutoRanging(false);
    }
}