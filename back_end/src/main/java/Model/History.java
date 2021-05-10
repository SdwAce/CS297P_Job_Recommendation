package Model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="history")
public class History implements Serializable {
    @EmbeddedId
    private HistoryKey key;

    public HistoryKey getKey() {
        return key;
    }

    public void setKey(HistoryKey key) {
        this.key = key;
    }
}
