package com.whollyframework.util.template;

import junit.framework.TestCase;

public class TemplateContextTest extends TestCase {

	/*
	 * Test method for 'com.whollyframework.util.template.TemplateContext.parse(String)'
	 */
	public void testParse() {
		String text = "${key} = helloworld";
		TemplateContext context = TemplateContext.parse(text);
		context.putParams("key","helloworld");
		assertEquals(context.toText(), "helloworld = helloworld");		
		text = "$$${key} = helloworld;${key} = helloworld;${key} = helloworld$$";
		context = TemplateContext.parse(text);
		context.putParams("key","helloworld");
		assertEquals(context.toText(), "$$helloworld = helloworld;helloworld = helloworld;helloworld = helloworld$$");

	}

}
