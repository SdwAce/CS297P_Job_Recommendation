package External;


import Model.ExtractRequestBody;
import Model.ExtractResponse;
import Model.Extraction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.*;

public class MonkeyLearnClient {
    private static final String EXTRACT_URL = "https://api.monkeylearn.com/v3/extractors/ex_YCya9nrn/extract/";
    private static final String AUTH_TOKEN = "2714d4bcba18a712cbe83d1c0298d22615f42318";
    public static void main(String[] args) throws MonkeyLearnException, IOException {
        extract("Microsoft’s OS Group is building the Windows 10 operating system and core user experiences to power the devices which will delight a billion users in this mobile first, cloud first world we love. Are you interested in the development platform for future releases of Windows and help shape the next generation devices? Are you someone with a strong passion for quality and engineering excellence? Do you want to be part of an agile and energetic team? Do you want to shape the UI platform for the largest operating system user base? Do you want to help ship the fluent features and interaction models that span from voice to 3D and mixed reality?\n" +
                "\n" +
                "Our team is part of the Windows Developer Ecosystem Platform group and is responsible for building the core UI framework (XAML) which is a widely used framework and actively used by Shell, Office, App teams & external developers. We work on a broad spectrum of instrumentation and data analysis to bring insights back on our device experiences, driving, and validating that we build products that are competitive, performant, long-term viable, and integrates well with other experiences.\n" +
                "\n" +
                "We are currently looking for a talented, self-motivated software engineer who is passionate about ensuring that we build highly performant and reliable experiences that delight our customers. You will work with other software engineers, program managers, data scientists, and partners to design and implement product code instrumentation, mine and analyze data, build data models and dashboards to answer business questions. You will need to monitor and improve telemetry to assess the health of the product in both pre-release as well as released software, using this data to drive improvements into the quality of the product. You will collaborate with the data science team to apply machine learning and artificial intelligence for building predictive analytic models. You must be self-motivated, a fast learner, have good communication skills, enjoy working in a team of talented engineers, and be passionate about quality and engineering excellence.\n" +
                "\n" +
                "Basic Qualifications:\n" +
                "3+ months active development experience in C/C++ and/or C# and .Net\n" +
                "Preferred Qualifications:\n" +
                "Strong problem solving, debugging, and troubleshooting skills BS or higher in Computer Science/Engineering or equivalent degree\n" +
                "Attention to detail and a passion for quality.\n" +
                "Strong communication and collaboration skills.\n" +
                "Experience with data science and data engineering technologies.\n" +
                "Passion for data driven approach to software engineering.\n" +
                "Excellent problem solving, troubleshooting skills.\n" +
                "Microsoft is an equal opportunity employer. All qualified applicants will receive consideration for employment without regard to age, ancestry, color, family or medical care leave, gender identity or expression, genetic information, marital status, medical condition, national origin, physical or mental disability, political affiliation, protected veteran status, race, religion, sex (including pregnancy), sexual orientation, or any other characteristic protected by applicable laws, regulations and ordinances. If you need assistance and/or a reasonable accommodation due to a disability during the application or the recruiting process, please send a request to askstaff@microsoft.com.");

    }
    public static List<List<String>> extract(String description) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<String> data = new ArrayList<>();
        //description.replaceAll("\n", "").replaceAll("\r", "");
        data.add(description);
        HttpPost request = new HttpPost(EXTRACT_URL);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Authorization", "Token " + AUTH_TOKEN);
        ExtractRequestBody requestBody = new ExtractRequestBody(data,10);
        String json;
        try{
            //write a json format string
            json = mapper.writeValueAsString(requestBody);
        }catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
        try {
            //create httpentity from the json string
            request.setEntity(new StringEntity(json));
        } catch (UnsupportedEncodingException e) {
            return Collections.emptyList();
        }

        ResponseHandler<List<List<String>>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            //extract the http entity
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();
            }
            //deserialize
            ExtractResponse[] results = mapper.readValue(entity.getContent(), ExtractResponse[].class);
            List<List<String>> keywordList = new ArrayList<>();
            for (ExtractResponse result : results) {
                List<String> keywords = new ArrayList<>();
                for (Extraction extraction : result.extractions) {
                    keywords.add(extraction.parsedValue);
                }
                keywordList.add(keywords);
            }


            return keywordList;
        };

        try {
            return httpclient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();

    }

}
