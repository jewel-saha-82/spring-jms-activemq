package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/config.xml");
        Student student1 = context.getBean(Student.class);
        System.out.println("Roll = " + student1.getRoll());



    }
}
