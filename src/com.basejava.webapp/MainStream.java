package com.basejava.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));

        System.out.println(minValue(new int[]{9, 8}));

        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(1, 2, -3, -4))));

        System.out.println(oddOrEven(new ArrayList<>(Arrays.asList(8, 9))));
    }


    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (a1, a2) -> a1 * 10 + a2);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .mapToInt(p -> p)
                .sum();
        return integers.stream()
                .filter((element) -> ((sum + element) % 2 != 0))
                .collect(Collectors.toList());
    }
}
