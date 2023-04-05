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
import java.util.*;

public class Main {
    public static String firstDexMd5 = null;
    public static String packageName = "com.e4d25.c34fa";
    public static String appName = "org.App";
    public static String srcDir = "/home/u1/Git/jiagu.ky.v1/src.build";
    public static String code = "66";
    public static String firstDexName  = "110bdcbb6c486590";
    public static String secondDexName = "cb90d78184cc1f6a";
    public static String dexKey = "afd47940fa9b8410";
    public static String dexIv  = "0b5dc28369b92120";

    public static void main(String[] args) {
        try {
            encFile(srcDir + "/src/classes.dex", srcDir + "/assets/" + firstDexName, dexKey, dexIv);
            encFile(srcDir + "/src/classes2.dex", srcDir + "/assets/" + secondDexName, dexKey, dexIv);
            if (true) {
                // key and iv from md5 sum result from first dex file decode
                System.out.println("系统配置文件加密");
                HashMap<String, String> conf = new HashMap<>();
                conf.put("v1", "opt");
                conf.put("v2", "pathList");
                conf.put("v3", "dexElements");
                conf.put("v4", "makePathElements");
                conf.put("v5", "attach");
                conf.put("v6", "android.app.ContextImpl");
                conf.put("v7", "mOuterContext");
                conf.put("v8", "mMainThread");
                conf.put("v9", "android.app.ActivityThread");
                conf.put("va", "mInitialApplication");
                conf.put("vb", "mAllApplications");
                conf.put("vc", "mPackageInfo");
                conf.put("vd", "android.app.LoadedApk");
                conf.put("ve", "mApplication");
                conf.put("vf", "mApplicationInfo");
                conf.put("vg", "deb");
                conf.put("vh", ".dex");
                String json = new Gson().toJson(conf);
                System.out.println("json: " + json);
                String encJson = encString(json, dexKey, dexIv);
                System.out.println("enc: " + encJson);
            }

            if (true) {
                byte[] bs = packageName.getBytes(StandardCharsets.UTF_8);
                Arrays.sort(bs);
                String ki = md5(bs);
                System.out.println("应用配置文件");
                HashMap<String, Object> conf = new HashMap<>();
                conf.put("v1", appName);
                conf.put("v2", code);
                conf.put("v3", firstDexName);
                conf.put("v4", secondDexName);
                conf.put("v5", dexKey);
                conf.put("v6", dexIv);
                String json = new Gson().toJson(conf);
                System.out.println("json: " + json);
                String jsonEnc = encString(json, ki.substring(0, 16), ki.substring(16));
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

    public static byte[] enc(byte[] bs, String key, String iv) {
        byte[] rs = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            rs = cipher.doFinal(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static byte[] dec(byte[] data, String key, String iv) {
        byte[] rs = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            rs = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void decFile(String srcPath, String destPath, String key, String iv) {
        try {
            byte[] src = new FileInputStream(srcPath).readAllBytes();
            byte[] dec = dec(src, key, iv);
            new FileOutputStream(destPath).write(dec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encFile(String srcPath, String destPath, String key, String iv) {
        try {
            byte[] src = new FileInputStream(srcPath).readAllBytes();
            byte[] enc = enc(src, key, iv);
            new FileOutputStream(destPath).write(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void encDex(String src, String target, int code) throws Exception {
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
            byte b2 = (byte) (b1 ^ code);
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