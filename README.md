# ZetabaseOnAndroid


#### About Zetabase

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
