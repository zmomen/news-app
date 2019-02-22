package newsapp.models;

import javafx.scene.control.CheckBox;

/**
 * ResultModel class which resembles the database structure. The model represents a news
 * result item with a title, summary and an author. The checkbox will be used to denote 
 * that item has been selected for modification (saved/delete).
 * @author zaidal-momen
 */
public class ResultModel {

    private String title;
    private String summary;    
    private String author;
    private CheckBox select;

    /**
     * constructor with title and summary
     * @param title news result title.
     * @param summary news result summary.
     */
    public ResultModel(String title, String summary) {
        this.title = title;
        this.summary = summary;
        this.select = new CheckBox();
    }
    
    /**
     * constructor with title, summary and author.
     * @param title news result title.
     * @param summary news result summary. 
     * @param author news result author.
     */
    public ResultModel(String title, String summary, String author) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.select = new CheckBox();
    }
    /**
     * gets the title
     * @return title text
     */
    public String getTitle() {
        return this.title;
    }
    /**
     * gets the summary.
     * @return summary text
     */
    public String getSummary() {
        return this.summary;
    }
    
    /**
     * gets the author.
     * @return author text.
     */
    public String getAuthor() {
        return this.author;
    }
    
    /**
     * sets the title.
     * @param newTitle new title.
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }
    
    /**
     * sets the summary
     * @param newSummary new summary 
     */
    public void setSummary(String newSummary) {
        this.summary = newSummary;
    }
    
    /**
     * sets the author
     * @param newAuthor new author 
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }
    
    /**
     * gets the value of checkBox
     * @return value of checkbox
     */
    public CheckBox getSelect() {
        return select;
    }
    
    /**
     * sets value of checkBox
     * @param newSelect value of checkbox.
     */
    public void setSelect(CheckBox newSelect) {
        this.select = newSelect;
    }
    
    /**
     * returns a string representation of ResultModel.
     * @return title, summary and author.
     */
    @Override
    public String toString() {
        return "title: " + this.title + ", summary: " + this.summary + ", author: " + this.author; 
        
    }
}
