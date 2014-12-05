using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for ChatMessage
/// </summary>
public class ChatMessage
{
	public ChatMessage()
	{
		//
		// TODO: Add constructor logic here
		//
	}
    public ChatMessage(String _sender, String _content, String _room)
    {
        this.content = _content;
        this.sender = _sender;
        this.room = _room;
    }
    public String sender { get; set; }
    public String content { get; set; }
    public String room { get; set; }
}