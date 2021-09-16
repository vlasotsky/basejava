package com.basejava.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStream {

    private static int minValue(int[] values) {
//        return Arrays.stream(values).map(Math::abs).distinct().sorted().reduce(0, (a1, a2) -> a1 * 10 + a2);
        return Arrays.stream(values).distinct().sorted().reduce(0, (a1, a2) -> a1 * 10 + a2);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(p -> p).sum();
        return integers.stream().filter((element) -> (sum % 2 == 0) == (element % 2 == 0)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
//        int result = minValue(new int[]{-9, -8});
//        System.out.println(result);
//
//        result = minValue(new int[]{0, 8, -5});
//        System.out.println(result);

        int result = minValue(new int[]{1, 2, 3, 3, 2, 3});
        System.out.println(result);

        result = minValue(new int[]{9, 8});
        System.out.println(result);

        System.out.println(oddOrEven(new ArrayList<>() {
            {
                add(1);
                add(2);
                add(-3);
                add(4);
                add(5);
            }
        }));


        System.out.println(oddOrEven(new ArrayList<>() {
            {
                add(1);
                add(2);
                add(-3);
                add(-4);
            }
        }));
    }
}
