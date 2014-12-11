package com.whollyframework.util.sequence;

/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:20:31
 */
public class SequenceException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SequenceException(){
		super();
	}

	public SequenceException(String msg){
		super(msg);
	}
	
	public SequenceException(Throwable cause){
		super(cause);
	}
	
	public SequenceException(String msg,Throwable cause){
		super(msg,cause);
	}
}
