package com.sinaye.jrename;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {
    private static Settings settingsInstance = null;
    private HashMap <String, String> settingsData;
    private File settingsFile;

    private Settings(){
        settingsData = new HashMap<>();
        settingsFile = new File("Settings.obj");
        if(settingsFile.exists()){
            deserialize();
        }
    }

    /**
     * @return The Settings instance.
     */
    public static Settings getInstance(){
        if(settingsInstance == null){
            settingsInstance = new Settings();
        }
        return settingsInstance;
    }

    /**
     * Saves or updates data.
     *
     * If a new label is specified, that label will be created however if an existing label is specified, then the value
     * represented by that label will be replaced with the new value.
     *
     * @param label The name of the key.
     * @param data The data you want to save.
     * @return
     */
    public boolean saveData(String label, String data){
        if(settingsData.containsKey(label)){
            settingsData.replace(label, data);
        }else{
            settingsData.put(label, data);
        }
        serialize();
        return true;
    }

    /**
     * Retrieves a value tied to the label or key specified.
     * @param label The name of the key.
     * @return Value
     */
    public String getItem (String label){
        return settingsData.getOrDefault(label, "no label");
    }

    private void serialize(){
        try (FileOutputStream fos = new FileOutputStream(settingsFile); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(settingsData);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deserialize(){
        try (FileInputStream fis = new FileInputStream(settingsFile); ObjectInputStream ois = new ObjectInputStream(fis)) {
            settingsData = (HashMap) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
