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
        this.name = name;
        this.id = id;
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

    public Document(String name, String date, String description, String fileName, String createdOn, String lastModified, int totalSize, boolean isMarkdown, String objectId, String documentTypeId) {
        this.id = UUID.randomUUID().toString();
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

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean isMarkdown() {
        return isMarkdown;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public String getId() {
        return id;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
