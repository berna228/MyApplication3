package com.example.app;

public class SparkCoreData {
    public String cmd;
    public String name;
    public String result;
    public Info coreinfo;

    public static class Info{
        public String last_app;
        public String last_heard;
        public String connected;
        public String device_id;
    }

}