package Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name="keywords")
public class Keywords {
    @EmbeddedId
    private KeyWordsKey key;

    public KeyWordsKey getKey() {
        return key;
    }

    public void setKey(KeyWordsKey key) {
        this.key = key;
    }
}