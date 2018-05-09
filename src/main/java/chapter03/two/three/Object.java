package chapter03.two.three;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

public class Object {
    public static void main(String[] args) {
        System.out.println(new Object().getClass().getResource("").getPath());
//        System.out.println(new java.lang.Object().getClass().getResource("").getPath());
        System.out.println(new java.lang.Object().getClass().getResource(""));

        System.out.println(new ClassLoader().getResource(""));
    }
}
