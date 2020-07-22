package com.example.wordsoundrecorder;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import androidx.fragment.app.FragmentActivity;

public class FileManagement {

    private FragmentActivity activity;
    private Context context;
    private String fileName;
    private final static String FOLDER_NAME = "/WordSoundRecorder/";
    private final static String WORD_NAME = "word";
    private final static String FILE_EXTENSION = ".m4a";

    public FileManagement(FragmentActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void checkFilePath() {
        fileName = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        fileName += FOLDER_NAME;

        File f = new File(fileName);
        if (!f.exists()) {
            if (f.mkdirs()) {
                Toast.makeText(activity,
                        "Klasör oluşturma başarılı...",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(activity,
                        "Klasör oluşturma başarısız!",
                        Toast.LENGTH_SHORT)
                        .show();
            }

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
