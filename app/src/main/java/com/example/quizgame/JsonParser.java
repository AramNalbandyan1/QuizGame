package com.example.quizgame;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.URL;
import java.util.stream.Collectors;

public class JsonParser {

    public static String getJSONFromFile(Context context, String filename){
        String jsonText = "";
        try{
            InputStream inputStream = context.getAssets().open(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null){
                jsonText += line + "\n";
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();}
        System.out.println(jsonText);

        return jsonText;

    }

    public static String getJSONFromURL(String strUrl){
        String jsonText = "";
        try{
            URL url = new URL(strUrl);
            InputStream is = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = bufferedReader.readLine()) != null){
                jsonText += line + "\n";
            }
            bufferedReader.close();
            is.close();
        } catch (Exception e) {e.printStackTrace();}

        return jsonText;

    }
}

