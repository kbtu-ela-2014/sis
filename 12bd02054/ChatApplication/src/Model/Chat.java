package Model;

import java.util.Vector;

public class Chat {
	private String nameOfChat;
	Vector<IMessage> history;
	

	/**
	 * @return the nameOfChat
	 */
	public String getNameOfChat() {
		return nameOfChat;
	}

	/**
	 * @param nameOfChat the nameOfChat to set
	 */
	public void setNameOfChat(String nameOfChat) {
		this.nameOfChat = nameOfChat;
	}

}
