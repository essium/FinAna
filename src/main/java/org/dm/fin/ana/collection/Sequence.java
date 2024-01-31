package org.dm.fin.ana.collection;

import org.dm.fin.ana.model.Tick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Sequence<T extends Tick> implements Serializable {
    public TreeMap<Long, T> tree;
    public transient ArrayList<T> array;

    public Sequence() {
        tree = new TreeMap<>();
    }

    public void add(T element) {
        tree.put(element.date, element);
    }

    public void createArray() {
        array = new ArrayList<>();
        for (Map.Entry<Long, T> entry : tree.entrySet()) {
            array.add(entry.getValue());
        }
    }
}
