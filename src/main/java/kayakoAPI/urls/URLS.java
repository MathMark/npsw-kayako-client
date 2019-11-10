package kayakoAPI.urls;

public enum URLS {
    CASES("https://npsw.kayako.com/api/v1/cases.json"),
    USER("https://npsw.kayako.com/api/v1/users/"),
    CONVERSATION("https://npsw.kayako.com/agent/conversations/");

    private final String url;

    URLS(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
