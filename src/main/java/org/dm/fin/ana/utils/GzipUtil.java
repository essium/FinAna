package org.dm.fin.ana.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
    public static void save(Object data, String file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gos = new GZIPOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(gos)) {
            oos.writeObject(data);
            oos.flush();
        } catch (IOException | SecurityException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    public static Object load(String file) {
        Object obj = null;
        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gis = new GZIPInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(gis)) {
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
        return obj;
    }
}
