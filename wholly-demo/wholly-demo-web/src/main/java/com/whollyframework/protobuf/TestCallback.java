package com.whollyframework.protobuf;

public interface TestCallback {

    String getName();

    byte[] writeObject(Object source);

    Object readObject(byte[] bytes);
}
