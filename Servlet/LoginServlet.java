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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet",urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //deserializer
        User user = mapper.readValue(request.getReader(), User.class);
        DBOperations db = new DBOperations();
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        if (db.login(user.getUser_id(), user.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("user_id",user.getUser_id());
            json.put("Result","OK!");
        }else{
            json.put("Result","Login failed");
            response.setStatus(401);
        }
        db.close();
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //ObjectMapper mapper = new ObjectMapper();
        //HttpSession session = request.getSession(false);
    }
}
