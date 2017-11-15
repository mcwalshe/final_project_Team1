package org.speed_reader.data;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
	
	public static String hashString(String str){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
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
			passwordHash = hashString(password);
			return true; // Password accepted and recorded as hash.
		} else {
			return false; // Password rejected.
		}
	}
	
	public boolean checkPassword(String password){
		if(hashString(password).equals(passwordHash)){
			return true;
		} else {
			return false;
		}
	}
	
	public int getCurrWPM(){
		return currWPM;
	}
	
	public void setCurrWPM(int currWPM){
		this.currWPM = currWPM;
	}
	
	public int getRecordTrainingSec(){
		return recordTrainingSec;
	}
	
	public void setRecordTrainingSec(int recordTrainingSec){
		this.recordTrainingSec = recordTrainingSec;
	}
	
	public int getRecordWPM(){
		return recordWPM;
	}
	
	public void setRecordWPM(int recordWPM){
		this.recordWPM = recordWPM;
	}
	
}
