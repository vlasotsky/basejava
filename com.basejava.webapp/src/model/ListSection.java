package model;

import java.util.ArrayList;
import java.util.List;

public class ListSection extends AbstractSection<String> {
    private final List<String> data = new ArrayList<>();

    @Override
    protected void printData() {
        System.out.println();
        for (String element : data) {
            System.out.println("* " + element);
        }
    }

    @Override
    protected void saveToData(String dataNew) {
        data.add(dataNew);
    }

    @Override
    protected void update(String dataPrev, String dataNew) {
        this.data.set(this.data.indexOf(dataPrev), dataNew);
    }

    @Override
    protected void delete(String dataToDelete) {
        this.data.remove(dataToDelete);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : data) {
            stringBuilder.append('*').append(element).append('\n');
        }
        return stringBuilder.toString();
    }
}
