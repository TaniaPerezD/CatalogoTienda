package com.shashi.srv;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shashi.beans.DemandBean;
import com.shashi.beans.ProductBean;
import com.shashi.service.impl.CartServiceImpl;
import com.shashi.service.impl.DemandServiceImpl;
import com.shashi.service.impl.ProductServiceImpl;

/**
 * Servlet implementation class UpdateToCart
 */
@WebServlet("/UpdateToCart")
public class UpdateToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateToCart() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");

		if (userName == null || password == null) {

			response.sendRedirect("login.jsp?message=sesion expirada, inicie sesion de nuevo!");
		}

		// login Check Successfull

		String userId = userName;
		String prodId = request.getParameter("pid");
		int pQty = Integer.parseInt(request.getParameter("pqty"));

		CartServiceImpl cart = new CartServiceImpl();

		ProductServiceImpl productDao = new ProductServiceImpl();

		ProductBean product = productDao.getProductDetails(prodId);

		int availableQty = product.getProdQuantity();

		PrintWriter pw = response.getWriter();

		response.setContentType("text/html");

		if (availableQty < pQty) {

			String status = cart.updateProductToCart(userId, prodId, availableQty);

			status = "Solo existen" + availableQty + " de " + product.getProdName()
					+ " Se están agregando" + availableQty
					+ " En tu carrito" + "";

			DemandBean demandBean = new DemandBean(userName, product.getProdId(), pQty - availableQty);

			DemandServiceImpl demand = new DemandServiceImpl();

			boolean flag = demand.addProduct(demandBean);

			if (flag)
				status += "<br/>Te manderemos un mensaje " + product.getProdName()
						+ " cuando esté disponible de nuevo!";
			RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");

			rd.include(request, response);

			pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");

		} else {
			String status = cart.updateProductToCart(userId, prodId, pQty);

			RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");

			rd.include(request, response);

			pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
