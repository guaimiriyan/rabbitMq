package com.angus.producer;



import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName angusCLassLoader.java
 * @Description TODO
 * @createTime 2021年02月16日 12:10:00
 */
public class angusCLassLoader extends ClassLoader{

    @Override
    public Class findClass(String name) {
        //1、通过找到该文件进行类的加载
        Class<?> demoJvm = null;
        try (FileInputStream inputStream = new FileInputStream(name);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {

            //读取文件
            int byteValue = 0;
            while ((byteValue = inputStream.read()) != -1) {
                byteArrayOutputStream.write(byteValue);
            }
            //写完之后调用加载
            byte[] bytes = byteArrayOutputStream.toByteArray();
            demoJvm = defineClass("demoJvm", bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return demoJvm;
    }

    public static void main(String[] args) {
              try {
                  angusCLassLoader angusCLassLoader = new angusCLassLoader();
                  Class aClass = angusCLassLoader.findClass("C:\\Users\\Angus_Lee\\IdeaProjects\\rabbitMq\\note\\demoJvm.class");
                  Method methods = aClass.getMethod("main",String[].class);
                  methods.invoke(null,(Object) new String[2]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
                  e.printStackTrace();
              } catch (InvocationTargetException e) {
                  e.printStackTrace();
              }

    }

}
