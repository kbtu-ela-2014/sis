using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql.Data.MySqlClient;
using System.Data;
using System.Web.Security;
using System.Messaging;
using System.Threading;
public partial class _Default : System.Web.UI.Page
{
    String path = ".\\private$\\" + HttpContext.Current.User.Identity.Name;
    MessageQueue queue;
    Int64 lstmsg, lstmsg1, lstmsg2;
    List<String> r1 = new List<string>();
    List<String> r2 = new List<string>();
    List<String> r3 = new List<string>();
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            try
            {
                if (!MessageQueue.Exists(path))
                {
                    MessageQueue.Create(path);
                }
                queue = new MessageQueue(path);
                queue.Formatter = new XmlMessageFormatter(new Type[] { typeof(string) });
                //lstmsg = queue.GetAllMessages().Last().LookupId;
            }
            catch (Exception ex)
            {

            }
        }
    }

    protected void sendMessage_Click(object sender, EventArgs e)
    {
        try
        {
            path = ".\\private$\\chat";
            if (!MessageQueue.Exists(path))
            {
                MessageQueue.Create(path);
            }
            queue = new MessageQueue(path);
            ChatMessage msg = new ChatMessage(HttpContext.Current.User.Identity.Name, message.Text, selectRoom.SelectedItem.Text);
            queue.Send(msg);
        }
        catch (Exception ex)
        {

        }
    }
    protected void rcvMessages_Click(object sender, EventArgs e)
    {
        try
        {
            path = ".\\private$\\" + HttpContext.Current.User.Identity.Name;
            if (!MessageQueue.Exists(path))
            {
                MessageQueue.Create(path);
            }
            queue = new MessageQueue(path);
            queue.Formatter = new XmlMessageFormatter(new Type[]{typeof(ChatMessage)});
            alarm.Text = queue.GetAllMessages().Length.ToString();
            if (queue.GetAllMessages().Length != 0)
            {
                receiveMsgs();
                if (selectRoom.SelectedItem.Text == "Room1") messages.DataSource = r1;
                else if (selectRoom.SelectedItem.Text == "Room2") messages.DataSource = r2;
                else messages.DataSource = r3;
                messages.DataBind();
            }
        }
        catch(Exception ex)
        {

        }
    }
    protected void selectRoom_SelectedIndexChanged(object sender, EventArgs e)
    {
    }
    void receiveMsgs()
    {
        path = ".\\private$\\" + HttpContext.Current.User.Identity.Name;
        if (!MessageQueue.Exists(path))
        {
            MessageQueue.Create(path);
        }
        queue = new MessageQueue(path);
        queue.Formatter = new XmlMessageFormatter(new Type[] { typeof(ChatMessage) });
        Message msg = queue.Receive();
        ChatMessage cm = (ChatMessage)msg.Body;
        if (cm.room == "Room1") r1.Add(cm.sender + ": " + cm.content);
        else if (cm.room == "Room2") r2.Add(cm.sender + ": " + cm.content);
        else r3.Add(cm.sender + ": " + cm.content);
        if (queue.GetAllMessages().Length != 0) receiveMsgs();
    }
}