package com.basejava.webapp.model;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Experience {
    private Link nameAndLink;
    private String subtitle;
    private String description;
    private YearMonth dateFrom;
    private YearMonth dateTo;
    private List<Period> periodList = new ArrayList<>();


    public Experience(Link link, String subtitle, String description, YearMonth dateFrom, YearMonth dateTo) {
        Objects.requireNonNull(link, "Title must not be null");
        Objects.requireNonNull(dateFrom, "dateFrom must not be null");
        Objects.requireNonNull(dateTo, "dateTo must not be null");
        this.nameAndLink = link;
        this.subtitle = subtitle;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Experience(Link link, String description, YearMonth dateFrom, YearMonth dateTo) {
        this.nameAndLink = link;
        this.subtitle = null;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Experience(Link link) {
        this.nameAndLink = link;
    }

    public void savePeriod(Period period) {
        this.periodList.add(period);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.periodList.isEmpty()) {
            sb.append(nameAndLink).append('\n');
            for (Period element : periodList) {
                sb.append(element).append('\n');
            }
        } else if (this.subtitle != null) {
            sb.append("Activity: ").append(subtitle).append('\n');
            sb.append("Description: ").append(description).append('\n');
            sb.append("DateFrom: ").append(dateFrom).append('\n');
            sb.append("DateTo: ").append(dateTo).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Experience)) return false;
        Experience that = (Experience) o;
        return Objects.equals(nameAndLink, that.nameAndLink) && Objects.equals(subtitle, that.subtitle) && Objects.equals(description, that.description) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo) && Objects.equals(periodList, that.periodList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameAndLink, subtitle, description, dateFrom, dateTo, periodList);
    }
}
