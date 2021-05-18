package CS297.JobRecommendation;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@WebServlet(name = "SearchServlet",urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get from url : http://localhost:8080/Job_Recommendation/search?title=harry&location=ch
//        String title = request.getParameter("title");
//        String location = request.getParameter("location").toString();
//        String skill_Set = request.getParameter("skill").toString();
        //SearchJob search = new SearchJob();
        //result = search.getresult();
        //search.search();
        response.setContentType("application/json");
        JSONObject json = new JSONObject();

        //extract all the parameters from the url
        Enumeration<String> parameterNames = request.getParameterNames();
//        int count = 0;
        ArrayList<String> key_words = new ArrayList<String>();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.equals("title")) {
//                key_words.add("title", request.getParameterValues(paramName));
                key_words.add(request.getParameterValues(paramName)[0]);
            } else if (paramName.equals("location")) {
//                json.put("location", request.getParameterValues(paramName));
                key_words.add(request.getParameterValues(paramName)[0]);
            } else if (paramName.equals("skill")) {
//                json.put("skill", request.getParameterValues(paramName));
                key_words.add(request.getParameterValues(paramName)[0]);
            }
//            json.put(String.valueOf(count),entry.toString());
        }

        //send the parameters to the database and get the result back in the form of a list of JobEntry
        String [] param = new String [key_words.size()];
        param = key_words.toArray(param);
        PostgresWithJDBCConnection query = new PostgresWithJDBCConnection();
        List<JobEntry> jobEntryList = query.get_result(param);
        int i = 0;
        for (JobEntry jobEntry : jobEntryList) {
            JSONObject entry = new JSONObject();
            entry.put("job_id",jobEntry.getJob_id());
            entry.put("title",jobEntry.getTitle());
            entry.put("company",jobEntry.getCompany());
            entry.put("location",jobEntry.getLocation());
            entry.put("skill",request.getParameterValues("skill"));
            System.out.print("Title:: " + jobEntry.getTitle());
            System.out.print(" | Company:: " + jobEntry.getCompany());
            System.out.print(" | Location:: " + jobEntry.getLocation());
//                System.out.print(" | Description:: " + jobEntry.getDescription());
            System.out.println();
            json.put(String.valueOf(i),entry);
            i++;
        }

        response.getWriter().print(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
