package org.example.Classe;

public class ClassD {
    public void method2() {
        System.out.println("ClassD: method2");
    }

    public void method5() {
        System.out.println("ClassD: method5");
        ClassC c = new ClassC();
        c.method1(); // Appel à method1 de ClassC
    }

    public void method6() {
        System.out.println("ClassD: method6");
    }
}
