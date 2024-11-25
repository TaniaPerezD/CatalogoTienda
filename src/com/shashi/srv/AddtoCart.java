package com.shashi.srv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

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
 * Servlet implementation class AddtoCart
 */
@WebServlet("/AddtoCart")
public class AddtoCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Estructura de datos de tipo cola para almacenar productos del carrito
    private Queue<ProductBean> cartQueue = new LinkedList<>();

    public AddtoCart() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        String usertype = (String) session.getAttribute("usertype");
        if (userName == null || password == null || usertype == null || !usertype.equalsIgnoreCase("customer")) {
            response.sendRedirect("login.jsp?message=sesion expirada, vuelve a Iniciar sesion!");
            return;
        }

        // login Check Successfull

        String userId = userName;
        String prodId = request.getParameter("pid");
        int pQty = Integer.parseInt(request.getParameter("pqty")); // 1

        CartServiceImpl cart = new CartServiceImpl();
        ProductServiceImpl productDao = new ProductServiceImpl();
        ProductBean product = productDao.getProductDetails(prodId);

        int availableQty = product.getProdQuantity();
        int cartQty = cart.getProductCount(userId, prodId);

        pQty += cartQty;

        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        if (pQty == cartQty) {
            String status = cart.removeProductFromCart(userId, prodId);

            // Remover el producto de la cola si es eliminado del carrito
            cartQueue.removeIf(p -> p.getProdId().equals(prodId));

            RequestDispatcher rd = request.getRequestDispatcher("userHome.jsp");
            rd.include(request, response);

            pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
        } else if (availableQty < pQty) {

            String status = null;

            if (availableQty == 0) {
                status = "Producto fuera de stock!";
            } else {
                cart.updateProductToCart(userId, prodId, availableQty);
                status = "Solo existen " + availableQty + " unidades " + product.getProdName()
                        + " en la tienda, estamos poniendo  " + availableQty
                        + " en tu carrito.";
            }

            // Crear una demanda si no hay suficiente stock
            DemandBean demandBean = new DemandBean(userName, product.getProdId(), pQty - availableQty);
            DemandServiceImpl demand = new DemandServiceImpl();
            boolean flag = demand.addProduct(demandBean);

            if (flag)
                status += "<br/>Te mandaremos un correo cuando " + product.getProdName()
                        + " este disponible de nuevo!";

            // Agregar el producto disponible a la cola
            if (availableQty > 0) {
                product.setProdQuantity(availableQty);
                cartQueue.add(product);
            }

            RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");
            rd.include(request, response);

            pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
        } else {
            String status = cart.updateProductToCart(userId, prodId, pQty);

            // Agregar el producto al carrito y también a la cola
            product.setProdQuantity(pQty);
            cartQueue.add(product);

            RequestDispatcher rd = request.getRequestDispatcher("userHome.jsp");
            rd.include(request, response);

            pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }

    // Método adicional para mostrar los productos en la cola (opcional)
    public Queue<ProductBean> getCartQueue() {
        return cartQueue;
    }
}