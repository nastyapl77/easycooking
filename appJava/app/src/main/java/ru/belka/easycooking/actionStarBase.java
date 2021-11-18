package ru.belka.easycooking;

import android.content.Context;

import java.io.File;

public class actionStarBase {
    public String[] getFiles(Context context) {
        int i = 0;
        String[] str = new String[200];
        String path = context.getFilesDir().toString();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                str[i] = file.getName();
                i++;
            }
        }
        return str;
    }
    public String getRecipe(Context context, long id) {
        String path = "recipe_" + id + ".json";
        cacheData cd = new cacheData();
        return cd.getCache(path, context);
    }
    public String getRecipe(Context context, String path) {
        cacheData cd = new cacheData();
        return cd.getCache(path, context);
    }
    public void setRecipe(Context context, long id, String result) {
        String path = "recipe_" + id + ".json";
        cacheData cd = new cacheData();
        cd.saveCache(path, result, context);
    }
    public void deleteRecipe(Context context, long id) {
        String path = "recipe_" + id + ".json";
        cacheData cd = new cacheData();
        cd.deleteCache(path, context);
    }
}

