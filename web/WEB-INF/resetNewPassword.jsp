<%-- 
    Document   : resetNewPassword
    Created on : Nov 21, 2017, 12:22:09 PM
    Author     : 725899
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Password</title>
    </head>
    <body>
        <h1>Enter a new password</h1>
        <form action="resetPassword" method="post"/>
         new password: <input type="password" name="password" value=""/>
            <input type="submit" name="submit" value="Submit"/>
            <input type="hidden" name="action" value="newPassword"/>
            <input type="hidden" name="uuid" value="${uuid}"/>
        </form>
    </body>
</html>
