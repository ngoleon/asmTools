package org.example;

public class Foo {
    public void sayHello() {
        System.out.println("Hello from Foo");
    }

    public void nestedCall() {
        System.out.println("Nested call");
        sayHello();
    }
}
