package org.example;

public class TestClass {
    public static void main(String[] args) {
        System.out.println("Hello from TestClass");
        Foo foo = new Foo();
        foo.sayHello();
        Bar bar = new Bar();
        System.out.println("3 + 4 = " + bar.add(3, 4));
    }
}
