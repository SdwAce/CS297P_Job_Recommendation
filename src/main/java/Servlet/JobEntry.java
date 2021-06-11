package Servlet;

public class JobEntry {
    public String job_id;
    public String title;
    public String company;

    public String estSalary;
    public String location;
    public String description;

    //-----------------------SETTERS-----------------------

    public void setJob_id(String job_id) { this.job_id = job_id;}
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEstimatedSalary(String estSalary) {
        this.estSalary = estSalary;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //-----------------------GETTERS-----------------------
    public String getTitle() {
        return this.title;
    }

    public String getCompany() {
        return this.company;
    }

    public String getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.description;
    }
    public String getJob_id() {
        return job_id;
    }
}
