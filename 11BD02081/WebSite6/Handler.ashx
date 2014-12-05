<%@ WebHandler Language="C#" Class="Handler" %>

using System;
using System.Web;
using System.Messaging;
using System.Threading;

public class Handler : IHttpHandler {
    String path = ".\\private$\\chat";
    MessageQueue queue;
    Int64 lstmsg;
    bool msgExists;
    
    public void ProcessRequest (HttpContext context) {
        context.Response.ContentType = "text/event-stream";
        HttpResponse Response = context.Response;
        int i = 0;
        while (i < 10)
        {
            if (Response != null)
            {
                Response.Write(String.Format("data: {0}\n\n", i));
                Response.Flush();
            }
            i++;
            Thread.Sleep(1000);
        }
        Response.Close();
        //context.Response.Close();
        /*try
        {
            if (!MessageQueue.Exists(path))
            {
                MessageQueue.Create(path);
            }
            queue = new MessageQueue(path);
            if (queue.GetAllMessages().Length == 0) msgExists = false;
            else msgExists = true;
            if (msgExists)
            {
                Message[] msgs = queue.GetAllMessages();
                lstmsg = msgs[queue.GetAllMessages().LongLength - 1].LookupId;
                Message msge = queue.PeekByLookupId(lstmsg + 1);
                msge.Formatter = new XmlMessageFormatter(new Type[] { typeof(ChatMessage) });
                ChatMessage ms = new ChatMessage();
                ms = (ChatMessage)msge.Body;
                context.Response.Write(ms.sender + ": " + ms.content);
                lstmsg = msge.LookupId;
                //context.Response.Write("Hello World");
            }
            else{
                Message msge = queue.Peek();
                msge.Formatter = new XmlMessageFormatter(new Type[]{typeof(ChatMessage)});
                ChatMessage ms = new ChatMessage();
                ms = (ChatMessage)msge.Body;
                context.Response.Write(ms.sender+": "+ms.content);
                lstmsg = msge.LookupId;
            }
                
        }
        catch (Exception ex)
        {

        }*/
    }
    
    public bool IsReusable {
        get {
            return false;
        }
    }

}