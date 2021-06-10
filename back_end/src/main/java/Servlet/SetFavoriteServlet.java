package Servlet;

import Database.DBOperations;
import Database.DBOperations_Hibernate;
import Model.ResultResponse;
import Model.SaveRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SetFavoriteServlet", urlPatterns = {"/save"})
public class SetFavoriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        SaveRequestBody saveRequest = mapper.readValue(request.getReader(),SaveRequestBody.class);
        DBOperations_Hibernate db = new DBOperations_Hibernate();
        db.setFavorite(saveRequest.getUser_id(), saveRequest.getJob_id());
        ResultResponse resultResponse = new ResultResponse("OK");
        db.close();
        mapper.writeValue(response.getWriter(), resultResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String origin = req.getHeader("Origin");
        resp.setHeader("Access-Control-Allow-Origin", origin);
        resp.setHeader("Access-Control-Allow-Methods", "DELETE, HEAD, GET, OPTIONS, POST, PUT");

        String headers = req.getHeader("Access-Control-Request-Headers");
        if (headers != null)
            resp.setHeader("Access-Control-Allow-Headers", headers);

        ObjectMapper mapper = new ObjectMapper();
        SaveRequestBody saveRequest = mapper.readValue(req.getReader(),SaveRequestBody.class);
        DBOperations db = new DBOperations();
        db.unsetFavorite(saveRequest.getUser_id(), saveRequest.getJob_id());
        ResultResponse resultResponse = new ResultResponse("OK");
        db.close();
        mapper.writeValue(resp.getWriter(), resultResponse);
    }
}
