package gui;

import io.TagValueDataReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import io.CsvDataReader;
import logic.Course;
import logic.RegularStudent;
import logic.Student;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ControlPane {
    @FXML
    private Pane mainPane;
    @FXML
    private Label amountStudentLabel;
    @FXML
    private ListView<String> studentListView;
    @FXML
    private Label averageGradeLabel;
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private BarChart<String, Number> diagramGrades;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label courseIdLabel;
    @FXML
    private Slider mySlider;
    @FXML
    private AnchorPane drawPanel;
    @FXML
    private Label preGradeLabel;
    private Line averageLine;
    public File fileName;
    public double preGradeFactor = 0.3;

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
                initWindow(fileName);
            }
        }catch (Exception e){
            System.out.println("Keine Datei ausgewählt.");
            fileName = fileSave;
        }
    }
    public void initWindow(File file){
    studentListView.setDisable(false);
    mySlider.setDisable(false);
    preGradeLabel.setDisable(false);
    Course course = new Course();
    if(file.getName().endsWith(".txt")){
        TagValueDataReader t = new TagValueDataReader();
        course = t.readData(file);                                 // Read Data
    }else{
        CsvDataReader c = new CsvDataReader();
        course = c.readData(file);                                 // Read Data
    }
    drawWindow(course);

    Course finalCourse = course;
    mySlider.valueProperty().addListener(new ChangeListener<Number>() {
    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        preGradeFactor = ((double) mySlider.getValue()/100);
        drawWindow(finalCourse);
    }
    });
    mainPane.widthProperty().addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
            drawManuelDiagram(finalCourse);
        }
    });
    mainPane.heightProperty().addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
            drawManuelDiagram(finalCourse);
        }
    });
}
    public void drawWindow(Course course){
        loadLabel(course);                                                                      // Load label
        drawDiagram(course);                                                                    // Diagram
        studentListView.getItems().clear();                                                     // Clear list
        loadAverageList(course.getStudents());                                                  // Build list
    }

    // ------------------------------- Label
    public void loadLabel(Course course){
        amountStudentLabel.setText("Anzahl Studierende: " + course.getStudents().size());           // Student amount
        courseIdLabel.setText(course.getId());                                                      // Course ID
        courseNameLabel.setText(course.getName());                                                  // Course Name
        final DecimalFormat df = new DecimalFormat("0");
        preGradeLabel.setText("Vornoten Faktor: " + df.format(preGradeFactor*100) + "%");    // Pregrade Factor
        averageGradeLabel.setText("Gesamtschnitt: " + course.totalGradeCourse(preGradeFactor));     // Total Grade Average
    }

    // ------------------------------- List
    public void loadAverageList(ArrayList<Student> studentList){
        String[] studentArray = new String[studentList.size()];
        for(int i = 0; i < studentList.size(); i++){
            if(studentList.get(i) instanceof RegularStudent){
                studentArray[i] = (studentList.get(i).getName() + " (" + studentList.get(i).getMajor() + "): " + studentList.get(i).getFinalGrade(preGradeFactor));
            }else{
                studentArray[i] = (studentList.get(i).getName() + "* (" + studentList.get(i).getMajor() + "): " + studentList.get(i).getFinalGrade(preGradeFactor));
            }
        }
        studentListView.getItems().addAll(studentArray);
    }
    // ------------------------------- Diagram
    public void drawDiagram(Course course){
        drawManuelDiagram(course);
        drawAutomaticDiagram(course.getStudents());
    }

    // Creates the diagram for tab "New Version"
    public void drawManuelDiagram(Course course){
        drawPanel.getChildren().clear();
        GraphicsPane gp = new GraphicsPane();
        gp.setCourse(course);
        gp.setPreGradeFactor(preGradeFactor);
        drawPanel.getChildren().add(gp.drawGraphics(drawPanel.getWidth(), drawPanel.getHeight()));
    }

    // Creates the diagram for tab "Old Version"
    public void drawAutomaticDiagram(ArrayList<Student> studentList){
        diagramGrades.getData().clear();
        yAxis.setLowerBound(1);
        yAxis.setUpperBound(6);
        yAxis.setTickUnit(0.5);
        yAxis.setAutoRanging(false);
        XYChart.Series<String,Number> series1 = new XYChart.Series();
        for(int i = 0; i < studentList.size(); i++){
            XYChart.Data data = new XYChart.Data<>(studentList.get(i).getName(), studentList.get(i).getFinalGrade(preGradeFactor));
            series1.getData().add(data);
        }
        diagramGrades.getData().add(series1);
    }
}