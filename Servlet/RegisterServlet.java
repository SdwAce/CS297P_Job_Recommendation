package Servlet;

import Database.DBOperations;
import Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet",urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //deserializer, jackson
        User user = mapper.readValue(request.getReader(), User.class);
        DBOperations db = new DBOperations();
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        if (db.register(user.getUser_id(), user.getPassword(), user.getFirstName(),user.getLastName())){
            json.put("Result","OK");
        }else{
            json.put("Result","User Already exists");
        }
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
