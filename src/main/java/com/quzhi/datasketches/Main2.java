package com.quzhi.datasketches;

import com.yahoo.sketches.frequencies.ErrorType;
import com.yahoo.sketches.frequencies.ItemsSketch;

import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * Created by zhiqu on 3/17/17.
 */
public class Main2 {

    private static final int TEST_NUM = 10000;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) throws Exception {
        ItemsSketch<String> sketch = new ItemsSketch<String>(131072);
        for (int i = 0; i < TEST_NUM; i++) {
            String randomStr = getRandomString();
            sketch.update(randomStr);
            sketch.update(randomStr);
        }
        System.out.println("Active num: " + sketch.getNumActiveItems());
        System.out.println("Stream length: " + sketch.getStreamLength());
        ItemsSketch.Row<String>[] items = sketch.getFrequentItems(ErrorType.NO_FALSE_NEGATIVES);
        System.out.println("Frequent items: " + items.length);
//        System.out.println(ItemsSketch.Row.getRowHeader());
//        for (ItemsSketch.Row<String> row : items) {
//            System.out.println(row.toString());
//        }
    }

    private static String getRandomString() {
        String randomStr = new BigInteger(2000, RANDOM).toString(32);
        return randomStr;
    }
}
