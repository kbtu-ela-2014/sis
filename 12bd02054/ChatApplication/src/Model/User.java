package Model;

import java.io.Serializable;
import java.util.Vector;

public class User implements Serializable, Comparable<User>{
	private String firstName;
	private String lastName;
	private int id;
	private String login;
	private int password;
	//Vector<Chat> chats;
	
	public User(){};
	public User(String fName, String lName, String login, String password){
		lastName = lName;
		firstName = fName;
		this.login = login;
		this.password = password.hashCode();
	};
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setId(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the password
	 */
	public int getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(int password) {
		this.password = password;
	}
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		//if(id==null && other.id==null) return true;
		return other.login.equals(this.login);
	}

	public int compareTo(User p) {
		return login.compareTo(p.getLogin());
	}
	public String toString(){
		return getFirstName()+" "+getLastName();
	}
	
}
