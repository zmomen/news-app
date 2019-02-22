package newsapp.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import newsapp.models.ResultModel;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 * Controller class with static methods that bind actions to viewer class events. 
 * The class connects to the news search API to retrieve data. 
 * The class connects the local servlet application to add/remove news results.
 * The class manipulates the viewer's main box and saved items box to add/remove results. 
 * @author zaidal-momen
 */
public class NewsAppController {
    
    //final variables used to connect to data sources. 
    private final static String dbLocation = "http://localhost:8080/DemoApp/results";
    private final static String apiKey = "16eabca179494fa391757fa32d70a9cd";
    private final static String apiSource = "https://newsapi.org/v2/everything";
    
    /**
     * Static method to edit the newsapp viewer and add results to display on the application.
     * @param vbox data will be added to this box provided by the viewer. 
     * @param ll List object containing ResultModel objects. Each ResultModel is a news result.
     * @param heading heading to be displayed on newsapp viewer. 
     */
    public static void addResultsToView(VBox vbox, List<ResultModel> ll, String heading) {
        //clear everything on box. 
        vbox.getChildren().clear();
        
        //add headers. 
        String style = "-fx-font-weight: bold; -fx-font-size: 16pt;";
        HBox header = new HBox(100);
        Label selectHeader = new Label("    ");
        Label titleHeader = new Label("Title");
        titleHeader.setStyle(style);
        titleHeader.setMinWidth(300);
        Label summaryHeader = new Label("Summary");
        summaryHeader.setStyle(style);
        header.getChildren().addAll(selectHeader, titleHeader, summaryHeader);
        vbox.getChildren().addAll(new Label(heading), header);

        //add data
        for (int i = 0; i < ll.size(); i++) {
            HBox newRow = new HBox(40);
            CheckBox select = ll.get(i).getSelect();
            Text title = new Text(ll.get(i).getTitle());
            title.setWrappingWidth(400);
            Text summary = new Text(ll.get(i).getSummary());
            summary.setWrappingWidth(700);
            Text author = new Text(ll.get(i).getAuthor());
            author.setVisible(false);
            newRow.getChildren().addAll(select, title, summary, author);
            vbox.getChildren().add(newRow);
            
            // add actions to title and summary. to create a popup window. 
            // the popup window shows more details per result, namely the author
            // partially working... causes the results to disappear. 
            // workaround: clicking on main button or view saved button.
            EventHandler detailHandler = (EventHandler<Event>) e -> {
                final Stage detailDialog = new Stage();
                VBox dialogVbox = new VBox(20);
                Label authorHeader = new Label("Author");
                authorHeader.setStyle(style);
                author.setVisible(true);
                dialogVbox.getChildren().addAll(titleHeader, title, authorHeader, author, summaryHeader, summary);
                Scene dialogScene = new Scene(dialogVbox, 800, 500);
                detailDialog.setScene(dialogScene);
                detailDialog.show();
            };
            title.setOnMouseClicked(detailHandler);
            summary.setOnMouseClicked(detailHandler);
        }
    }
    
    /**
     * static method to edit the newsapp box, and save new data or delete data. 
     * @param source the box to be edited. 
     * @param action the type of action: save or delete. 
     */
    public static void editSelected(VBox source, String action) {
        //Get the data from source UI. retrieve each HBox and find the ResultModel object.
        // adds the ResultModel to an ObservableArrayList to be passed down to the database.
        List<ResultModel> out = FXCollections.observableArrayList();
        for (int i = 0; i < source.getChildren().size(); i++) {
            if (source.getChildren().get(i) instanceof HBox) {
                HBox tempResult = (HBox) source.getChildren().get(i);
                if (tempResult.getChildren().get(0) instanceof CheckBox) {
                    CheckBox tempCheck = (CheckBox) tempResult.getChildren().get(0);
                    if (tempCheck.isSelected()) {
                        Text title = (Text) tempResult.getChildren().get(1);
                        Text summary = (Text) tempResult.getChildren().get(2);
                        Text author = (Text) tempResult.getChildren().get(3);
                        ResultModel res = new ResultModel(title.getText(), summary.getText(), author.getText());
                        out.add(res);
//                        System.out.println(res.toString());
                    }
                }
            }
        }
        //private static method to update the database. 
        updateDB(out, action);
    }
    
