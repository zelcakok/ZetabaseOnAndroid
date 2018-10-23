package com.zktechproductionhk.zetabaseonandroid.InternalStorage;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class IO {
    public interface OnFileUpdateListener {
        void onUpdate();
    }

    private String filename;
    private Context context;

    public IO(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    public void write(byte[] dataStream, @Nullable OnFileUpdateListener listener) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(this.filename, Context.MODE_PRIVATE);
            outputStream.write(dataStream);
            outputStream.close();
            if (listener != null) listener.onUpdate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        FileInputStream inputStream;
        StringBuilder content = new StringBuilder();
        try {
            inputStream = context.openFileInput(this.filename);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufReader = new BufferedReader(reader);
            String line;
            while ((line = bufReader.readLine()) != null)
                content.append(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return content.toString();
        }
    }

    public boolean wipe() {
        return new File(context.getFilesDir(), this.filename).delete();
    }
}
