package com.whollyframework.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.whollyframework.protobuf.model.EnumTypeProtos;
import com.whollyframework.protobuf.model.InnerMessageProtos;
import com.whollyframework.protobuf.model.MessageProtos;
import com.whollyframework.protobuf.pojo.EnumType;
import com.whollyframework.protobuf.pojo.InnerMessagePojo;
import com.whollyframework.protobuf.pojo.MessagePojo;
import com.whollyframework.util.json.CustomFilteringIntrospector;
import com.whollyframework.util.json.CustomNullKeySerializer;
import com.whollyframework.util.json.StringUnicodeSerializer;

public class ProctoBufTest {

	final static DecimalFormat integerFormat = new DecimalFormat("########");
	
	public static void main(String[] args) {
        final int testCount = 1000 * 500;        
        final MessageProtos.Message protoObj = getProtobufBean();
        final MessagePojo pojoOBj = getPojoBean();

        // Serializable测试
        testTemplate(new TestCallback() {

            public String getName() {
                return "Serializable Test";
            }

            @Override
            public byte[] writeObject(Object source) {
                try {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream output = new ObjectOutputStream(bout);
                    output.writeObject(source);
                    return bout.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object readObject(byte[] bytes) {
                try {
                    ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
                    ObjectInputStream input = new ObjectInputStream(bin);
                    return input.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, pojoOBj, testCount);

        // protobuf测试
        testTemplate(new TestCallback() {

            public String getName() {
                return "protobuf test";
            }

            @Override
            public byte[] writeObject(Object source) {
                if (source instanceof MessagePojo) {
                	MessageProtos.Message.Builder messageBuilder = MessageProtos.Message.newBuilder();
                	MessageProtos.Message message = messageBuilder.buildMessage((MessagePojo) source);
                    return message.toByteArray();
                }

                return null;
            }

            @Override
            public Object readObject(byte[] bytes) {
                try {
                    return MessageProtos.Message.parseFrom(bytes);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, pojoOBj, testCount);

        // json测试
        final ObjectMapper objectMapper = new ObjectMapper();
        final JavaType javaType = TypeFactory.defaultInstance().constructType(pojoOBj.getClass());

        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		//使Jackson JSON支持Unicode编码非ASCII字符  
	    SimpleModule module = new SimpleModule();  
	    module.addSerializer(String.class, new StringUnicodeSerializer());  
	    objectMapper.registerModule(module);  
	    
        // JSON configuration not to serialize null field
	    //设置null值不参与序列化(字段不被显示)  
	    objectMapper.setSerializationInclusion(Include.NON_NULL);  

        // JSON configuration not to throw exception on empty bean class
	    objectMapper.getSerializerProvider().setNullKeySerializer(new CustomNullKeySerializer());

        // JSON configuration for compatibility
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        testTemplate(new TestCallback() {

            public String getName() {
                return "Jackson Test";
            }

            @Override
            public byte[] writeObject(Object source) {
                try {
                    return objectMapper.writeValueAsBytes(source);
                } catch (JsonGenerationException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public Object readObject(byte[] bytes) {
                try {
                    return objectMapper.readValue(bytes, 0, bytes.length, javaType);
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, pojoOBj, testCount);

        // Xstream测试
//        final XStream xstream = new XStream();
//        testTemplate(new TestCallback() {
//
//            public String getName() {
//                return "Xstream test";
//            }
//
//            @Override
//            public byte[] writeObject(Object source) {
//                return xstream.toXML(source).getBytes();
//            }
//
//            @Override
//            public Object readObject(byte[] bytes) {
//                return xstream.fromXML(new ByteArrayInputStream(bytes));
//            }
//        }, pojoOBj, testCount);
	}
	private static MessageProtos.Message getProtobufBean() {
	      MessageProtos.Message.Builder messageBuilder = MessageProtos.Message.newBuilder();

	      messageBuilder.setStrObj("message");
	      messageBuilder.setFolatObj(1f);
	      messageBuilder.addDoubleObj(1d);
	      messageBuilder.addDoubleObj(2d);
	      messageBuilder.setBoolObj(true);

	      messageBuilder.setBytesObj(ByteString.copyFrom(new byte[] { 1, 2, 3 }));
	      messageBuilder.setInt32Obj(32);
	      messageBuilder.setInt64Obj(64l);
	      messageBuilder.setSint32Obj(232);
	      messageBuilder.setSint64Obj(264);
	      messageBuilder.setFixed32Obj(532);
	      messageBuilder.setFixed64Obj(564);
	      messageBuilder.setSfixed32Obj(2532);
	      messageBuilder.setSfixed64Obj(2564);
	      messageBuilder.setUint32Obj(632);
	      messageBuilder.setUint64Obj(664);

	      InnerMessageProtos.InnerMessage.Builder innerMessageBuilder = InnerMessageProtos.InnerMessage.newBuilder();
	      innerMessageBuilder.setId(1);
	      innerMessageBuilder.setName("inner");
	      innerMessageBuilder.setType(EnumTypeProtos.EnumType.PRODUCTS);

	      messageBuilder.setInnerMessage(innerMessageBuilder);

	      return messageBuilder.build();
	  }
	
	private static MessagePojo getPojoBean() {
        MessagePojo bean = new MessagePojo();

        bean.setStrObj("message");
        bean.setFolatObj(1f);
        List<Double> doubleObj = new ArrayList<Double>();
        doubleObj.add(1d);
        doubleObj.add(2d);
        bean.setDoubleObj(doubleObj);
        bean.setBoolObj(true);

        bean.setBytesObj(new byte[] { 1, 2, 3 });
        bean.setInt32Obj(32);
        bean.setInt64Obj(64l);
        bean.setSint32Obj(232);
        bean.setSint64Obj(264);
        bean.setFixed32Obj(532);
        bean.setFixed64Obj(564);
        bean.setSfixed32Obj(2532);
        bean.setSfixed64Obj(2564);
        bean.setUint32Obj(632);
        bean.setUint64Obj(664);

        InnerMessagePojo innerMessagePojo = new InnerMessagePojo();
        innerMessagePojo.setId(1);
        innerMessagePojo.setName("inner");
        innerMessagePojo.setType(EnumType.PRODUCTS);

        bean.setInnerMessage(innerMessagePojo);

        return bean;
    }
	
	private static void restoreJvm() {
        int maxRestoreJvmLoops = 10;
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < maxRestoreJvmLoops; i++) {
            System.runFinalization();
            System.gc();

            long memUsedNow = memoryUsed();
            // break early if have no more finalization and get constant mem used
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                && (memUsedNow >= memUsedPrev)) {
                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }
    }

    private static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
	
	private static void testTemplate(TestCallback callback, Object source, int count) {
        int warmup = 10;
        // 先进行预热，加载一些类，避免影响测试
        for (int i = 0; i < warmup; i++) {
            byte[] bytes = callback.writeObject(source);
            callback.readObject(bytes);
        }
        restoreJvm(); // 进行GC回收
        // 进行测试
        long start = System.nanoTime();
        long size = 0l;
        for (int i = 0; i < count; i++) {
            byte[] bytes = callback.writeObject(source);
            size = size + bytes.length;
            callback.readObject(bytes);
            // System.out.println(callback.readObject(bytes));
            bytes = null;
        }
        long nscost = (System.nanoTime() - start);
        System.out.println(callback.getName() + "total objects="+count+" total cost=" + integerFormat.format(nscost) + "ns , each cost="
                           + integerFormat.format(nscost / count) + "ns , and byte sizes = " + size / count);
        restoreJvm();// 进行GC回收

    }
}
