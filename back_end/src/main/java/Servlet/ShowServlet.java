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

        jobs = db.showHistory(show_request.getUser_id());
        db.close();
        response.getWriter().print(mapper.writeValueAsString(jobs));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
