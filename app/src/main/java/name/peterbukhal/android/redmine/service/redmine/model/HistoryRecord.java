package name.peterbukhal.android.redmine.service.redmine.model;

import java.util.Collections;
import java.util.List;

public final class HistoryRecord {

    private int id;
    private Author user;
    private String notes;
    private String created_on;
    private List<HistoryRecordDetail> details;

    public int getId() {
        return id;
    }

    public Author getUser() {
        return user;
    }

    public String getNotes() {
        if (notes == null) {
            notes = "";
        }

        return notes.trim();
    }

    public String getCreated_on() {
        return created_on;
    }

    public List<HistoryRecordDetail> getDetails() {
        if (details == null) {
            details = Collections.emptyList();
        }

        return details;
    }

}
