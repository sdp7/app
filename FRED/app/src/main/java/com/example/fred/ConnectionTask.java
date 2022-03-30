package com.example.fred;

public class ConnectionTask{

    private static String address = "";
    private static int port = 6005;
    public static void setPort(int newPort){
        port = newPort;
    }

    public static void setAddress(String newAddress){
        address = newAddress;
    }

    public static int getPort(){
        if(port != 0){
            return port;
        }
        return -1;
    }

    public static String getAddress(){
        return address;
    }

}
