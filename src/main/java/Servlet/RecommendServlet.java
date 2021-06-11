package Servlet;

import Database.DBOperations;
import Model.Job;
import Model.ShowRequestBody;
import Model.User;
import Recommendation.RecommendByTFIDFKeywords;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RecommendServlet",urlPatterns = {"/recommend"})
public class RecommendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ShowRequestBody body = mapper.readValue(request.getReader(), ShowRequestBody.class);
        String user_id = body.getUser_id();
        List<Job> jobEntryList = new ArrayList<>();
        List<Double> ranks = new ArrayList<>();
        Double mlWeight = 0.9;
        Double tsWeight = 1.0;
        RecommendByTFIDFKeywords.getRecommendations(user_id, jobEntryList, ranks, mlWeight, tsWeight);
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        response.getWriter().print(mapper.writeValueAsString(jobEntryList));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
