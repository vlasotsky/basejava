package model;

import java.time.YearMonth;

public class Period {
    private String description;
    private YearMonth dateFrom;
    private YearMonth dateTo;

    public Period(String description, YearMonth dateFrom, YearMonth dateTo) {
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Period: " + dateFrom + " - " + dateTo +
                "\nDescription: " + description +
                "\n....................................................................";
    }
}
