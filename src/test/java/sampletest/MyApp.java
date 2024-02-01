package test.java.sampletest;

import com.testkit.errormanager.GlobalExceptionHandler;

public class MyApp {
    public static void main(String[] args) {
        // Set the custom uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        // Your application logic here
        int result = 10 / 0; // Simulate an unhandled exception
        System.out.println("after exception");
    }
}
