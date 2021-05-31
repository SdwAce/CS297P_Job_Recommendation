package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractRequestBody {
    public List<String> data;
    public int max_keywords;
    public String transformation = "stem";
    //public int lowercase = 1;
    //public String representation = "transformation";
    //public static List<String> white_list = new ArrayList<String>(Arrays.asList("data","ai","software","web","ios","android","business","machine learn","natural language","system","ui"));
    //public List<String> white_list = new ArrayList<String>(Arrays.asList("software","web"));
    //public List<String> black_list = new ArrayList<String>(Arrays.asList("\n","check","culture of innovation","business united"));
    public ExtractRequestBody(List<String> data, int maxKeywords){
        this.data = data;
        this.max_keywords = maxKeywords;
    }
}
