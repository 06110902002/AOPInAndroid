
package com.haocai.aopdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private int age;

    public int getAge() {
        return this.age;
    }

    /**--------------------在aspject中测试获取代码中的函数返回值-----------------*/
    public int getHeight() {
        return 0;
    }

    public int getHeight(int sex) {
        switch (sex) {
            case 0:
                return 163;
            case 1:
                return 173;
        }
        return 173;
    }
    /**--------------------end-----------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.age = 10;
        setContentView(R.layout.activity_main);
    }


    @BehaviorTrace(value = "摇一摇", type = 1)
    public void mShake(View view) {

    }


    //@BehaviorTrace(value = "语音:", type = 2)
    public void callMethod(View view) {
        testCallMethod();
    }


    public void testGet(View view) {
        System.out.println("39---------测试 aspectJ 中 修改属性方法，修改后的age："+age);
    }


    private void testCallMethod(){
        System.out.println("38---------测试 aspectJ 中 call方法，这里是待测试的方法");

    }

    public void afterReturn(View view){
        getHeight(23);
    }


    @PermissonAnnotation(value = Manifest.permission.CAMERA)
    public void permisson(View view){
        System.out.println("72---------如果这段代码执行了，代表权限通过");

    }


}
