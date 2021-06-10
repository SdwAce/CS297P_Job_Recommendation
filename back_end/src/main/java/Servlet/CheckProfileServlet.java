package Servlet;

import Database.DBOperations;
import Model.Profile;
import Model.ShowRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckProfileServlet",urlPatterns = {"/check"})
public class CheckProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ShowRequestBody show_request = mapper.readValue(request.getReader(),ShowRequestBody.class);
        DBOperations db = new DBOperations();
        Profile profile = db.checkProfileExist(show_request.getUser_id());
        response.setContentType("application/json");
        db.close();
        mapper.writeValue(response.getWriter(), profile);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
