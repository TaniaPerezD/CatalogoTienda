<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.shashi.service.impl.*, com.shashi.service.*,com.shashi.beans.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Actualizar producto</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet" href="css/changes.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body style="background-color:  #FAE0DC;;">
	<%
	/* Checking the user credentials */
	String utype = (String) session.getAttribute("usertype");
	String uname = (String) session.getAttribute("username");
	String pwd = (String) session.getAttribute("password");
	String prodid = request.getParameter("prodid");
	ProductBean product = new ProductServiceImpl().getProductDetails(prodid);
	if (prodid == null || product == null) {
		response.sendRedirect("updateProductById.jsp?message=Please Enter a valid product Id");
		return;
	} else if (utype == null || !utype.equals("admin")) {
		response.sendRedirect("login.jsp?message=acceso denegado, Login as admin!!");
		return;
	} else if (uname == null || pwd == null) {
		response.sendRedirect("login.jsp?message=sesion expirada, inicie sesion de nuevo!");
		return;
	}
	%>

	<jsp:include page="header.jsp" />

	<%
	String message = request.getParameter("message");
	%>
	<div class="container">
		<div class="row"
			style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">
			<form action="./UpdateProductSrv" method="post"
				class="col-md-6 col-md-offset-3"
				style="border: 2px; border-radius: 10px; background-color: #FFE5CC; padding: 10px;">
				<div style="font-weight: bold;" class="text-center">
					<div class="form-group">
						<img src="./ShowImage?pid=<%=product.getProdId()%>"
							alt="Product Image" height="100px" />
						<h2 style="color: black;">Actualizar producto</h2>
					</div>

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
				<div class="row">
					<input type="hidden" name="pid" class="form-control"
						value="<%=product.getProdId()%>" id="last_name" required>
				</div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Nombre</label> <input type="text"
							placeholder="Nombre" name="name" class="form-control"
							value="<%=product.getProdName()%>" id="last_name" required>
					</div>
					<div class="col-md-6 form-group">
						<%
						String ptype = product.getProdType();
						%>
						<label for="producttype">Categoria </label> <select name="type"
							id="producttype" class="form-control" required>
							<option value="carne"
								<%="mobile".equalsIgnoreCase(ptype) ? "selected" : ""%>>Carne</option>
							<option value="frutas"
								<%="tv".equalsIgnoreCase(ptype) ? "selected" : ""%>>Frutas</option>
							<option value="verduras"
								<%="camera".equalsIgnoreCase(ptype) ? "selected" : ""%>>Verduras</option>
							<option value="panes"
								<%="laptop".equalsIgnoreCase(ptype) ? "selected" : ""%>>Panes</option>
							<option value="lacteos"
								<%="tablet".equalsIgnoreCase(ptype) ? "selected" : ""%>>Lacteos</option>
							<option value="otra"
								<%="other".equalsIgnoreCase(ptype) ? "selected" : ""%>>Otra</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="last_name">Descripcion</label>
					<textarea name="info" class="form-control text-align-left"
						id="last_name" required><%=product.getProdInfo()%></textarea>
				</div>
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="last_name">Precio</label> <input type="number"
							value="<%=product.getProdPrice()%>"
							placeholder="Precio" name="price" class="form-control"
							id="last_name" required>
					</div>
					<div class="col-md-4 form-group">
						<label for="last_name">Stock</label> <input type="number"
							value="<%=product.getProdQuantity()%>"
							placeholder="Stock " class="form-control"
							id="last_name" name="quantity" required>
					</div>
					<div class="col-md-4 form-group">
						<label for="last_name">Descuento</label> <input type="number"
							value="<%=product.getDescuento()%>"
							placeholder="Descuento " class="form-control"
							id="last_name" name="descuento" required>
					</div>
				</div>
				<div class="row text-center">
					<div class="col-md-6" style="margin-bottom: 2px;">
						<button formaction="adminViewProduct.jsp" class="btn btn-danger">Cancelar</button>
					</div>
					<div class="col-md-6">
						<button type="submit" class="btn btn-success">Actualizar</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<%@ include file="footer.html"%>
</body>
</html>