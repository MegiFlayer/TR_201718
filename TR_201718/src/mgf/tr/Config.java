/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kp.jngg.json.JSONArray;
import kp.jngg.json.JSONException;
import kp.jngg.json.JSONObject;
import kp.jngg.json.JSONTokener;
import kp.jngg.math.Vector2;

/**
 *
 * @author Asus
 */
public final class Config
{
    private Config() {}
    
    private static final File FILE = new File("data" + File.separator + "config.json");
    private static JSONObject PROPS;
    
    private static JSONObject props()
    {
        if(PROPS == null)
        {
            if(!FILE.exists() || !FILE.isFile())
            {
                PROPS = new JSONObject();
                store();
            }
            else try { PROPS = load(); }
            catch(IOException ex) { PROPS = new JSONObject(); store(); }
        }
        return PROPS;
    }
    
    private static String[] splitPath(String name)
    {
        String[] parts = name.split("\\.", 2);
        return parts.length < 2
                ? new String[] { "root", parts[0] }
                : parts;
    }
    
    private static JSONObject load() throws IOException, JSONException
    {
        JSONObject json;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(FILE)))
        {
            json = new JSONObject(new JSONTokener(bis));
        }
        return json;
    }
    
    public static final void store()
    {
        try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(FILE)))
        {
            PROPS.write(osw, 4, 0);
        }
        catch(IOException ex) { ex.printStackTrace(System.err); }
    }
    
    private static JSONObject prop(String[] path)
    {
        if(path.length == 1)
            return props();
        return prop(props(), path, 0, false);
    }
    
    private static JSONObject prop(JSONObject base, String[] path, int index, boolean store)
    {
        JSONObject child = base.optJSONObject(path[index]);
        if(child == null)
        {
            child = new JSONObject();
            base.put(path[index], child);
            store = true;
        }
        if(index + 2 >= path.length)
        {
            if(store)
                store();
            return child;
        }
        return prop(child, path, index + 1, store);
    }
    
    private static void set(String[] path, Object value) { prop(path).put(path[path.length - 1], value); }
    private static void set(String[] path, Collection<?> value) { prop(path).put(path[path.length - 1], value); }
    private static void set(String[] path, Map<?, ?> value) { prop(path).put(path[path.length - 1], value); }
    private static <T> T get(String[] path, T defaultValue, Type type)
    {
        JSONObject base = prop(path);
        if(!base.has(path[path.length - 1]))
            return defaultValue;
        T value;
        switch(type)
        {
            default: throw new IllegalStateException();
            case STRING: value = (T) base.getString(path[path.length - 1]); break;
            case INT: value = (T) (Integer) base.getInt(path[path.length - 1]); break;
            case LONG: value = (T) (Long) base.getLong(path[path.length - 1]); break;
            case FLOAT: value = (T) (Float) base.getFloat(path[path.length - 1]); break;
            case DOUBLE: value = (T) (Double) base.getDouble(path[path.length - 1]); break;
            case BOOLEAN: value = (T) (Boolean) base.getBoolean(path[path.length - 1]); break;
            case LIST: {
                JSONArray temp = base.getJSONArray(path[path.length - 1]);
                value = (T) temp.toList();
            } break;
            case MAP: {
                JSONObject temp = base.getJSONObject(path[path.length - 1]);
                value = (T) temp.toMap();
            } break;
            case VECTOR2: {
                JSONArray temp = base.getJSONArray(path[path.length - 1]);
                if(temp.length() < 2)
                    return defaultValue;
                value = (T) new Vector2(temp.optDouble(0), temp.optDouble(1));
            } break;
            case COLOR: {
                JSONArray temp = base.getJSONArray(path[path.length - 1]);
                if(temp.length() < 3)
                    return defaultValue;
                if(temp.length() == 3)
                    value = (T) new Color(temp.optInt(0), temp.optInt(1), temp.optInt(2));
                else value = (T) new Color(temp.optInt(0), temp.optInt(1), temp.optInt(2), temp.optInt(3));
            } break;
        }
        return value == null ? defaultValue : value;
    }
    private static <T> T get(String name, T defaultValue, Type type) { return get(splitPath(name), defaultValue, type); }
    
    
    public static final String getString(String name, String defaultValue) { return get(name, defaultValue, Type.STRING); }
    public static final String getString(String name) { return get(name, "", Type.STRING); }
    
    public static final boolean getBoolean(String name, boolean defaultValue) { return get(name, defaultValue, Type.BOOLEAN); }
    public static final boolean getBoolean(String name) { return get(name, false, Type.BOOLEAN); }
    
    public static final int getInt(String name, int defaultValue) { return get(name, defaultValue, Type.INT); }
    public static final int getInt(String name) { return get(name, 0, Type.INT); }
    
    public static final long getLong(String name, long defaultValue) { return get(name, defaultValue, Type.LONG); }
    public static final long getLong(String name) { return get(name, 0L, Type.LONG); }
    
    public static final float getFloat(String name, float defaultValue) { return get(name, defaultValue, Type.FLOAT); }
    public static final float getFloat(String name) { return get(name, 0f, Type.FLOAT); }
    
    public static final double getDouble(String name, double defaultValue) { return get(name, defaultValue, Type.DOUBLE); }
    public static final double getDouble(String name) { return get(name, 0D, Type.DOUBLE); }
    
    public static final List<Object> getList(String name, List<Object> defaultValue) { return get(name, defaultValue, Type.LIST); }
    public static final List<Object> getList(String name) { return get(name, Collections.emptyList(), Type.LIST); }
    
    public static final Map<String, Object> getMap(String name, Map<String, Object> defaultValue) { return get(name, defaultValue, Type.MAP); }
    public static final Map<String, Object> getMap(String name) { return get(name, Collections.emptyMap(), Type.MAP); }
    
    public static final Vector2 getVector2(String name, Vector2 defaultValue) { return get(name, defaultValue, Type.VECTOR2); }
    public static final Vector2 getVector2(String name) { return get(name, new Vector2(), Type.VECTOR2); }
    
    public static final Color getColor(String name, Color defaultValue) { return get(name, defaultValue, Type.COLOR); }
    public static final Color getColor(String name) { return get(name, Color.BLACK, Type.COLOR); }
    
    public static final List<String> getStringList(String name)
    {
        return get(name, Collections.emptyList(), Type.LIST)
                .stream().map(o -> o.toString()).collect(Collectors.toList());
    }
    
    
    private static void set0(String name, Object value)
    {
        set(splitPath(name), value);
        store();
    }
    
    public static final void set(String name, String value) { set0(name, value == null ? "" : value); }
    public static final void set(String name, int value) { set0(name, value); }
    public static final void set(String name, long value) { set0(name, value); }
    public static final void set(String name, float value) { set0(name, value); }
    public static final void set(String name, double value) { set0(name, value); }
    public static final void set(String name, boolean value) { set0(name, value); }
    public static final void set(String name, List<?> value)
    {
        set(splitPath(name), value == null ? Collections.emptyList() : value);
        store();
    }
    public static final void set(String name, Map<?, ?> value)
    {
        set(splitPath(name), value == null ? Collections.emptyMap() : value);
        store();
    }
    public static final void set(String name, Vector2 value)
    {
        if(value == null)
            set0(name, new JSONArray(new double[] { 0, 0 }));
        else set0(name, new JSONArray(new double[] { value.x, value.y }));
    }
    public static final void set(String name, Color value)
    {
        if(value == null)
            set0(name, new JSONArray(new int[] { 0, 0, 0 }));
        else
        {
            if(value.getAlpha() > 0)
                set0(name, new JSONArray(new int[] { value.getRed(), value.getGreen(), value.getBlue() }));
            else set0(name, new JSONArray(new int[] { value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha() }));
        }
    }
    
    public static final void setAll(Consumer<ConfigBlockSetter> action)
    {
        action.accept(BLOCK);
        store();
    }
    
    
    public static final class ConfigBlockSetter
    {
        private ConfigBlockSetter() {}
        
        private void set0(String name, Object value) { Config.set(splitPath(name), value); }
        
        public final void set(String name, String value) { set0(name, value == null ? "" : value); }
        public final void set(String name, int value) { set0(name, value); }
        public final void set(String name, long value) { set0(name, value); }
        public final void set(String name, float value) { set0(name, value); }
        public final void set(String name, double value) { set0(name, value); }
        public final void set(String name, boolean value) { set0(name, value); }
        public final void set(String name, List<?> value)
        {
            Config.set(splitPath(name), value == null ? Collections.emptyList() : value);
        }
        public final void set(String name, Map<?, ?> value)
        {
            Config.set(splitPath(name), value == null ? Collections.emptyMap() : value);
        }
        public final void set(String name, Vector2 value)
        {
            if(value == null)
                set0(name, new JSONArray(new double[] { 0, 0 }));
            else set0(name, new JSONArray(new double[] { value.x, value.y }));
        }
        public final void set(String name, Color value)
        {
            if(value == null)
                set0(name, new JSONArray(new int[] { 0, 0, 0 }));
            else
            {
                if(value.getAlpha() > 0)
                    set0(name, new JSONArray(new int[] { value.getRed(), value.getGreen(), value.getBlue() }));
                else set0(name, new JSONArray(new int[] { value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha() }));
            }
        }
    }
    private static final ConfigBlockSetter BLOCK = new ConfigBlockSetter();
    
    private enum Type { STRING, INT, LONG, FLOAT, DOUBLE, BOOLEAN, LIST, MAP, VECTOR2, COLOR }
}
