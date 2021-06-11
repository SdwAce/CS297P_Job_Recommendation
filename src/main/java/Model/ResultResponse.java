package Model;

import javax.xml.transform.Result;

public class ResultResponse {
    private String result;
    public ResultResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
