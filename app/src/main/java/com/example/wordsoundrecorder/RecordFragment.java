package com.example.wordsoundrecorder;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordFragment extends Fragment {

    private MediaRecorder mediaRecorder;

    private static final String RECORDING_TEXT = "Kaydediliyor...";
    private static final String COMMENT_TEXT = "Kelimenin sesini kaydetmek için butona bas";
    private static final String LOG_TAG = "SOUND_RECORDER_LOG";

    private Button buttonStartRecord, buttonLeft, buttonRight;
    private TextView textViewRecord, textViewWord;

    private WordManagement wordManager;
    private FileManagement fileManager;

    private Timer timer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  view.findViewById(R.id.button_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RecordFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        buttonStartRecord = view.findViewById(R.id.button_record);
        buttonLeft = view.findViewById(R.id.button_left);
        buttonRight = view.findViewById(R.id.button_right);
        textViewRecord = view.findViewById(R.id.text_record_status);
        textViewWord = view.findViewById(R.id.text_word);

        wordManager = new WordManagement(getActivity());
        fileManager = new FileManagement();

        timer = new Timer();

        buttonStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPermissions()) {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecorder.setAudioChannels(1);
                    mediaRecorder.setAudioEncodingBitRate(128000);
                    mediaRecorder.setAudioSamplingRate(44100);
                    mediaRecorder.setOutputFile(fileManager.getFileName(wordManager.getCurrentWord()));
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    mediaRecorder.start();
                    buttonStartRecord.setEnabled(false);
                    textViewRecord.setText(RECORDING_TEXT);

                    final Handler handler = new Handler();

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textViewRecord.setText(COMMENT_TEXT);
                                    buttonStartRecord.setEnabled(true);
                                    textViewWord.setText(wordManager.getNextWord());
                                }
                            });
                        }
                    };
                    timer.schedule(task, 3000);
                }
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewWord.setText(wordManager.getPrevWord());
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewWord.setText(wordManager.getNextWord());
            }
        });

        textViewWord.setText(wordManager.getCurrentWord());

    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}