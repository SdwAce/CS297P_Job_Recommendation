package Model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryKey that = (HistoryKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, jobId);
    }
}
