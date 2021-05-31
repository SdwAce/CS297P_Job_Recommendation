package Servlet;

import Model.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet" ,urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ResultResponse resultResponse = new ResultResponse("Failed");

        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "DELETE, HEAD, GET, OPTIONS, POST, PUT");

        String headers = request.getHeader("Access-Control-Request-Headers");
        if (headers != null)
            response.setHeader("Access-Control-Allow-Headers", headers);

        if (session != null) {
            session.invalidate();
            resultResponse.setResult("OK");
        }

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), resultResponse);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
