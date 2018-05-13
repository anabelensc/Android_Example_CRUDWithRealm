package com.example.belensarabia.examplerealm.models;

import android.app.Application;

import com.example.belensarabia.examplerealm.app.MyApplication;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import java.util.Date;

/**
 * Created by belensarabia on 22/4/18.
 */

public class Note extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date createdAt;

    public Note(){}

    public Note(String description){
        this.id = MyApplication.NoteID.incrementAndGet();
        this.description = description;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
