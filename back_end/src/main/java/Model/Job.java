package Model;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.mapping.Set;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="job")

public class Job implements Serializable {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String company;
    private String title;
    private String location;
    private String description;
    //private boolean favorite;
    @Transient
    private Set keywords;

    //public boolean getFavorite() {
        //return favorite;
    //}

   // public void setFavorite(boolean favorite) {
        //this.favorite = favorite;

    public Job(){

    }
   public Job(String title,String company,String location){
       //this.id = id;
       this.title = title;
       this.company = company;
       this.location = location;
   }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Set getKeywords() {
        return keywords;
    }

    public void setKeywords(Set keywords) {
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
