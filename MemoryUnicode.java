package JavaFX_13_UniCode;

//I'd tried to incorporate images but failed miserably so i went ahead and used unicode instead.
//I didn't realize how much effort it would take to incorporate images, fade transition doesn't
//work with images but I had to use timeline but I didn't have enough time to learn timeline and code it!
//I hope this is sufficient!

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryUnicode  extends Application {
    private boolean playerTurn = false;
    private Squares selected = null;
    private int clickCount = 2;
    private static int NUMBER_PAIRS = 18;
    private static int NUMBER_IN_A_ROW = 9;



    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(1320,600);
        char uniCode = '\u2689';

        ArrayList<Squares> squares = new ArrayList<>();
        for (int i = 0; i < NUMBER_PAIRS; i++){
            squares.add( new Squares(String.valueOf(uniCode)));
            squares.add( new Squares(String.valueOf(uniCode)));
            uniCode++;
        }

        Collections.shuffle(squares);

        for (int i = 0; i < squares.size(); i++){
            Squares square = squares.get(i);
            square.setTranslateX(145 * (i % NUMBER_IN_A_ROW));
            square.setTranslateY(145 * (i / NUMBER_IN_A_ROW));
            root.getChildren().add(square);
        }
        return root;
    }

    //we are going to stack on top of each other
    private class Squares extends StackPane {

        private Text text = new Text();

        //if the value is the same we keep them open
        public Squares(String value){
            Rectangle border = new Rectangle(165,165);
            border.opacityProperty().set(0.5);
            border.setStroke(Color.BLUEVIOLET);

            //this will set the borders
            border.setFill(Color.LIGHTBLUE);
            text.setText(value);
            text.setFont(Font.font(100));
            text.setFill(Color.rgb(100, 100, 225, 1));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border,text);

            setOnMouseClicked(this::handleMouseClick);

            close();
        }


        public void handleMouseClick(MouseEvent event){
            if(isOpen() || clickCount == 0) //if clicked on again, nothing should happen since it's open
                return;

            clickCount--;

            if(selected == null){ //there's nothing selected and now open the square
                selected = this; //points to this object, no longer null
                open(() -> {}); //creates a new runnable and does nothing

            }
            else{
                open(() -> {
                    //if it doesn't have the same value
                    if(!hasSameValue(selected)) {
                        selected.close();
                        this.close();
                    }
                    selected = null;

                    //restore the click count
                    clickCount = 2;
                });

            }
        }

        public void playerTurn(Runnable action){

        }

        public boolean isOpen(){

            return text.getOpacity() == 1;
        }

        //returns true if it returns same value
        public boolean hasSameValue(Squares other){
            return text.getText().equals((other.text.getText()));
        }

        //calling the open method which is an action
        public void open(Runnable actionOpen){
            FadeTransition ft = new FadeTransition(Duration.seconds(1), text);
            ft.setToValue(1);
            //executed after the square is turned over
            ft.setOnFinished(e -> actionOpen.run());
            ft.play();

        }

        public void close(){
            FadeTransition ft = new FadeTransition(Duration.seconds(.25), text);
            ft.setToValue(0);
            ft.play();

        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        primaryStage.setTitle("Memory Match Game");
        Stage stage2 = new Stage();
        stage2.setTitle("Game Directions");
            Text text = new Text(100, 100, ""
                    + "Match a pair of Icons by clicking within the blue squares"
                    + "\nThere can only be one pair of matching Unicode Icons");
        root.getChildren().add(text);
        stage2.setScene(new Scene(root, 400, 100));
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
        stage2.show();
    }

    public static void main(String[] args) {

        launch(args);

    }
}
