package edu.handong.analysis.utils;

public class NotEnoughArgumentException extends Exception{
	public NotEnoughArgumentException(){
		super("No CLI argument Exception! Please put a file path");
	}
	NotEnoughArgumentException(String message){
		super(message);
	}

}
