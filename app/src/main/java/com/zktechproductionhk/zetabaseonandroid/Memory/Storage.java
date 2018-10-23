package com.zktechproductionhk.zetabaseonandroid.Memory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Storage {
    public interface OnChangeListner {
        void onChanged();
    }

    public interface Monitor {
        void onTrigger(String data);
    }

    enum OPTCODE {
        WIPE("&actWipe");
        String code;

        OPTCODE(String code) {
            this.code = code;
        }
    }

    private static String TAG = "[Storage]";
    private DataNode root;
    private HashMap<String, Monitor> monitors;

    public Storage() {
        this.root = new DataNode("/");
        this.monitors = new HashMap<>();
    }

    public Storage(DataNode root) {
        this.root = root;
        this.monitors = new HashMap<>();
    }

    public void write(String path, String data, OnChangeListner listner) {
        this.write(path, data);
        listner.onChanged();
    }

    public void write(String path, String data) {
        QueryPath result = root.traverse(path, true);
        result.self.setData(data);
        invalidate(path, data);
    }

    public void append(String path, String data, OnChangeListner listner) {
        this.append(path, data);
        if (listner != null) listner.onChanged();
    }

    public void append(String path, String data) {
        Long timestamp = System.currentTimeMillis();
        String hash = md5(path + timestamp.toString());
        QueryPath result = root.traverse(path, true);
        DataNode child = new DataNode(hash);
        child.setData(data);
        result.self.nodes.put(hash, child);
        invalidate(path, data);
    }

    public void wipe(String path, OnChangeListner listner) {
        this.wipe(path);
        listner.onChanged();
    }

    public void wipe(String path) {
        QueryPath result = root.traverse(path, false);
        String childPath = result.self.path;
        result.parent.nodes.remove(childPath);
        invalidate(path, OPTCODE.WIPE.code);
    }

    public void purge(OnChangeListner listner) {
        this.root = new DataNode("/");
        listner.onChanged();
    }

    public void purge() {
        this.root = new DataNode("/");
    }

    public DataNode read(String path) {
        return root.traverse(path, false).self;
    }

    public void monitor(String path, Monitor listener) {
        monitors.put(path, listener);
    }

    private boolean isSubset(String key, String path) {
        List<String> keyTokens = Arrays.asList(DataNode.tokenize(key));
        List<String> pathTokens = Arrays.asList(DataNode.tokenize(path));
        return pathTokens.containsAll(keyTokens);
    }

    private void invalidate(String path, String data) {
        Iterator<String> monitor = monitors.keySet().iterator();
        while (monitor.hasNext()) {
            String key = monitor.next();
            if (isSubset(key, path))
                monitors.get(key).onTrigger(data);
        }
    }

    private String md5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "root=" + root +
                '}';
    }
}
