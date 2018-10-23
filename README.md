# ZetabaseOnAndroid

### About Zetabase

Zetabase is a NoSQL database provides a quick way to store or cache data.

### Operations

#### Class name: **Zetabase**
Function name | Parameter(s) | Description
------------ | ------------- | -------------
getInstance | context:Context | Return a Zetabase instance.
write | path:String, data:Object | Write data to designated path, the missing path will be created automatically.
write | path:String, data:Object, OnChangeListener:interface | Write function with a callback.
append | path:String, data:Object | Append data to designated path with a hash key, the missing path will be created automatically.
append | path:String, data:Object, OnChangeListener:interface | Append function with a callback.
read | path:String | Return an Object from the designated path.
wipe | path:String | Delete the data node from the designated path.
wipe | path:String, OnChangeListener:interface | Delete function with a callback.
monitor | path:String, Monitor:interface | Set a monitor for the designated path, the action specified in the monitor will be trigger when the path invoke changes.
getDataNode | path:String | Return the data node.

#### Class name: **DataNode**
Function name | Parameter(s) | Description
------------ | ------------- | -------------
getPath | null | Return the path of the data node.
getData | null | Return the data of the data node.
getChildrenPath | null | Return a path iterator for all the children data node.
getChild | path:String | Return the child data node.

### Example

#### Demo class
```JAVA
class Student {
    public String id, name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
```

##### Initialize Zetabase
```JAVA
zetabase = Zetabase.getInstnace(this);
```
##### Wipe data by path.
```JAVA
zetabase.wipe("/");
```

##### Set monitors
```JAVA
zetabase.monitor("/RoomA/students", new Storage.Monitor() {
    @Override
    public void onTrigger(String data) {
        Student std = (Student) JsonReader.jsonToJava(data);
        Log.d(TAG, "RoomA, A new student comes: " + std.toString());
    }
});
```

##### Write to Zetabase without a callback
``` JAVA
zetabase.write("/RoomA/students/Alice", new Student("100000A", "Alice"));
```

##### Write to Zetabase with a callback
``` JAVA
zetabase.write("/RoomA/students/Alice", new Student("100000A", "Alice"), new Storage.OnChangeListner() {
    @Override
    public void onChanged() {
        Log.d(TAG, "Alice is updated");
    }
});
```

##### Append to Zetabase without a callback
```JAVA
zetabase.append("/RoomA/students", new Student("100001A", "Bob"));
```

##### Append to Zetabase with a callback
```JAVA
zetabase.append("/RoomA/students", new Student("100002A", "Calvin"), new Storage.OnChangeListner() {
    @Override
    public void onChanged() {
        Log.d(TAG, "Peter is created");
    }
});
```

##### Read from a path
```JAVA
Student alice = (Student) zetabase.read("/RoomA/students/Alice");
Log.d(TAG, alice.toString());
```

##### List all items without knowing the actual path.
```JAVA
Node roomAStd = zetabase.getDataNode("/RoomA/students");
Iterator<String> items = roomAStd.getChildrenPath();
while (items.hasNext()) {
    Student std = (Student) JsonReader.jsonToJava(roomAStd.getChild(items.next()).getData());
    Log.d(TAG, "Student: " + std.toString());
}
```
