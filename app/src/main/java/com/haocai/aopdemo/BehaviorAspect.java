package com.haocai.aopdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.text.SimpleDateFormat;

/**
 * Created by liuxiaobing on 2018/08/01.
 */
@Aspect
public class BehaviorAspect {
    private static final String TAG = "MainAspect";
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 测试切点，execution ：函数执行内部
     *
     */
    @Pointcut("execution(@com.haocai.aopdemo.BehaviorTrace * *(..)) && @annotation(behaviorTrace)")
    public void annoBehavior(BehaviorTrace behaviorTrace) throws Throwable{

    }

    @Around("annoBehavior(behaviorTrace)")
    public Object dealPoint(ProceedingJoinPoint point,BehaviorTrace behaviorTrace) throws Throwable{

        Object result = null;
        result = point.proceed();
        System.out.println("34--------value："+behaviorTrace.value());
        return result;
    }

    /**
     * 测试方法调用时，触发
     *
     * @Around 不能和 @Before、@After 一起使用，如果不小心这样用了，你会发现没有任何效果。
     * @Around 会替换原先执行的代码，但如果你仍然希望执行原先的代码，可以使用joinPoint.proceed()。
     */
    @Pointcut("call(* com.haocai.aopdemo.MainActivity.testCallMethod(..))")
    public void callMethod() {}

    @Before("callMethod()")
    public void beforeMethodCall(JoinPoint joinPoint) {
        System.out.println("45--------beforeMethodCall："+joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
    }

    /**
     * 测试 属性修改值
     * 测试例子：我们希望不管怎么修改age的值，最后获取的age都为100，那么就需要替换访问age的代码：
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("get(int com.haocai.aopdemo.MainActivity.age)")
    public int aroundFieldGet(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行原代码
        Object obj = joinPoint.proceed();
        int age = Integer.parseInt(obj.toString());
        System.out.println("59--------before age:"+age);
        return 100;
    }


    /**
     * 测试 获取代码中的 函数返回值，getHeight(..) 括号中的 ..表示任意类型
     * 如果 想限定 getHeight()括号中的形参类型，可以使用如下方式
     * 方式一：Pointcut 中表示任意参数的 .. 改为 int 即下述例子改为：
     * @AfterReturning(pointcut = "execution(* com.haocai.aopdemo.MainActivity.getHeight(int))", returning = "height")
     *
     * 方式二：
     * @AfterReturning(pointcut = "execution(* com.haocai.aopdemo.MainActivity.getHeight(..)) && args(int)", returning = "height")
     * @param height
     */
    @AfterReturning(pointcut = "execution(* com.haocai.aopdemo.MainActivity.getHeight(..))", returning = "height")
    public void getHeight(int height) {
        System.out.println("59--------height:"+height);
    }


    @Around("execution(@com.haocai.aopdemo.PermissonAnnotation * *(..)) && @annotation(permisson)")
    public void checkPermisson(final ProceedingJoinPoint joinPoint, PermissonAnnotation permisson) throws Throwable {

        String permissonStr = permisson.value();
        Activity mainActivity = (Activity) joinPoint.getThis();
        int check = ContextCompat.checkSelfPermission(mainActivity, permissonStr);
        if (check != PackageManager.PERMISSION_GRANTED) {
            System.out.println("96-------------未授权:"+permissonStr +" ：程序停止");
        }else{
            System.out.println("102-------------已授权:"+permissonStr +" ：程序将继续执行");
        }
    }


}
