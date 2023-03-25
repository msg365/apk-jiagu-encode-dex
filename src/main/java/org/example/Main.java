package org.example;

import com.google.gson.Gson;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class Main {
    public static String firstDexMd5 = null;

    public static void main(String[] args) {
        try {
            Main.doIt("/home/u1/1/build/src/classes.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec501", 66);
            Main.doIt("/home/u1/1/build/src/classes2.dex", "/home/u1/1/build/assets/70e8f53d6b128db1c7844dca43fec502", 66);
            if (true) {
                // key and iv from md5 sum result from first dex file decode
                System.out.println("系统配置文件加密");
                System.out.println("ki: " + Main.firstDexMd5);
                HashMap<String, String> conf = new HashMap<>();
                conf.put("v2", "opt");
                conf.put("v3", "pathList");
                conf.put("v4", "dexElements");
                conf.put("v5", "makePathElements");
                conf.put("v6", "attach");
                conf.put("v7", "android.app.ContextImpl");
                conf.put("v8", "mOuterContext");
                conf.put("v9", "mMainThread");
                conf.put("v10", "android.app.ActivityThread");
                conf.put("v11", "mInitialApplication");
                conf.put("v12", "mAllApplications");
                conf.put("v13", "mPackageInfo");
                conf.put("v14", "android.app.LoadedApk");
                conf.put("v15", "mApplication");
                conf.put("v16", "mApplicationInfo");
                conf.put("v17", "deb");
                conf.put("v18", ".dex");
                String json = new Gson().toJson(conf);
                System.out.println("json: " + json);
                String encJson = encString(json, firstDexMd5.substring(0, 16), firstDexMd5.substring(16));
                System.out.println("enc: " + encJson);
            }

            if (true) {
                String packageName = "com.wnsr001.cocosandroid";
                byte[] bs = packageName.getBytes(StandardCharsets.UTF_8);
                Arrays.sort(bs);
                String ki = md5(bs);
                String key = ki.substring(0, 16);
                String iv = ki.substring(16);
                System.out.println("应用配置文件");
                System.out.println("ki: " + ki);
                HashMap<String, Object> conf = new HashMap<>();
                conf.put("v1", "org.App");
                conf.put("v2", "66");
                conf.put("v3", "70e8f53d6b128db1c7844dca43fec501");
                conf.put("v4", "70e8f53d6b128db1c7844dca43fec502");
                String json = new Gson().toJson(conf);
                System.out.println("json: " + json);
                String jsonEnc = encString(json, key, iv);
                System.out.println("enc: " + jsonEnc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decString(String encStr, String key, String iv) {
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

    public static String encString(String txt, String key, String iv) {
        String encStr = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] bytes = cipher.doFinal(txt.getBytes());
            encStr = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encStr;
    }

    public static void doIt(String src, String target, int num) throws Exception {
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
        if (firstDexMd5 == null) {
            firstDexMd5 = md5(srcBytes);
        }
    }

    public static String md5(byte[] bs) {
        String s = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(bs);
            s = new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}