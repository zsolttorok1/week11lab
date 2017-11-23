<%-- 
    Document   : reset
    Created on : Nov 21, 2017, 12:03:23 PM
    Author     : 725899
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
    </head>
    <body>
        <h1>Reset Password</h1>
        Please enter your email address to reset your password.
        <form action="resetPassword" method="post"/>
        Email address: <input type="text" name="email" value=""/>
            <input type="submit" name="submit" value="Submit"/>
        </form>
        ${errormessage}
    </body>
</html>
