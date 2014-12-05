<%@ Page Language="C#" AutoEventWireup="true" CodeFile="index.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <script type="text/javascript" src="Scripts/jquery-2.1.1.min.js" ></script>
    <script type="text/javascript" src="Scripts/jquery-2.1.1.js" ></script>
    <form id="form1" runat="server">
    <div align="center">
        <asp:DropDownList id="selectRoom" runat="server" OnSelectedIndexChanged="selectRoom_SelectedIndexChanged">
            <asp:ListItem>Room1</asp:ListItem>
            <asp:ListItem>Room2</asp:ListItem>
            <asp:ListItem>Room3</asp:ListItem>
        </asp:DropDownList>
        <br />
        <asp:ListBox runat="server" ID="messages" TextMode="MultiLine" Height="480px" Width="360px" overflow="scroll"></asp:ListBox>
        <br />
        <asp:TextBox runat="server" ID="message" TextMode="MultiLine" Width = "355px" Height="50px" overflow="hidden"></asp:TextBox>
        <br/>
        <asp:Button runat="server" ID="sendMessage" text="Send" OnClick="sendMessage_Click"/>
        <asp:Button runat="server" ID="rcvMessages" Text="Receive Messages" OnClick="rcvMessages_Click" />
        <asp:Label runat="server" ID="alarm" />
        <asp:Label runat="server" ID="alarm2" />
        <asp:Label runat="server" ID="alarm1" />
    </div>
    </form>
</body>
</html>
