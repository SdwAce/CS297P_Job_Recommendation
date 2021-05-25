package Servlet;

import Database.DBOperations;
import Model.Job;
import Model.ResultResponse;
import Model.ShowRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ShowServlet",urlPatterns = {"/show"})
public class ShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ShowRequestBody show_request = mapper.readValue(request.getReader(),ShowRequestBody.class);

        DBOperations db = new DBOperations();
        List<Job> jobs = null;
        try {
            jobs = db.showHistory(show_request.getUser_id());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        response.getWriter().print(mapper.writeValueAsString(jobs));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ObjectMapper mapper = new ObjectMapper();
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session Invalid"));
            return;
        }
        DBOperations db = new DBOperations();
        List<Job> jobs = null;
        try {
            jobs = db.showHistory(session.getAttribute("user_id").toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        response.getWriter().print(mapper.writeValueAsString(jobs));

    }
}
