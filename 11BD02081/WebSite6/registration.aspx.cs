using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql.Data.MySqlClient;
using System.Data;

public partial class _Default : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Unnamed3_Click(object sender, EventArgs e)
    {
        User newUser = new User();
        newUser.username = new_username.Text;
        newUser.password = new_userpwd.Text;
        if (newUser.isExists())
        {
            msgLabel.Text = "Username already exists, please try another one";
        }
        else
        {
            newUser.insertUser();
            msgLabel.Text = "Registration successfull!";
        }
    }
}