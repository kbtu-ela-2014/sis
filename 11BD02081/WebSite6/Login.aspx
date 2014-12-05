<%@ Page Language="C#" AutoEventWireup="true" CodeFile="login.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link rel="stylesheet" type="text/css" href="StyleSheet.css" />
</head>
<body>
    <form id="form1" runat="server">
    <div align="center" id="loginBody">
        <table>
            <tr>
                <td>
                    <asp:Label runat="server" Text="Login" />
                </td>
        
                <td>
                    <asp:TextBox id="logName" runat="server" />
                </td>
            </tr>
            <tr>
                <td>
                    <asp:Label runat="server" Text="Password" />
                </td>
                <td>
                    <asp:TextBox id="logPwd" runat="server" TextMode="Password" />
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <asp:Button runat="server" text="Submit" OnClick="Unnamed5_Click"/>
                </td>
            </tr>
        </table>
        <span>
            If you are not registered, please <a href="registration.aspx">register</a>.
        </span>
    </div>
    </form>
</body>
</html>
