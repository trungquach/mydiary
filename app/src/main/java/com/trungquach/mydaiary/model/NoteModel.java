package com.trungquach.mydaiary.model;

/**
 * Created by trungqn on 12/20/2017.
 */

public class NoteModel {
    public static final int ACTIVE_STATUS = 0;
    public static final int INACTIVE_STATUS=1;

    private int id;
    private String title;
    private String content;
    private String dateTime;
    private String image;
    private int status = ACTIVE_STATUS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
