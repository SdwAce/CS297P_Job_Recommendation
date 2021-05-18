package Servlet;

import Database.DBOperations;
import Model.Job;
import Model.SearchNearbyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchNearbyServlet", urlPatterns = {"/searchnearby"})
public class SearchNearbyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        //System.out.println(jObj.getString("lon"));

        ObjectMapper mapper = new ObjectMapper();
        SearchNearbyRequest search_request = mapper.readValue(request.getReader(),SearchNearbyRequest.class);
        DBOperations db = new DBOperations();
        List<Job> jobs = db.searchNearJobs(Double.parseDouble(search_request.getLon()), Double.parseDouble(search_request.getLat()));
        //use writeValueAsString to transfer jobs to json string and respond to user
        response.getWriter().print(mapper.writeValueAsString(jobs));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
