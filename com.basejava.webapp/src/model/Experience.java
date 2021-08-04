package model;

import java.time.YearMonth;

public class Experience {
    private String title;
    private String subtitle;
    private String description;
    private YearMonth dateFrom;
    private YearMonth dateTo;

    public Experience(String title, String subtitle, String description, YearMonth dateFrom, YearMonth dateTo) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Experience(String title, String description, YearMonth dateFrom, YearMonth dateTo) {
        this.title = title;
        this.subtitle = null;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public YearMonth getDateFrom() {
        return dateFrom;
    }

    public YearMonth getDateTo() {
        return dateTo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateFrom(YearMonth dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(YearMonth dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        if (this.subtitle == null) {
            return "\ntitle: " + title + '\n' +
                    "description: " + description + '\n' +
                    "dateFrom: " + dateFrom + '\n' +
                    "dateTo: " + dateTo + '\n';
        }
        return "\ntitle: " + title + '\n' +
                "subtitle: " + subtitle + '\n' +
                "description: " + description + '\n' +
                "dateFrom: " + dateFrom + '\n' +
                "dateTo: " + dateTo + '\n';
    }
}
