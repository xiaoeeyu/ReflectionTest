package com.xiaoeryu.reflectiontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xiaoeryu.reflectiontest.databinding.ActivityMainBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'reflectiontest' library on application startup.
    static {
        System.loadLibrary("reflectiontest");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        // testField();
        testMethod();
    }

    public void testField() {
        // 创建几个新的实例来调用构造函数
        Test a_obj = new Test();
        Test b_obj = new Test("test");
        Test c_obj = new Test("test", 2);

        // 通过loadClass获取Class
        try {
            Class testClazz = MainActivity.class.getClassLoader().loadClass("com.xiaoeryu.reflectiontest.Test");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 通过Class.forName()也可以获取Class
        try {
            Class testClazz2 = Class.forName("com.xiaoeryu.reflectiontest.Test");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Class testClazz3 = Test.class;
        // 获取单个Field信息public
        try {
            Field publicStaticField_field = testClazz3.getDeclaredField("publicStaticField");
            Log.i("xiaoeryu", "getDeclaredFields->" + publicStaticField_field);
            // 通过Field获取属性
            String content = (String) publicStaticField_field.get(null);    // 对于staticField来说是属于类的而不是实例，所以可以直接调用，不传入参数也可以
            Log.i("xiaoeryu", "publicStaticField_field-> " + content);
            content = (String) publicStaticField_field.get(a_obj);          // 传入参数也可
            Log.i("xiaoeryu", "publicStaticField_field->a_obj " + content);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 获取单个域信息private
        try {
            Field priviteStaticField_field = testClazz3.getDeclaredField("priviteStaticField");
            Log.i("xiaoeryu", "getFields++->" + priviteStaticField_field);
            // 通过域获取属性
            priviteStaticField_field.setAccessible(true);   // 通过取消权限检查来修改访问限制
            priviteStaticField_field.set(null, "modified"); // 对于非静态属性可以调用set()修改对象属性
            String content = (String) priviteStaticField_field.get(null);
            Log.i("xiaoeryu", "priviteStaticField_field-> " + content);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 遍历所有域信息通过getDeclaredFields
        Field[] fields = testClazz3.getDeclaredFields();
        for (Field i : fields) {
            Log.i("xiaoeryu", "getDeclaredFields->" + i);
        }
        // 遍历所有域信息
        Field[] fields_1 = testClazz3.getFields();
        for (Field i : fields) {
            Log.i("xiaoeryu", "getFields->" + i);
        }

    }
    public void testMethod() {
        Class testClazz = Test.class;
        Method publicStaticFunc_Method = null;
        try {
            publicStaticFunc_Method = testClazz.getDeclaredMethod("publicStaticFunc");
            // public native Object invoke(Object obj, Object... args)
            publicStaticFunc_Method.invoke("null");     // 同样如果是静态方法可以直接调用，不需要传入对象，另外这个方法没有参数可以省略
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Log.i("xiaoeryu", "getDeclaredMethod-> " + publicStaticFunc_Method);

        Method PrivateFunc_Method = null;
        try {
            PrivateFunc_Method = testClazz.getDeclaredMethod("privateStaticFunc");
            PrivateFunc_Method.setAccessible(true);     // 同样的，调用私有函数也需要修访问限制
            PrivateFunc_Method.invoke(null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Log.i("xiaoeryu", "getDeclaredMethod-> " + PrivateFunc_Method);
        Method[] methods = testClazz.getDeclaredMethods();      // getDeclaredMethods()可以获取到类中的公有和私有函数，所以一般调用类中的函数的话使用这个比较多
        for (Method i : methods) {
            Log.i("xiaoeryu", "getDeclaredMethods-> " + i);
        }
        Method[] methods1 = testClazz.getMethods();             // getMethods()只能获取到当前类和继承的父类中的公有函数
        for (Method i : methods1) {
            Log.i("xiaoeryu", "getMethod-> " + i);
        }

        // 遍历构造函数
        Constructor[] constructors = testClazz.getDeclaredConstructors();
        for (Constructor i : constructors){
            Log.i("xiaoeryu", "getDeclaredConstructors-> " + i);
        }
        // 根据传入参数的不同调用不同的构造函数
        try {
            Constructor constructor = testClazz.getDeclaredConstructor(String.class);
            Log.i("xiaoeryu", "getDeclaredConstructor-> " + constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            Constructor constructor = testClazz.getDeclaredConstructor(String.class, int.class);
            // 实例化构造函数并调用
            try {
                // 实例化构造函数对象
                Object testObj = constructor.newInstance("test", 666);
                try {
                    // Field privateField_field = testClazz.getDeclaredField("priviteField");
                    Field privateField_field = testObj.getClass().getDeclaredField("priviteField");
                    privateField_field.setAccessible(true);
                    // 通过实例化对象访问类中的私有属性
                    String privateField_String = (String) privateField_field.get(testObj);
                    Log.i("xiaoeryu", "priviteField-> " + privateField_String);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            Log.i("xiaoeryu", "getDeclaredConstructor-> " + constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A native method that is implemented by the 'reflectiontest' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}