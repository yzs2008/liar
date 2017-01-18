package com.kaidi.swallow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Liar {


    private int bufferSize = 1024 * 10;

    private List<String> excludePath = new ArrayList<>();

    public void scan(String path, List<File> all) throws IOException {
        File scanner = new File(path);
        File[] filesInPath = scanner.listFiles();
        if (filesInPath != null) {
            for (File f : filesInPath) {
                if (f.isDirectory()) {
                    if(excludePath.contains(f.getPath())){
                        continue;
                    }else {
                        scan(f.getPath(), all);
                    }
                }
                if (f.isFile()) {
                    all.add(f);
                }
            }
        }
    }


    public void lie(List<File> files) throws IOException, LogicException {

        for (File f : files) {
            //修改文件名
            String temFileName = renameFile(f);
            //修改文件内容
            modifyFile(f, temFileName);
            //删除临时文件
            removeTempFile(temFileName);
        }

    }

    private void modifyFile(File f, String temFileName) throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(temFileName);
            out = new FileOutputStream(f);
            int bufferLen = 4;//1024*10;
            byte[] buffer = new byte[bufferLen];

            int count = bufferLen;
            while (count == bufferLen) {
                count = in.read(buffer);
                if (count == -1) break;

                for (int i = 0; i < count; i++) {
                    buffer[i] ^= 20;
                }
                out.write(buffer, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private void removeTempFile(String temFileName) throws LogicException {
        if (!new File(temFileName).delete()) {
            throw new LogicException("remove temp file failed.");
        }
    }

    private String renameFile(File f) throws LogicException {
        String temFileName = f.getPath() + ".lie";
        if (!f.renameTo(new File(temFileName))) {
            throw new LogicException("rename file failed.");
        }
        return temFileName;
    }

    public String nightMan(String path) {
        return path;
    }

    public void liar(String[] args) throws IOException {
        List<File> files = new ArrayList<File>();
        scan(nightMan(args[0]), files);
        //lie(files);
    }
}
