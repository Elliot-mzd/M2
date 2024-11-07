package org.example.Classe;

public class ClassC {
    public void method1() {
        System.out.println("ClassC: method1");
        ClassD d = new ClassD();
        d.method2(); // Appel à method2 de ClassD
    }

    public void method3() {
        System.out.println("ClassC: method3");
    }

    public void method4() {
        System.out.println("ClassC: method4");
    }
}
