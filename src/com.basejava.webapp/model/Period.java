package com.basejava.webapp.model;

import java.time.YearMonth;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period)) return false;
        Period period = (Period) o;
        return Objects.equals(description, period.description) && Objects.equals(dateFrom, period.dateFrom) && Objects.equals(dateTo, period.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dateFrom, dateTo);
    }
}
