package org.example.Dump;

public class Dump1 {
    public void method1() {
        Dump2 dump5 = new Dump2();
        dump5.methodA();
        dump5.methodB();
        dump5.methodD();
    }

    public void method2() {
        Dump2 dump2 = new Dump2();
        dump2.methodA();
        dump2.methodC();
    }

    public void method3() {
        Dump2 dump2 = new Dump2();
        dump2.methodB();
        dump2.methodC();
    }
}