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

public class Controller {
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
        //Init Variables
        drawPanel.getChildren().clear();

        //----- Customizable -----
        double gap = 10;                     // Gap between the data bars
        double borderGap = 25;              // Gap between the diagram and the border of the pane
        // --------------------------------------------------------------------------------------
        double xPosition = borderGap;
        double yPosition;
        double heightBar;
        double widthBar = ((drawPanel.getWidth()-(2*borderGap))/course.getStudents().size())-gap; // Calculates the max Width of bars

        // Horizontal divider lines
        for(double i = 0; i <= 6; i+=0.5){
            Line gradeLine = new Line(borderGap-20,(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*i/6))-borderGap,(drawPanel.getWidth()-(borderGap)+3),(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*i/6))-borderGap);
            gradeLine.setStroke(Color.DARKGRAY);
            if(i%1==0){
                gradeLine.setStrokeWidth(0.9);  // Every whole number is a THIIIIIIICK line
                Text nameText = new Text(String.valueOf(i));
                nameText.getTransforms().add(new Translate(borderGap-20, (drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*i/6))-borderGap - 10));
                drawPanel.getChildren().add(nameText);
            }else{
                gradeLine.setStrokeWidth(0.3);  // Every decimal number in slime line
            }
            drawPanel.getChildren().add(gradeLine);
        }

        // Creates the databars
        for(int i = 0; i < course.getStudents().size(); i++){
            heightBar = (drawPanel.getHeight()-2*borderGap)*(course.getStudents().get(i).getFinalGrade(preGradeFactor)/6);
            yPosition = drawPanel.getHeight()-heightBar-borderGap;
            Rectangle gradeBar = new Rectangle( xPosition,  yPosition, widthBar, heightBar);
            if(course.getStudents().get(i).getFinalGrade(preGradeFactor)>= 4){
                gradeBar.setFill(Color.rgb(36, 137, 220));
            }else{
                gradeBar.setFill(Color.INDIANRED);
            }
            drawPanel.getChildren().add(gradeBar);
            Text nameText = new Text(course.getStudents().get(i).getName());
            nameText.getTransforms().add(new Translate(xPosition + widthBar/2, drawPanel.getHeight() - borderGap- 20));
            nameText.getTransforms().add(new Rotate(-90));
            drawPanel.getChildren().add(nameText);
            xPosition += widthBar + gap;
        }
        // Creates a THIIICKer line on grade 4
        Line successLine = new Line(borderGap-20,(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*4/6))-borderGap,(drawPanel.getWidth()-(borderGap)+3),(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*4/6))-borderGap);
        successLine.setStrokeWidth(1.8);
        successLine.setStroke(Color.BLACK);
        drawPanel.getChildren().add(successLine);

        // Creates a line for the average grade
        Line averageLine = new Line(borderGap,(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*course.totalGradeCourse(preGradeFactor)/6))-borderGap,(drawPanel.getWidth()-(borderGap))+3,(drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*course.totalGradeCourse(preGradeFactor)/6))-borderGap);
        averageLine.setStrokeWidth(2.4);
        averageLine.setStroke(Color.DARKBLUE);
        drawPanel.getChildren().add(averageLine);
        Text averageText = new Text(String.valueOf("Durchschnitssnote"));
        averageText.getTransforms().add(new Translate(borderGap, drawPanel.getHeight()-((drawPanel.getHeight()-2*borderGap)*course.totalGradeCourse(preGradeFactor)/6)-30));
        averageText.setFill(Color.DARKBLUE);
        drawPanel.getChildren().add(averageText);
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