package Model;

public class LoginResponse {
    public String user_id;
    public String status;
    public String first_name;
    public LoginResponse(String user_id, String status, String first_name){
        this.user_id = user_id;
        this.status = status;
        this.first_name = first_name;

    }


}
