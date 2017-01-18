package com.kaidi.swallow;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Liar {


    // 文件读取处理时的缓存大小
    private int bufferSize = 1024 * 4;

    // 排除目录列表
    private List<String> excludePath = new ArrayList<>();
    // 排除文件列表
    private List<String> excludeFile = new ArrayList<>();
    // 排除文件类型列表，使用后缀标志
    private List<String> excludeExtension = new ArrayList<>();

    // 配置文件地址
    private String configFilePath;

    // 口令, -128-127
    private int key;

    // 是否显示详细标志.true@显示；false@不显示
    private boolean verbose;

    public void scan(String path, List<File> all) throws IOException {
        File scanner = new File(path);
        File[] filesInPath = scanner.listFiles();
        if (filesInPath != null) {
            for (File f : filesInPath) {
                if (f.isDirectory()) {
                    if (filterDirectory(f)) {
                        scan(f.getPath(), all);
                    } else {
                        continue;
                    }
                }
                if (f.isFile()) {
                    if (filterFile(f)) {
                        all.add(f);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    private boolean filterFile(File f) {
        String extension = FilenameUtils.getExtension(f.getPath());
        String fileName = f.getName();
        return !(excludeExtension.contains(extension) || excludeFile.contains(fileName));
    }

    private boolean filterDirectory(File f) {
        return !excludePath.contains(f.getName());
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
                    if(buffer[i] != '\n'){
                        buffer[i] ^= 20;
                    }
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
        excludeExtension.add("md");
        excludeFile.add(".gitignore");
        excludePath.add(".idea");
        excludePath.add(".git");
        excludePath.add("log");
        excludePath.add("node_modules");
        return path;
    }

    public void liar(String[] args) throws IOException {
        List<File> files = new ArrayList<File>();
        scan(nightMan(args[0]), files);
        //lie(files);
    }
}
