package com.basejava.webapp.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.time.YearMonth.of;

public class Organisation  implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Link homePage;
    private List<Position> positions = new ArrayList<>();

    public Organisation(String name, String url, Position... positions) {
        this(new Link(name, url), Arrays.asList(positions));
    }

    public Organisation(Link homePage, List<Position> positions) {
        this.homePage = homePage;
        this.positions = positions;
    }

    public Organisation(Link homePage, Position... positions) {
        this(homePage, Arrays.asList(positions));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return Objects.equals(homePage, that.homePage) &&
                Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, positions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.homePage.getUrl() == null) {
            sb.append("Organisation: ").append(this.homePage.getName()).append('\n');
        } else {
            sb.append(homePage).append('\n');
        }
        for (Position position : positions) {
            sb.append(position).append('\n');
        }
        return sb.toString();
    }

    public static class Position  implements Serializable{
        private final YearMonth startDate;
        private final YearMonth endDate;
        private final String title;
        private final String description;

        public Position(int startYear, int startMonth, String title, String description) {
            this(of(startYear, startMonth), YearMonth.now(), title, description);
        }

        public Position(int startYear, int startMonth, int endYear, int endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public Position(int startYear, int startMonth, int endYear, int endMonth, String title) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, null);
        }

        public Position(YearMonth startDate, YearMonth endDate, String title, String description) {
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(startDate, position.startDate) &&
                    Objects.equals(endDate, position.endDate) &&
                    Objects.equals(title, position.title) &&
                    Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, title, description);
        }

        @Override
        public String toString() {
            if (this.description == null) {
                return "Period: " + "(" + startDate + " - " + endDate + ')' + "\nTitle: " + title;
            }
            return "Period: " + "(" + startDate + " - " + endDate + ')' + "\nTitle: " + title + "\nDescription: " + description;
        }
    }
}