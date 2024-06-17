package org.exaphex.realty.model;

import java.util.UUID;

public class Document {
    private String id;
    private String name;
    private String date;
    private String description;
    private String fileName;
    private String createdOn;
    private String lastModified;
    private int totalSize;
    private boolean isMarkdown;
    private String objectId;
    private String documentTypeId;

    public Document(String id, String name, String date, String description, String fileName, String createdOn, String lastModified, int totalSize, boolean isMarkdown, String objectId, String documentTypeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.fileName = fileName;
        this.createdOn = createdOn;
        this.lastModified = lastModified;
        this.totalSize = totalSize;
        this.isMarkdown = isMarkdown;
        this.objectId = objectId;
        this.documentTypeId = documentTypeId;
    }

    public Document(String name, String date, String description, String fileName, String createdOn, String lastModified, int totalSize, boolean isMarkdown, String documentTypeId, String objectId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.description = description;
        this.fileName = fileName;
        this.createdOn = createdOn;
        this.lastModified = lastModified;
        this.totalSize = totalSize;
        this.isMarkdown = isMarkdown;
        this.documentTypeId = documentTypeId;
        this.objectId = objectId;
    }
}
