# ZetabaseOnAndroid
An NoSQL database provides a quick way to store or cache data.

## DEMO
``` Java
package com.zktechproductionhk.zetabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.cedarsoftware.util.io.JsonReader;
import com.zktechproductionhk.zetabase.InternalStorage.IO;
import com.zktechproductionhk.zetabase.Memory.Node;
import com.zktechproductionhk.zetabase.Memory.Storage;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "[Zetabase]";
    private Zetabase zetabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Zetabase
        zetabase = Zetabase.getInstnace(this);

        //Wipe data by path.
        zetabase.wipe("/");

        //Set monitors
        zetabase.monitor("/RoomA/students", new Storage.Monitor() {
            @Override
            public void onTrigger(String data) {
                Student std = (Student) JsonReader.jsonToJava(data);
                Log.d(TAG, "RoomA, A new student comes: " + std.toString());
            }
        });

        //Write to Zetabase without a callback
        zetabase.write("/RoomA/students/Alice", new Student("100000A", "Alice"));

        //Write to Zetabase with a callback
        zetabase.write("/RoomA/students/Alice", new Student("100000A", "Alice"), new Storage.OnChangeListner() {
            @Override
            public void onChanged() {
                Log.d(TAG, "Alice is updated");
            }
        });

        //Append to Zetabase without a callback
        zetabase.append("/RoomA/students", new Student("100001A", "Bob"));

        //Append to Zetabase with a callback
        zetabase.append("/RoomA/students", new Student("100002A", "Calvin"), new Storage.OnChangeListner() {
            @Override
            public void onChanged() {
                Log.d(TAG, "Peter is created");
            }
        });

        //Read from a path
        Student alice = (Student) zetabase.read("/RoomA/students/Alice");
        Log.d(TAG, alice.toString());

        //List all items without knowing the actual path.
        Node roomAStd = zetabase.getDataNode("/RoomA/students");
        Iterator<String> items = roomAStd.getChildrenPath();
        while (items.hasNext()) {
            Student std = (Student) JsonReader.jsonToJava(roomAStd.getChild(items.next()).getData());
            Log.d(TAG, "Student: " + std.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //The data will be stored to a file when saveState is called.
        zetabase.saveState(new IO.OnFileUpdateListener() {
            @Override
            public void onUpdate() {
                Toast.makeText(getApplicationContext(), "All data is saved", Toast.LENGTH_LONG).show();
            }
        });
    }
}


```
