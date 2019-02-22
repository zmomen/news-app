package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.DBUtils;

/**
 * Servlet class with basic GET and POST methods to retrieve news results from the database.
 * @author zaidal-momen
 */
public class ResultServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {    
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //creates a connection object.
            DBUtils db = new DBUtils();
            //get db results.
            String result = db.getNewsResults().toString();
            out.write(result);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //create connection object
            DBUtils db = new DBUtils();
            //read data from POST request
            String reader = request.getReader().readLine();
            Object obj = new JSONParser().parse(reader);
            JSONObject jo = (JSONObject) obj;
            System.out.println("json arra" + jo.toString());
            //updates the database.
            db.updateResults(jo);
            db.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
        
  
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
