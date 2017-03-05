package com.example.nickpham.checkittodo.GAS;

/**
 * Created by nickpham on 30/12/2016.
 */

public class GAS_WorkInFormation
{

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    String Id;


    String Name, Location, Note, Priority, Alarm, Type, Complete, CurrentTimeWeek, SoundAlarm;

    public String getSoundAlarm() {
        return SoundAlarm;
    }

    public void setSoundAlarm(String soundAlarm) {
        SoundAlarm = soundAlarm;
    }

    public String getCurrentTimeWeek() {
        return CurrentTimeWeek;
    }

    public void setCurrentTimeWeek(String currentTimeWeek) {
        CurrentTimeWeek = currentTimeWeek;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getComplete() {
        return Complete;
    }

    public void setComplete(String complete) {
        Complete = complete;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getAlarm() {
        return Alarm;
    }

    public void setAlarm(String alarm) {
        Alarm = alarm;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
