package ru.belka.easycooking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.*;
import android.util.Log;
import java.io.*;
import java.net.*;

public class FilesUploadingTask extends AsyncTask<Void, Void, String> {
    private final String filePath;
    public static String API_FILES_UPLOADING_PATH;
    public static final String FORM_FILE_NAME = "photo";
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public FilesUploadingTask(String filePath, String urlTo, Context context) {
        this.filePath = filePath;
        API_FILES_UPLOADING_PATH = urlTo;
        FilesUploadingTask.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;

        try {
            URL uploadUrl = new URL(API_FILES_UPLOADING_PATH);

            HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");

            // Задание необходимых свойств запросу
            connection.setRequestProperty("Connection", "Keep-Alive");
            String boundary = "----WebKitFormBoundary9xFB2hiUhzqbBQ4M";
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ boundary);

            // Создание потока для записи в соединение
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // Формирование multipart контента

            // Начало контента
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            // Заголовок элемента формы
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    FORM_FILE_NAME + "\"; filename=\"" + filePath + "\"" + lineEnd);
            // Тип данных элемента формы
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            // Конец заголовка
            outputStream.writeBytes(lineEnd);

            // Поток для считывания файла в оперативную память
            FileInputStream fileInputStream = new FileInputStream(filePath);

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int serverResponseCode = connection.getResponseCode();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            if(serverResponseCode == 200) {
                result = readStream(connection.getInputStream());
            } else {
                result = readStream(connection.getErrorStream());
            }
        } catch (IOException e) {
            Log.e("log_error", e.toString());
        }
        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        showDialog("Ваша заявка успешно принята на обработку\n\nВ ближайшее время администраторы проверят Ваше предложение.", "Результат");
    }
    public void showDialog(String text, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Продолжить",
                        (dialog, id) -> dialog.dismiss()).create();
        builder.show();
    }

    public static String readStream(InputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }
}