using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql.Data.MySqlClient;
using System.Data;
using System.Web.Security;
public partial class _Default : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Unnamed5_Click(object sender, EventArgs e)
    {
        User usr = new User();
        usr.username = logName.Text;
        usr.password = logPwd.Text;
        if (usr.isValid())
        {
            FormsAuthentication.RedirectFromLoginPage(usr.username, true);
        }
    }
}