package Servlet;

import Database.DBOperations;
import Model.LoginResponse;
import Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "LoginServlet",urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //deserializer
        User user = mapper.readValue(request.getReader(), User.class);
        DBOperations db = new DBOperations();
        LoginResponse loginResponse;
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName);
            System.out.println(request.getParameterValues(paramName));
        }

        String[] result = db.login(user.getUser_id(), user.getPassword());

        if (!result[0].equals("Failed")){
            HttpSession session = request.getSession();
            session.setAttribute("user_id",user.getUser_id());
            loginResponse = new LoginResponse(user.getUser_id(), "OK", result[1]);
            response.setStatus(200);
        }else{
            loginResponse = new LoginResponse(null,"Login Failed, user_id or password does not exist",null);
            response.setStatus(401);
        }

        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(),loginResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        LoginResponse loginResponse;
        if (session != null){
            DBOperations db = new DBOperations();
            loginResponse = new LoginResponse(session.getAttribute("user_id").toString(), "OK",db.getFirstName(session.getAttribute("user_id").toString()));
       }else{
            loginResponse = new LoginResponse(null, "Invalid Session", null);
            response.setStatus(403);
        }
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), loginResponse);
    }
}
