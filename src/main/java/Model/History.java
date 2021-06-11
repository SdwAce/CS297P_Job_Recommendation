package Model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(key, history.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
