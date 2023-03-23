package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        try {
            new Main().doIt("/home/u1/1/build/classes.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec501", 66);
            new Main().doIt("/home/u1/1/build/classes2.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec502", 66);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doIt(String src, String target, int num) throws Exception {
        File srcFile = new File(src);
        File targetFile = new File(target);
        int srcLen = (int) srcFile.length();
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        byte[] srcBytes = new byte[srcLen];
        new FileInputStream(srcFile).read(srcBytes);

        byte[] targetBytes = new byte[srcLen];
        for (int i = 0; i < srcLen; i++) {
            byte b1 = srcBytes[i];
            byte b2 = (byte) (b1 ^ num);
            targetBytes[i] = b2;
        }
        new FileOutputStream(targetFile).write(targetBytes);
    }
}