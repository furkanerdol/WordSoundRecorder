package com.example.wordsoundrecorder;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;

public class WordManagement {

    private static final String LOG_TAG = "WORD_SOUND_RECORDER";
    private FragmentActivity activity;
    private List<String> words;
    private String currentWord;
    private int currentIndex;


    public WordManagement(FragmentActivity activity) {
        this.activity = activity;
        initialize();
    }

    private void initialize() {
        words = new ArrayList<>();

        try {
            readWordsXml(words);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        setCurrentWord(words.get(0));
        setCurrentIndex(0);
    }

    private void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    private int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getNextWord() {
        String word;
        if (getCurrentIndex() == (words.size() - 1)) {
            word = words.get(0);
            setCurrentWord(word);
            setCurrentIndex(0);
        } else {
            word = words.get(getCurrentIndex() + 1);
            setCurrentWord(word);
            setCurrentIndex(getCurrentIndex() + 1);
        }
        return word;
    }

    public String getPrevWord() {
        String word;
        if (getCurrentIndex() == 0) {
            word = words.get(words.size() - 1);
            setCurrentWord(word);
            setCurrentIndex(words.size() - 1);
        } else {
            word = words.get(getCurrentIndex() - 1);
            setCurrentWord(word);
            setCurrentIndex(getCurrentIndex() - 1);
        }
        return word;
    }

    private void readWordsXml(List<String> words) throws IOException, XmlPullParserException {
        int eventType = 0;

        Resources res = activity.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.words);

        eventType = xpp.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.TEXT) {
                words.add(xpp.getText());
                Log.e(LOG_TAG, xpp.getText());
            }
            eventType = xpp.next();
        }
    }

}
