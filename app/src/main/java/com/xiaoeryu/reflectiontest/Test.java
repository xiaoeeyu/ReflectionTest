package com.xiaoeryu.reflectiontest;

import android.util.Log;

public class Test {
    public String flag = null;
    public Test(){
        flag = "Test()";
    }
    public Test(String arg){
        flag = "Test(String arg)";
    }
    public Test(String arg, int arg2){
        flag = "Test(String arg, int arg2)";
    }

    public static String publicStaticField = "i am a public StaticField";
    public String publicField = "i am a public Field";

    private static String priviteStaticField = "i am a private StaticField";
    private String priviteField = "i am a private Field";

    public static void publicStaticFunc(){
        Log.i("xiaoeryu", "i am from publicStaticFunc");
    }
    public void publicNonStaticFunc(){
        Log.i("xiaoeryu", "i am from publicNonStaticFunc");
    }
    private static void privateStaticFunc(){
        Log.i("xiaoeryu", "i am from privateStaticFunc");
    }
    private void privateNonStaticFunc(){
        Log.i("xiaoeryu", "i am from privateNonStaticFunc");
    }

}
