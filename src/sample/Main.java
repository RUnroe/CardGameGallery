package sample;

import blackjack.controllers.BlackjackEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../blackjack/views/blackjack-home-scene.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("../blackjack/views/blackjack-home-scene.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

        new BlackjackEngine().setup();

//        BlackjackTemplateModel bjTM = new BlackjackTemplateModel(); // used for card test
//        bjTM.testCardImageView(); // sets card image view
//        BlackjackHomeScene bjHS = fxmlLoader.getController(); // loads active controller from fxml
//        bjHS.anchorPaneRoot.getChildren().add(bjTM.queenOfHearts.imageView); // adds card image view to anchor pane
//
//        List<ImageView> handImageViews = new ArrayList<>();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
