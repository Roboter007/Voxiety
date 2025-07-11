package de.Roboter007.voxiety.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class JavaUtils {

    @SafeVarargs
    public static <E> ArrayList<E> arrayListOf(E... e) {
        return new ArrayList<>(Arrays.stream(e).toList());
    }

}
