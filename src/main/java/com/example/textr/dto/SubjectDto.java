package com.example.textr.dto;

public class SubjectDto {
    private long id;

    private long createdOn;

    private long validTill;

    private String role;

    public SubjectDto() {
    }

    public SubjectDto(long id, long createdOn, long validTill, String role) {
        this();
        this.id = id;
        this.createdOn = createdOn;
        this.validTill = validTill;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getValidTill() {
        return validTill;
    }

    public void setValidTill(long validTill) {
        this.validTill = validTill;
    }

}
