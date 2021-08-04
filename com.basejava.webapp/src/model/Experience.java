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

    @Override
    public String toString() {
        if (this.subtitle == null) {
            return "Organisation: " + title + '\n' +
                    "Description: " + description + '\n' +
                    "DateFrom: " + dateFrom + '\n' +
                    "DateTo: " + dateTo + '\n';
        }
        return "Organisation: " + title + '\n' +
                "Activity: " + subtitle + '\n' +
                "Description: " + description + '\n' +
                "DateFrom: " + dateFrom + '\n' +
                "DateTo: " + dateTo + '\n';
    }
}
