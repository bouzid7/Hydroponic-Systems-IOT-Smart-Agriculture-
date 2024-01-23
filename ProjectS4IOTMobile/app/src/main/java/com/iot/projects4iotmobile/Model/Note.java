package com.iot.projects4iotmobile.Model;
import java.io.Serializable;

public class Note  implements Serializable {
    private String objectNote ;
    private String contentNote ;
    private String dateNote;
    private String isFavorite;

    public Note(String objet, String content, String date,String isFavorite) {
        this.objectNote = objet;
        this.contentNote = content;
        this.dateNote=date;
        this.isFavorite = isFavorite;
    }

    public String getObjectNote(){
        return this.objectNote;
    }

    public String getContentNote(){
        return this.contentNote;
    }

    public void setObjectNote(String objet) {
        this.objectNote = objet;
    }

    public void setContentNote(String content) {
        this.contentNote = content;
    }

    public String getDateNote(){return this.dateNote;}

    public void setDateNote(String date) {this.dateNote = date;}

    public String getIsFavorite(){
        return this.isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

}
