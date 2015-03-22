<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>APPLICATION LOGIN</title>
</head>

<body>
<div class="main">
    <div class="outerBox">
        <div class="box">
            <%--<form id="signForm" action="/api/user/login" method="POST">
                <p><label>Login: <input type='text' style="height: 25px" autocomplete="off" name='username'></label>
                </p>

                <p><label>Mot de passe: <input type='password' style="height: 25px" name='password'></label></p>

                <p><input name="login" value="Login" type="submit"></p>
            </form>--%>
            <div align="center">
                <form id="casForm" name="casForm" action="/api/signin/corporate" method="POST" >
                    <p><input name="login" value="Connexion via Portail" type="submit"></p>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
