package org.speed_reader.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
	
	private String name;
	private ArrayList<Document> docList;
	private String passwordHash;
	private Pointer myPointer;
	private int currWPM;
	private int recordTrainingSec;
	private int recordWPM;
	
	public User(){
		this(null);
	}
	
	public User(String name){
		this.name = name;
		docList = new ArrayList<Document>();
		passwordHash = null;
		myPointer = null;
		currWPM = 0;
		recordTrainingSec = 0;
		recordWPM = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public ArrayList<Document> getDocs(){
		return docList;
	}
	
	public Document getDoc(int idx){
		return docList.get(idx);
	}
	
	public void addDoc(Document doc){
		docList.add(doc);
	}
	
	public void removeDoc(Document doc){
		docList.remove(doc);
	}
	
	public void removeDoc(int idx){
		docList.remove(idx);
	}
	
	public Pointer getPointer(){
		return myPointer;
	}
	
	public void setPointer(Pointer pointer){
		myPointer = pointer;
	}
	
	public void recordTrainingSecs(int secs){
		recordTrainingSec = secs;
	}
	
	public void recordWPM(int wpm){
		recordWPM = wpm;
	}
	
	public boolean hasPassword(){
		return passwordHash != null;
	}
	
	private boolean validatePassword(String password){
		// TODO: Check password against a regular expression.
		return true;
	}
	
	public static String hashString(String str) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(str.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = md.digest();
		char[] hash = new char[bytes.length];
		for(int i = 1; i < bytes.length; i += 1){
			hash[i] = (char)(bytes[i] & 0xFF);
		}
		return new String(hash);
	}
	
	public boolean setPassword(String password){
		if(validatePassword(password)){
			try {
				passwordHash = hashString(password);
			} catch(NoSuchAlgorithmException e){
				e.printStackTrace();
				return false; // Exception in hashing function.
			}
			return true; // Password accepted and recorded as hash.
		} else {
			return false; // Password rejected.
		}
	}
}
