package gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import logic.Course;

public class GraphicsPane {
    Course course;
    double preGradeFactor;

    public AnchorPane drawGraphics(double width, double height){
        //Init Variables
        AnchorPane drawPanel = new AnchorPane();
        drawPanel.resize(width, height);

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

    return drawPanel;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public void setPreGradeFactor(double preGradeFactor) {
        this.preGradeFactor = preGradeFactor;
    }
}
