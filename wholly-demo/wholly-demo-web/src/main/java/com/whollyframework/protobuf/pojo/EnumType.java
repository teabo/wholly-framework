package com.whollyframework.protobuf.pojo;

public enum EnumType {

	UNIVERSAL(0),WEB(1),IMAGES(2),LOCAL(3),NEWS(4),PRODUCTS(5),VIDEO(6);
	
	int value;
	
	EnumType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
