package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Utility class with static methods to connect/get/update/delete news results from the derby database.
 * @author zaidal-momen
 */
public class DBUtils {

    private Connection conn = null;
    
    /**
     * constructor to initiate a db connection. connect to derby. 
     */
    public DBUtils() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            this.conn = DriverManager.getConnection("jdbc:derby://localhost:1527/news");
//            System.out.println("connected to DB!");
        } catch (Exception ex) {
            System.out.println("failed to connect");
            System.out.println(ex);
        }
    }
    
    /**
     * closes the connection.
     */
    public void closeConnection() {
        try {
            System.out.println("closing...");
            this.conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * method to get all results from the database. sends a SQL query. 
     * @return JSONArray with results. 
     */
    public JSONArray getNewsResults() {
        String sqlStmt = "SELECT n.title, n.summary, n.author FROM NEWS_RESULT n";
        PreparedStatement pstm;
        JSONArray ja = new JSONArray();
        try {
            pstm = this.conn.prepareStatement(sqlStmt);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                jo.put("title", rs.getString("title"));
                jo.put("summary", rs.getString("summary"));
                jo.put("author", rs.getString("author"));
                ja.add(jo);
            }
            this.conn.close();
//            System.out.println("reults in json" + ja.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return ja;
    }
    
    /**
     * method to update results. action to save or delete is part of the JSONObject.
     * Runs SQL statement to insert or delete results, based on action.
     * @param jo results to be updated to the database.
     */
    public void updateResults(JSONObject jo) {
        String action = (String) jo.get("action");
        JSONArray res = (JSONArray) jo.get("articles");
        String sqlStmt = "";
        PreparedStatement pstm;
        try {
            for (int i = 0; i < res.size(); i++) {
                if (action.equals("save")) {
                    sqlStmt = "INSERT INTO NEWS_RESULT (title, summary, author) VALUES (?, ?, ?)";
                    Map article = (Map) res.get(i);
                    pstm = this.conn.prepareStatement(sqlStmt);
                    pstm.setString(1, (String) article.get("title"));
                    pstm.setString(2, (String) article.get("summary"));
                    pstm.setString(3, (String) article.get("author"));
                } else {
                    sqlStmt = "DELETE FROM NEWS_RESULT WHERE TITLE=?";
                    pstm = this.conn.prepareStatement(sqlStmt);
                    Map article = (Map) res.get(i);
                    pstm.setString(1, (String) article.get("title"));
                }
                pstm.execute();
            }

            this.conn.close();
            System.out.println(res.size() + " result(s) updated!");

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
