package Servlet;

import Database.DBOperations;
import Model.Profile;
import Model.ShowRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProfileServlet",urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Profile profile = mapper.readValue(request.getReader(),Profile.class);
        DBOperations db = new DBOperations();
        db.saveProfile(profile.getUser_id(),profile.getEmail(),profile.getMajor(),profile.getLevel(),profile.isFind_Job());
        db.close();
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        json.put("Result","OK");
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
