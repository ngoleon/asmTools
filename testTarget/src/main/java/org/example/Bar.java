package org.example;
public class Bar {
    public int add(int a, int b) {
        return a + b;
    }

    public void throwSomething() {
        throw new RuntimeException("Boom!");
    }
}
