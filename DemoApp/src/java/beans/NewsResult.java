package beans;

/**
 *
 * @author zaidal-momen
 */
public class NewsResult {
    
    private String title;
    private String summary;
    private String body;
    
    public NewsResult() {
        this.title = this.summary = this.body = "";
    }
    
    public NewsResult(String aTitle, String aSummary, String aBody) {
        this.title = aTitle;
        this.summary = aSummary;
        this.body = aBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    
}
