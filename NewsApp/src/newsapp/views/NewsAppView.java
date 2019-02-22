package newsapp.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import newsapp.controllers.NewsAppController;

/**
 * Viewer class to build the javafx UI for newsapp. Provides a search box, and a
 * main data area, and the view saved items area. Also provides are the save
 * button and delete button.
 *
 * @author zaidal-momen
 */
public class NewsAppView {

    public HBox searchBox;
    public VBox mainVBox;
    public VBox viewSavedVBox;
    public Button saveSelected;
    public Button deleteButton;
    public BorderPane bigPane;

    /**
     * builds the javafx scene to be used in the Main application. The scene
     * draws all the components of the newapp.
     *
     * @return a Scene.
     */
    public Scene buildScene() {

        //The entire pane.
        this.bigPane = new BorderPane();
        this.bigPane.setPadding(new Insets(20, 20, 20, 20));

        //builds the top menu which consists of mainMenu buttons and a search box. 
        VBox topMenu = new VBox(10);
        HBox mainMenu = new HBox(400);
        Button mainButton = new Button("Main");
        Button viewSavedButton = new Button("View Saved");

        //create hbox for mainMenu
        mainMenu.getChildren().addAll(mainButton, viewSavedButton);

        //call drawSearchBox to build the search box and assign event handlers. 
        drawSearchBox();
        topMenu.getChildren().addAll(mainMenu, this.searchBox);

        //add the top menu to bigPane.
        this.bigPane.setTop(topMenu);

        //Add main center box which will display search results 
        this.mainVBox = new VBox(10);

        //build view saved page.      
        this.viewSavedVBox = new VBox(10);

        //handling events: flip between main area and saved items. 
        mainButton.setOnAction(e -> {
            this.mainVBox.setVisible(true);
            this.viewSavedVBox.setVisible(false);
            this.bigPane.setCenter(this.mainVBox);

        });

        viewSavedButton.setOnAction(e -> {
            this.mainVBox.setVisible(false);
            this.viewSavedVBox.setVisible(true);

            //controller handles data retrieval.
            NewsAppController.addResultsToView(this.viewSavedVBox, NewsAppController.getSavedItems(), "Saved Items:");
            drawDeleteButton();
            this.bigPane.setCenter(this.viewSavedVBox);
        });

        this.bigPane.setCenter(this.mainVBox);

        Scene scene = new Scene(this.bigPane, 1300, 750);
        return scene;
    }
    
    /**
     * method to draw the search box and assign event handling to the textbox. 
     * creates the instance variable searchBox. 
     * event handling is assigned to both search text and Go Button.
     * NewsAppController assigns the event on action. Event is search.
     */
    public void drawSearchBox() {
        this.searchBox = new HBox();
        Label searchLabel = new Label("Search:");
        searchLabel.setPadding(new Insets(5, 10, 10, 10));

        TextField searchText = new TextField("");
        Button goButton = new Button("Go!");
        this.searchBox.getChildren().addAll(searchLabel, searchText, goButton);
        this.searchBox.setSpacing(10);

        //on action event, data results come from controller. add save button.
        EventHandler searchHandler = (EventHandler<ActionEvent>) e -> {
            NewsAppController.addResultsToView(this.mainVBox, NewsAppController.search(searchText.getText()), "Search Results:");
            drawSaveSelectedButton();
        };
        searchText.setOnAction(searchHandler);
        goButton.setOnAction(searchHandler);
    }
    
    /**
     * method to draw delete button.
     * creates the instance variable deleteButton.
     * NewsAppController assigns deleting items event to the delete button.
     */
    public void drawDeleteButton() {
        this.deleteButton = new Button("Delete Selected");
        this.viewSavedVBox.getChildren().add(this.deleteButton);
        this.deleteButton.setOnAction(e -> {
            NewsAppController.editSelected(this.viewSavedVBox, "delete");
            NewsAppController.addResultsToView(this.viewSavedVBox, NewsAppController.getSavedItems(), "Saved Items:");
            this.viewSavedVBox.getChildren().addAll(this.deleteButton, new Label("Deleted!"));
        });

    }
    
    /**
     * method to draw save selected items button. 
     * creates the save selected button,
     * NewsAppController assigns saving data event to button.
     */
    public void drawSaveSelectedButton() {
        this.saveSelected = new Button("Save Selected");
        this.mainVBox.getChildren().add(this.saveSelected);
        this.saveSelected.setOnAction(e -> {
            NewsAppController.editSelected(this.mainVBox, "save");
            if (!(this.mainVBox.getChildren().get(this.mainVBox.getChildren().size() - 1) instanceof Label)) {
                this.mainVBox.getChildren().add(new Label("Saved!"));
            }
        });
    }

          //OLD CODE that used a table, but did not have checkboxes. 
    
//        public static TableView drawTable() {
//        TableView tbData = new TableView<>();
//        TableColumn<ResultModel, String> title = new TableColumn<>("title");
//        TableColumn<ResultModel, String> summary = new TableColumn<>("summary");
//        TableColumn<ResultModel, String> body = new TableColumn<>("body");
//        
//        title.setCellValueFactory(new PropertyValueFactory<>("title"));
//        title.setMinWidth(200);
//        summary.setCellValueFactory(new PropertyValueFactory<>("summary"));
//        summary.setMinWidth(300);
//        body.setCellValueFactory(new PropertyValueFactory<>("body"));
//        body.setMinWidth(500);
//        tbData.getColumns().addAll(title, summary, body);
//        
//        tbData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        return tbData;
//    }
    
}
