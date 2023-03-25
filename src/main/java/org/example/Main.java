package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            if (true) {
                String encStr = new Main().encString("ok", "1234567890123456", "1234567890123456");
                System.out.println(encStr);
                String decStr = new Main().decString(encStr, "1234567890123456", "1234567890123456");
                System.out.println(decStr);
                return;
            }
            new Main().doIt("/home/u1/1/build/classes.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec501", 66);
            new Main().doIt("/home/u1/1/build/classes2.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec502", 66);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decString(String encStr, String key, String iv) {
        String decStr = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encBytes = Base64.getDecoder().decode(encStr.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = cipher.doFinal(encBytes);
            decStr = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decStr;
    }

    public String encString(String txt, String key, String iv) {
        String encStr = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] bytes = cipher.doFinal(txt.getBytes());
            System.out.println("enc len: " + bytes.length);
            encStr = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encStr;
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