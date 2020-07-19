package com.example.wordsoundrecorder;

import android.os.Environment;

import java.io.File;

public class FileManagement {

    private String fileName;
    private final static String FOLDER_NAME = "/WordSoundRecorder/";
    private final static String WORD_NAME = "word";
    private final static String FILE_EXTENSION = ".m4a";

    public FileManagement() {
        initialize();
    }

    private void initialize() {
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += FOLDER_NAME;

        File f = new File(fileName);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public String getFileName(String currentWordName) {
        String transientFileName = new String();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        transientFileName += fileName;
        transientFileName += WORD_NAME + "_" + currentWordName;
        transientFileName += "_" + ts + FILE_EXTENSION;

        return transientFileName;
    }
}
