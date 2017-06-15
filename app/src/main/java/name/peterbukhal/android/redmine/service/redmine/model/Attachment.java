package name.peterbukhal.android.redmine.service.redmine.model;

import com.google.gson.annotations.SerializedName;

public final class Attachment {

    private int id;

    @SerializedName("filename")
    private String name;

    @SerializedName("filesize")
    private long size;

    @SerializedName("content_type")
    private String contentType;

    private String description;

    @SerializedName("content_url")
    private String contentUrl;

    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    private Author author;

    @SerializedName("created_on")
    private String createdOn;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Author getAuthor() {
        return author;
    }

    public String getCreatedOn() {
        return createdOn;
    }

}
