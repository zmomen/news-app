package newsapp;

import javafx.application.Application;
import javafx.stage.Stage;
import newsapp.views.NewsAppView;

/**
 * main application class that launches the News Search App.
 * Starts the NewsAppView viewer class and provides the main scene.
 * @author zaidal-momen
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Stage window = primaryStage;
        window.setTitle("News Search App");

        NewsAppView newsView = new NewsAppView();
        window.setScene(newsView.buildScene());
        window.show();
    }
    /**
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
