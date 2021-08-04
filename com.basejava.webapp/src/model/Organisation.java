package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organisation extends AbstractSection<Experience, List<Experience>> {

    private final List<Experience> data;

    public Organisation() {
        this.data = new ArrayList<>();
    }

    @Override
    protected void printData() {
        System.out.println();
        for (Experience element : data) {
            System.out.println(element);
            System.out.println("________________________________________________________");
        }
    }

    @Override
    protected void saveToData(Experience dataNew) {
        data.add(dataNew);
    }

    @Override
    protected void update(Experience dataPrev, Experience dataNew) {
        data.set(data.indexOf(dataPrev), dataNew);
    }

    @Override
    protected void delete(Experience dataToDelete) {
        data.remove(dataToDelete);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organisation)) return false;
        Organisation that = (Organisation) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
