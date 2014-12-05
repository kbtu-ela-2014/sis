package Model;

import java.io.Serializable;
import java.util.Date;

public class IMessage implements Serializable{
	private int id;
	User sender;
	Date date;
	private String body;
	private String roomName;
	private int roomID;

	public IMessage(){};

	
	/**
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	/**
	 * @return the roomID
	 */
	public int getRoomID() {
		return roomID;
	}


	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	
}
