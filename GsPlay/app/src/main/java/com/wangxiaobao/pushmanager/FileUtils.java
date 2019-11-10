package com.wangxiaobao.pushmanager;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by jack on 2018/1/25.
 */

public class FileUtils {


    public static String readFile(String file_path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(file_path, "r");
        String str = file.readUTF();
        file.close();
        return str;
    }
}