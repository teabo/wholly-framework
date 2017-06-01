package com.whollyframework.protobuf.pojo;

import java.io.Serializable;
import java.util.List;

public class MessagePojo implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = -2755858202058471831L;
	String strObj = "hello";
	 int int32Obj;
	 long int64Obj;
	 int uint32Obj;
	 long uint64Obj;
	 int sint32Obj;
	 long sint64Obj;
	 int fixed32Obj;
	 long fixed64Obj;
	 int sfixed32Obj;
	 long sfixed64Obj;
	 boolean 	boolObj;
	 byte[] 	bytesObj;
	 float folatObj;
	 List<Double> doubleObj; //
	 InnerMessagePojo innerMessage;
	public String getStrObj() {
		return strObj;
	}
	public void setStrObj(String strObj) {
		this.strObj = strObj;
	}
	public int getInt32Obj() {
		return int32Obj;
	}
	public void setInt32Obj(int int32Obj) {
		this.int32Obj = int32Obj;
	}
	public long getInt64Obj() {
		return int64Obj;
	}
	public void setInt64Obj(long int64Obj) {
		this.int64Obj = int64Obj;
	}
	public int getUint32Obj() {
		return uint32Obj;
	}
	public void setUint32Obj(int uint32Obj) {
		this.uint32Obj = uint32Obj;
	}
	public long getUint64Obj() {
		return uint64Obj;
	}
	public void setUint64Obj(long uint64Obj) {
		this.uint64Obj = uint64Obj;
	}
	public int getSint32Obj() {
		return sint32Obj;
	}
	public void setSint32Obj(int sint32Obj) {
		this.sint32Obj = sint32Obj;
	}
	public long getSint64Obj() {
		return sint64Obj;
	}
	public void setSint64Obj(long sint64Obj) {
		this.sint64Obj = sint64Obj;
	}
	public int getFixed32Obj() {
		return fixed32Obj;
	}
	public void setFixed32Obj(int fixed32Obj) {
		this.fixed32Obj = fixed32Obj;
	}
	public long getFixed64Obj() {
		return fixed64Obj;
	}
	public void setFixed64Obj(long fixed64Obj) {
		this.fixed64Obj = fixed64Obj;
	}
	public int getSfixed32Obj() {
		return sfixed32Obj;
	}
	public void setSfixed32Obj(int sfixed32Obj) {
		this.sfixed32Obj = sfixed32Obj;
	}
	public long getSfixed64Obj() {
		return sfixed64Obj;
	}
	public void setSfixed64Obj(long sfixed64Obj) {
		this.sfixed64Obj = sfixed64Obj;
	}
	public boolean isBoolObj() {
		return boolObj;
	}
	public void setBoolObj(boolean boolObj) {
		this.boolObj = boolObj;
	}
	public byte[] getBytesObj() {
		return bytesObj;
	}
	public void setBytesObj(byte[] bytesObj) {
		this.bytesObj = bytesObj;
	}
	public float getFolatObj() {
		return folatObj;
	}
	public void setFolatObj(float folatObj) {
		this.folatObj = folatObj;
	}
	public List<Double> getDoubleObj() {
		return doubleObj;
	}
	public void setDoubleObj(List<Double> doubleObj) {
		this.doubleObj = doubleObj;
	}
	public InnerMessagePojo getInnerMessage() {
		return innerMessage;
	}
	public void setInnerMessage(InnerMessagePojo innerMessage) {
		this.innerMessage = innerMessage;
	}
	 
	 
}
