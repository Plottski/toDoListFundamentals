package plottski.todolistfundamentals.Entities;

import jakarta.persistence.*;

@Entity
public class Collaboration {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String ownerUser;

    @Column(nullable = false)
    private int ownerID;

    @Column(nullable = false)
    private String collaboratorUserName;

    @Column(nullable = false)
    private int collaboratorUserID;

    @Column(nullable = false)
    private int listID;

    @Column(nullable = false)
    private String listName;

    public Collaboration() {
    }

    public Collaboration(String ownerUser, int ownerID, String collaboratorUserName, int collaboratorUserID, int listID, String listName) {
        this.ownerUser = ownerUser;
        this.ownerID = ownerID;
        this.collaboratorUserName = collaboratorUserName;
        this.collaboratorUserID = collaboratorUserID;
        this.listID = listID;
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getCollaboratorUserName() {
        return collaboratorUserName;
    }

    public void setCollaboratorUserName(String collaboratorUserName) {
        this.collaboratorUserName = collaboratorUserName;
    }

    public int getCollaboratorUserID() {
        return collaboratorUserID;
    }

    public void setCollaboratorUserID(int collaboratorUserID) {
        this.collaboratorUserID = collaboratorUserID;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

}
