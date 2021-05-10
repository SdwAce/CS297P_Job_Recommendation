package Model;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class HistoryKey implements Serializable {
    private String userId;
    private String jobId;
    public HistoryKey(String userId, String jobId){
        this.userId = userId;
        this.jobId = jobId;
    }

    public HistoryKey() {

    }
}
