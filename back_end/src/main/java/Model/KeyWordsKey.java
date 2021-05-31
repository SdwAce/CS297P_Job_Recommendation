package Model;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class KeyWordsKey implements Serializable{
    private String job_id;
    private String keyword;
    public KeyWordsKey(String job_id, String keyword){
        this.job_id = job_id;
        this.keyword = keyword;
    }
    public KeyWordsKey(){

    }
}
