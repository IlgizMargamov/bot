package com;

public enum TypeOfCommunication {
    IN_CONSOLE,
    IN_TELEGRAM;

    public static TypeOfCommunication getType(int numberOfCommand){
        switch (numberOfCommand){
            case 1 -> {return IN_CONSOLE;}
            case 2 -> {return IN_TELEGRAM;}
            default -> throw new IllegalArgumentException("Wrong number of command");
        }
    }
}
