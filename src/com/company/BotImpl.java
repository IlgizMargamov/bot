package com.company;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BotImpl implements Bot {

    public BotImpl(){
        //botCommands.put("sayHello", sayHello());
    }

    @Override
    public void getRequest(String request) {
        botCommands.get(request);
    }

    public void sayHelloTo(String name){
        System.out.println("Hello, "+name+"!");
    }

    private final Map<String, Void> botCommands = new HashMap<String, Void>(){};
}
