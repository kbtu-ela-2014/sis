<%@ Page Language="C#" AutoEventWireup="true" CodeFile="registration.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <style type="text/css">
        .auto-style1 {
            height: 30px;
        }
    </style>
</head>
<body>
    <form id="form1" runat="server">
    <div align="center">
        <table>
            <tr>
                <td>
                    <asp:Label runat="server" Text="Enter your username" />
                </td>
                <td>
                    <asp:TextBox runat="server" id="new_username"/>
                </td>
            </tr>
            <tr>
                <td>
                    <asp:Label Text="Enter your password" runat="server" />
                </td>
                <td>
                    <asp:TextBox runat="server" id="new_userpwd" TextMode="Password"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center" class="auto-style1">
                    <asp:Button Text="Submit" runat="server" OnClick="Unnamed3_Click"/>
                </td>
            </tr>
            <tr>
                <asp:Label runat="server" ID="msgLabel" />
            </tr>
        </table>
    </div>
    </form>
</body>
</html>
