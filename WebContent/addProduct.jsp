<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Agregar producto</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/changes.css">
</head>
<body style="background-color:  #FAE0DC;;">
	<%
	/* Checking the user credentials */
	String userType = (String) session.getAttribute("usertype");
	String userName = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");

	if (userType == null || !userType.equals("admin")) {

		response.sendRedirect("login.jsp?message=acceso denegado, Login as admin!!");

	}

	else if (userName == null || password == null) {

		response.sendRedirect("login.jsp?message=sesion expirada, inicie sesion de nuevo!");

	}
	%>

	<jsp:include page="header.jsp" />

	<%
	String message = request.getParameter("message");
	%>
	<div class="container">
		<div class="row"
			style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">
			<form action="./AddProductSrv" method="post"
				enctype="multipart/form-data" class="col-md-6 col-md-offset-3"
				style="border: 2px; border-radius: 10px; background-color: #FFE5CC; padding: 10px;">
				<div style="font-weight: bold;" class="text-center">
					<h2 style="color: black;">Agregar producto</h2>
					<%
					if (message != null) {
					%>
					<p style="color:black;">
						<%=message%>
					</p>
					<%
					}
					%>
				</div>
				<div></div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Nombre</label> <input type="text"
							placeholder="Nombre" name="name" class="form-control"
							id="last_name" required>
					</div>
					<div class="col-md-6 form-group">
						<label for="producttype">Categoria</label> <select name="type"
							id="producttype" class="form-control" required>
							<option value="carne">Carne</option>
							<option value="tv">Frutas</option>
							<option value="verduras">Verduras</option>
							<option value="panes">Panes</option>
							<option value="lacteos">Lacteos</option>
							<option value="otra">Otra</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="last_name">Descripcion</label>
					<textarea name="info" class="form-control" id="last_name" required></textarea>
				</div>
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="last_name">Precio</label> <input type="number"
							placeholder="Precio" name="price" class="form-control"
							id="last_name" required>
					</div>
					<div class="col-md-4 form-group">
						<label for="last_name">Stock</label> <input type="number"
							placeholder="Stock" name="quantity"
							class="form-control" id="last_name" required>
					</div>
					<div class="col-md-4 form-group">
						<label for="last_name">Descuento</label> <input type="number"
							placeholder="Descuento" name="descuento"
							class="form-control" id="last_name" required>
					</div>
				</div>
				<div>
					<div class="col-md-12 form-group">
						<label for="last_name">Imagen</label> <input type="file"
							placeholder="Seleccionar imagen" name="image" class="form-control"
							id="last_name" required>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 text-center" style="margin-bottom: 2px;">
						<button type="reset" class="btn btn-danger">Limpiar campos</button>
					</div>
					<div class="col-md-6 text-center">
						<button type="submit" class="btn btn-success">Agregar</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<%@ include file="footer.html"%>
</body>
</html>