    /**
     * private static method to handle connection to the tomcat database and perform action.
     * The data will be converted to JSON and be sent as a POST request. 
     * @param ll List of data to be updated on the database.
     * @param action the type of action to be performed: save or delete. 
     */
    private static void updateDB(List<ResultModel> ll, String action) {
        System.out.println("updating DB...");
        JSONObject jo = new JSONObject();
        jo.put("action", action);
        JSONArray ja = new JSONArray();

        for (ResultModel rs : ll) {
            Map article = new JSONObject();
            article.put("title", rs.getTitle());
            article.put("summary", rs.getSummary());
            article.put("author", rs.getAuthor());
            ja.add(article);
        }
        jo.put("articles", ja);
        try {
            //connect to tomcat application and send POST request with JSON payload and action.
            URL u = new URL(dbLocation);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jo.toJSONString());
            wr.flush();
            wr.close();
            System.out.println("POST response code. " + conn.getResponseCode() + " " + conn.getResponseMessage());
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    /**
     * static method to get saved news items from the database display back to the UI. 
     * this method is used in the news app view  in saved items page. 
     * this method is called by the addResultsToView.
     * @return a List to be used for display on the viewer app.
     */
    public static List<ResultModel> getSavedItems() {
        //calls private static method. 
        return getFromDB();
    }
    
    /**
     * private static method to connect to the tomcat database and retrieve results.
     * converts database results from JSONObject to a list of ResultModel objects. 
     * @return a List to be used for display on the viewer app.
     */
    private static List<ResultModel> getFromDB() {
        List<ResultModel> out = FXCollections.observableArrayList();
        JSONArray ja = null;

        try {
            URL u = new URL(dbLocation);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            ja = (JSONArray) new JSONParser().parse(response.toString());
            for (int i = 0; i < ja.size(); i++) {
                Map article = (Map) ja.get(i);
                String title = (String) article.get("title");
                String summary = (String) article.get("summary");
                String author = (String) article.get("author");
                ResultModel rm = new ResultModel(title, summary, author);
                out.add(rm);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return out;
    }
    
    /**
     * static method to retrieve search results from the external API. 
     * @param text value to search for.
     * @return a list of ResultModel objects to be used for display on viewer.
     */
    public static List<ResultModel> search(String text) {
        //calls private method.
        return searchAPI(text);
    }
    
    /**
     * private static method to connect to external API and retrieve search results.
     * connects to news-api website, retrieves a JSON object and converts it to ResultModel.
     * @param text value to search for. 
     * @return a list of ResultModel objects to be used for display on viewer. 
     */
    private static List<ResultModel> searchAPI(String text) {
        List<ResultModel> out = FXCollections.observableArrayList();
        //news-api search
        try {
            String url = apiSource + "?q=" + text + "&apiKey=" + apiKey + "&pageSize=10";
            url = url.replace(" ", "%20");
            URLConnection con = new URL(url).openConnection();

            System.out.println("\nSending 'GET' request to URL : " + url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Object obj = new JSONParser().parse(response.toString());
            JSONObject jo = (JSONObject) obj;

            Long totalResults = (Long) jo.get("totalResults");
            System.out.println("found " + totalResults + " results");
            if (totalResults > 0) {
                JSONArray articles = (JSONArray) jo.get("articles");
                for (int i = 0; i < articles.size(); i++) {
                    Map article = (Map) articles.get(i);
                    String title = (String) article.getOrDefault("title", "no title");
                    String summary = (String) article.getOrDefault("description", "no desc");
                    String author = (String) article.getOrDefault("author", "no author");
                    ResultModel res = new ResultModel(title, summary, author);
                    out.add(res);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return out;
    }

}
