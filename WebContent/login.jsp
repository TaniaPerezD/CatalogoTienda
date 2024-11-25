<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body style="background-color: #FAE0DC;">

	<%@ include file="header.jsp"%>

	<%
	String message = request.getParameter("message");
	%>
	<div class="container">
		<div class="row"
			style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">
			<form action="./LoginSrv" method="post"
				class="col-md-4 col-md-offset-4 col-sm-8 col-sm-offset-2"
				style="border: 2px; border-radius: 10px; background-color: #F8BBAF; padding: 10px;">
				<div style="font-weight: bold;" class="text-center">
					<h2 style="color: black;">Iniciar sesion</h2>
					<%
					if (message != null) {
					%>
					<p style="color: black;">
						<%=message%>
					</p>
					<%
					}
					%>
				</div>
				<div></div>
				<div class="row">
					<div class="col-md-12 form-group">
						<label for="last_name">Correo</label> <input type="email"
							placeholder="Correo" name="username" class="form-control"
							id="last_name" required>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 form-group">
						<label for="last_name">Clave de acceso</label> <input type="password"
							placeholder="Clave de acceso" name="password" class="form-control"
							id="last_name" required>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 form-group">
						<label for="userrole">Rol</label> <select name="usertype"
							id="userrole" class="form-control" required>
							<option value="customer" selected>Cliente</option>
							<option value="admin">Admin</option>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 text-center">
						<button type="submit" class="btn btn-success">Iniciar sesi�n</button>
					</div>
				</div>
			</form>

		</div>
	</div>

	<%@ include file="footer.html"%>

</body>
</html>