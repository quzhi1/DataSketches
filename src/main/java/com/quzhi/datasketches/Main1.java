package com.quzhi.datasketches;

import com.yahoo.memory.NativeMemory;
import com.yahoo.sketches.ArrayOfStringsSerDe;
import com.yahoo.sketches.frequencies.ErrorType;
import com.yahoo.sketches.frequencies.ItemsSketch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by zhiqu on 3/17/17.
 */
public class Main1 {

    public static void main(String[] args) throws Exception {
        writeItemToFile();
        readItemFromFile();
    }

    private static void writeItemToFile() throws Exception {
        ItemsSketch<String> sketch1 = new ItemsSketch<String>(64);
        sketch1.update("a");
        sketch1.update("a");
        sketch1.update("b");
        sketch1.update("c");
        sketch1.update("a");
        sketch1.update("d");
        sketch1.update("a");
        FileOutputStream out1 = new FileOutputStream(new File("FrequentStringsSketch1.bin"));
        out1.write(sketch1.toByteArray(new ArrayOfStringsSerDe()));
        out1.close();

        ItemsSketch<String> sketch2 = new ItemsSketch<String>(64);
        sketch2.update("e");
        sketch2.update("a");
        sketch2.update("f");
        sketch2.update("f");
        sketch2.update("f");
        sketch2.update("g");
        sketch2.update("a");
        sketch2.update("f");
        FileOutputStream out2 = new FileOutputStream(new File("FrequentStringsSketch2.bin"));
        out2.write(sketch2.toByteArray(new ArrayOfStringsSerDe()));
        out2.close();
    }

    private static void readItemFromFile() throws Exception {
        FileInputStream in1 = new FileInputStream(new File("FrequentStringsSketch1.bin"));
        byte[] bytes1 = new byte[in1.available()];
        in1.read(bytes1);
        in1.close();
        ItemsSketch<String>
            sketch1 =
            ItemsSketch.getInstance(new NativeMemory(bytes1), new ArrayOfStringsSerDe());

        FileInputStream in2 = new FileInputStream(new File("FrequentStringsSketch2.bin"));
        byte[] bytes2 = new byte[in2.available()];
        in2.read(bytes2);
        in2.close();
        ItemsSketch<String>
            sketch2 =
            ItemsSketch.getInstance(new NativeMemory(bytes2), new ArrayOfStringsSerDe());

        // we could merge sketch2 into sketch1 or the other way around
        // this is an example of using a new sketch as a union and keeping the original sketches intact
        ItemsSketch<String> union = new ItemsSketch<String>(64);
        union.merge(sketch1);
        union.merge(sketch2);

        ItemsSketch.Row<String>[] items = union.getFrequentItems(ErrorType.NO_FALSE_POSITIVES);
        System.out.println("Frequent items: " + items.length);

//        System.out.println(ItemsSketch.Row.getRowHeader());
//            for (ItemsSketch.Row<String> row : items) {
//                System.out.println(row.toString());
//            }
        for (ItemsSketch.Row<String> item : items) {
            System.out.println("Item " + item.getItem()
                               + " has est: " + item.getEstimate()
                               + " has ub: " + item.getUpperBound()
                               + " has lb: " + item.getLowerBound());
        }
    }
}
