package com.wdeath.wdlang.script.modules;

import com.wdeath.wdlang.script.modules.collections.collections;
import com.wdeath.wdlang.script.modules.date.date;
import com.wdeath.wdlang.script.modules.downloader.downloader;
import com.wdeath.wdlang.script.modules.files.files;
import com.wdeath.wdlang.script.modules.gzip.gzip;
import com.wdeath.wdlang.script.modules.java.java;
import com.wdeath.wdlang.script.modules.json.json;
import com.wdeath.wdlang.script.modules.math.math;
import com.wdeath.wdlang.script.modules.regex.regex;
import com.wdeath.wdlang.script.modules.types.types;
import com.wdeath.wdlang.script.modules.yaml.yaml;
import com.wdeath.wdlang.script.modules.zip.zip;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class Modules {

    private ConcurrentHashMap<String, Class<? extends Module>> map;
    private ConcurrentHashMap<String, Module> mapObj;

    public Modules(){
        map = new ConcurrentHashMap<>();
        mapObj = new ConcurrentHashMap<>();
        set("collections", collections.class);
        set("date", date.class);
        set("downloader", downloader.class);
        set("files", files.class);
        set("gzip", gzip.class);
        set("java", java.class);
        set("json", json.class);
        set("math", math.class);
        set("regex", regex.class);
        set("types", types.class);
        set("yaml", yaml.class);
        set("zip", zip.class);
        set("std", StdModule.class);
    }

    public void set(String name, Class<? extends Module> m){
        map.put(name, m);
    }
    public void set(String name, Module m){
        mapObj.put(name, m);
    }

    public Class<? extends Module> get(String name){
        return map.get(name);
    }
    public Module getObj(String name){
        return mapObj.get(name);
    }

    public Module newInstance(String name){
        Class<? extends Module> clazz = map.get(name);
        if(clazz == null)
            return null;
        Module module = null;
        try {
            module = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return module;
    }
}
