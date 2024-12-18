<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.shashi.service.impl.*, com.shashi.service.*,com.shashi.beans.*,java.util.*,javax.servlet.ServletOutputStream,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Ver productos</title>
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
	String userName = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");
	String userType = (String) session.getAttribute("usertype");

	if (userType == null || !userType.equals("admin")) {

		response.sendRedirect("login.jsp?message=acceso denegado!!");

	}

	else if (userName == null || password == null) {

		response.sendRedirect("login.jsp?message=sesion expirada, inicie sesion de nuevo!");

	}
	ProductServiceImpl prodDao = new ProductServiceImpl();
	List<ProductBean> products = new ArrayList<ProductBean>();

	String search = request.getParameter("search");
	String type = request.getParameter("type");
	String message = "Todos los productos";
	if (search != null) {
		products = prodDao.searchAllProducts(search);
		message = "Mostrando resultados de '" + search + "'";
	} else if (type != null) {
		products = prodDao.getAllProductsByType(type);
		message = "Mostrando resultados de '" + type + "'";
	} else {
		products = prodDao.getAllProducts();
	}
	if (products.isEmpty()) {
		message = "No hay productos para '" + (search != null ? search : type) + "'";
		products = prodDao.getAllProducts();
	}
	%>



	<jsp:include page="header.jsp" />

	<div class="text-center"
		style="color: black; font-size: 14px; font-weight: bold;"><%=message%></div>
	<!-- Start of Product Items List -->
	<div class="container" style="background-color:  #FAE0DC;;">
		<div class="row text-center">

			<%
			for (ProductBean product : products) {
			%>
			<div class="col-sm-4" style='height: 350px;'>
				<div class="thumbnail">
					<img src="./ShowImage?pid=<%=product.getProdId()%>" alt="Product"
						style="height: 150px; max-width: 180px;">
					<p class="productname"><%=product.getProdName()%>
						(
						<%=product.getProdId()%>
						)
					</p>
					<p class="productinfo"><%=product.getProdInfo()%></p>
					<p class="price">
						<%
						double originalPrice = product.getProdPrice();
						double discount = product.getDescuento(); // Porcentaje de descuento
						double discountedPrice = originalPrice * (1 - discount / 100.0);
						if (discount > 0) {
						%>
						<span style="text-decoration: line-through; color: grey;">Bs <%=originalPrice%></span>
						<span style="color: green; font-weight: bold;">Bs <%=String.format("%.2f", discountedPrice)%></span>
						<%
						} else {
						%>
						<span>Bs <%=originalPrice%></span>
						<%
						}
						%>
					</p>
					<p class="price">
					    <%
					    if (discount > 0) {
					    %>
					    Descuento: <%=discount%> %
					    <%
					    }
					    %>
					</p>
					<form method="post">
						<button type="submit"
							formaction="./RemoveProductSrv?prodid=<%=product.getProdId()%>"
							class="btn btn-danger">Quitar producto</button>
						&nbsp;&nbsp;&nbsp;
						<button type="submit"
							formaction="updateProduct.jsp?prodid=<%=product.getProdId()%>"
							class="btn btn-primary">Actualizar producto</button>
					</form>
				</div>
			</div>

			<%
			}
			%>

		</div>
	</div>
	<!-- ENd of Product Items List -->

	<%@ include file="footer.html"%>

</body>
</html>