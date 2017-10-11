package com.softwaretree.jdxandroidimagesexample.model;

public class Person {
    String name;
    byte[] picture;  
    
    public Person() {
    }
    
    public Person(String name, byte[] picture) {
        super();
        this.name = name;
        this.picture = picture;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getPicture() {
        return picture;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
