package Model;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.mapping.Set;



import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="job_data")

public class Job implements Serializable {
    @Id
    private String job_id;

    private String company;
    private String job_title;
    private String location;
    private String job_description;

    private Double lat;

    private Double lon;
    //private boolean favorite;
    @Transient
    private Set keywords;
    @Transient
    private boolean favorite = false;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Set getKeywords() {
        return keywords;
    }

    public void setKeywords(Set keywords) {
        this.keywords = keywords;
    }
}
