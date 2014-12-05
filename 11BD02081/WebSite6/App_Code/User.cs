using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using MySql.Data.MySqlClient;
using System.Data;
/// <summary>
/// Summary description for User
/// </summary>
public class User
{
	public User()
	{     
	}
    public User(String _username, String _password)
    {
        this.username = _username;
        this.password = _password;
    }
    protected String id;
    public String username { get; set; }
    public String password { get; set; }
    public Boolean isValid()
    {
        MySqlConnection con = new MySqlConnection("database=chat_app; server=localhost; uid=root");
        con.Open();
        MySqlCommand cmd = new MySqlCommand("SELECT username, password FROM users WHERE username='" + username + "' AND password='" + password + "'", con);
        MySqlDataAdapter adp = new MySqlDataAdapter(cmd);
        DataTable dt = new DataTable();
        adp.Fill(dt);
        if (dt.Rows.Count == 0)
        {
            con.Close();
            return false;
        }
        else
        {
            DataRow row = dt.Rows[0];
            id = row[0].ToString();
            return true;
        }
    }
    public String getID()
    {
        return this.id;
    }
    public Boolean isExists()
    {
        MySqlConnection con = new MySqlConnection("database=chat_app; server=localhost; uid=root; pwd=");
        con.Open();
        String query = "SELECT username FROM users WHERE username='" + username + "'";
        MySqlDataAdapter adp = new MySqlDataAdapter(query, con);
        DataTable dt = new DataTable();
        adp.Fill(dt);
        if (dt.Rows.Count == 0)
        {
            con.Close();
            return false;
        }
        else
        {
            con.Close();
            return true;
        }
    }
    public void insertUser()
    {
        MySqlConnection con = new MySqlConnection("database=chat_app; server=localhost; uid=root; pwd=");
        con.Open();
        String query = "INSERT INTO users(username, password) VALUES('"+username+"','"+password+"')";
        MySqlCommand com = new MySqlCommand(query, con);
        com.ExecuteNonQuery();
        con.Close();
    }
}