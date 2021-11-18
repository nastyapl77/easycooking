package ru.belka.easycooking;

import android.content.Context;
import android.widget.Toast;
import java.io.*;

public class cacheData {
    public void saveCache(String file, String text, Context ctx) {
        try {
            File gpxfile = new File(ctx.getFilesDir(), file);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (Exception e){
            Toast.makeText(ctx, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteCache(String file, Context ctx) {
        try {
            File gpxfile = new File(ctx.getFilesDir(), file);
            gpxfile.delete();
        } catch (Exception e){
            Toast.makeText(ctx, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public String getCache(String file, Context ctx) {
        File myFile = new File(ctx.getFilesDir(), file);
        String line;
        String answer;
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            try {
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                answer = stringBuilder.toString();
            } catch (IOException e) {
                return "error";
            }
        } catch (FileNotFoundException e) {
            return "error";
        }
        return answer;
    }
}
