package kayakoAPI.pojos;

public class Rating {

    private String status;

    private String text;

    public Rating(){

    }

    public Rating(String status, String text){
        this.status = status;
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
