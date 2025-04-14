# ASM Bytecode Toolkit

A Java developer–friendly toolkit for reading, inspecting, analyzing, and modifying compiled `.class` and `.jar` files using the [ASM bytecode framework](https://asm.ow2.io/). 
Designed as a demonstration of clean bytecode tooling.

---

## Features

- Class/method/field inspection  
- Instruction printer with color-coded terminal output  
- Pattern matchers for opcode sequences  
- Injection helpers (println, timer, constant return)  
- Frame-safe transformations (COMPUTE_FRAMES + custom ClassWriter)  
- JAR loader + writer with non-class resource preservation  
- Variable slot allocator (safe LSTORE/IRETURN injection)  
- Modular, extensible project structure  
- Built entirely in **vanilla Java + ASM**

---

### Requirements

- Java 17+
- Gradle

### Run the demo

```bash
./gradlew run