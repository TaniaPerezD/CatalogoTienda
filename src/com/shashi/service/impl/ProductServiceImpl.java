package com.shashi.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import com.shashi.beans.DemandBean;
import com.shashi.beans.ProductBean;
import com.shashi.beans.UserBean;
import com.shashi.service.ProductService;
import com.shashi.utility.DBUtil;
import com.shashi.utility.IDUtil;
import com.shashi.utility.MailMessage;

public class ProductServiceImpl implements ProductService {

    // HashMap para acceso eficiente por identificador único
    private HashMap<String, ProductBean> productMap = new HashMap<>();

    // LinkedList para almacenamiento detallado de productos
    private List<ProductBean> productList = new LinkedList<>();

    // Constructor: Carga inicial desde la base de datos
    public ProductServiceImpl() {
        loadProductsFromDatabase();
    }

    // Método para cargar productos desde la base de datos
    private void loadProductsFromDatabase() {
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM product");
            rs = ps.executeQuery();

            // Limpiar estructuras en memoria
            productMap.clear();
            productList.clear();

            while (rs.next()) {
                // Crear un ProductBean por cada fila de la base de datos
                ProductBean product = new ProductBean();
                product.setProdId(rs.getString("pid"));
                product.setProdName(rs.getString("pname"));
                product.setProdType(rs.getString("ptype"));
                product.setProdInfo(rs.getString("pinfo"));
                product.setProdPrice(rs.getDouble("pprice"));
                product.setProdQuantity(rs.getInt("pquantity"));
                product.setProdImage(rs.getAsciiStream("image"));
                product.setDescuento(rs.getInt("descuento"));

                // Poblar el HashMap y la LinkedList
                productMap.put(product.getProdId(), product);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }
    }

    @Override
    public String addProduct(String prodName, String prodType, String prodInfo, double prodPrice, int prodQuantity,
            InputStream prodImage, int descuento) {
        String prodId = IDUtil.generateId();
        ProductBean product = new ProductBean(prodId, prodName, prodType, prodInfo, prodPrice, prodQuantity, prodImage, descuento);

        return addProduct(product);
    }

    @Override
    public String addProduct(ProductBean product) {
        String status = "No se pudo registrar el producto!";
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, product.getProdId());
            ps.setString(2, product.getProdName());
            ps.setString(3, product.getProdType());
            ps.setString(4, product.getProdInfo());
            ps.setDouble(5, product.getProdPrice());
            ps.setInt(6, product.getProdQuantity());
            ps.setBlob(7, product.getProdImage());
            ps.setInt(8, product.getDescuento());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = "Producto agregado!";
                loadProductsFromDatabase(); // Sincronizar con memoria
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
        }

        return status;
    }

    @Override
    public String removeProduct(String prodId) {
        // String status = "No se pudo borrar el producto!";
        String status = null;
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("DELETE FROM product WHERE pid = ?");
            ps.setString(1, prodId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = "Producto eliminado!";
                loadProductsFromDatabase(); // Sincronizar con memoria
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
        }

        return status;
    }

    @Override
    public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct) {
        String status = "No se pudo actualizar el producto!";

        if (!prevProduct.getProdId().equals(updatedProduct.getProdId())) {

            status = "No son los mismos productos!";

            return status;
        }
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(
                    "UPDATE product SET pname = ?, ptype = ?, pinfo = ?, pprice = ?, pquantity = ?, image = ?, descuento = ? WHERE pid = ?");
            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setBlob(6, updatedProduct.getProdImage());
            ps.setInt(7, updatedProduct.getDescuento());
            ps.setString(8, prevProduct.getProdId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = "Producto actualizado!";
                loadProductsFromDatabase(); // Sincronizar con memoria
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
        }

        return status;
    }

    @Override
    public String updateProductPrice(String prodId, double updatedPrice) {
        String status = "No se pudo actualizar el precio!";
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("UPDATE product SET pprice = ? WHERE pid = ?");
            ps.setDouble(1, updatedPrice);
            ps.setString(2, prodId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = "Precio actualizado!";
                loadProductsFromDatabase(); // Sincronizar con memoria
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
        }

        return status;
    }
    
    @Override
    public String updateProductDiscount(String prodId, int updatedDiscount) {
        String status = "No se pudo actualizar el descuento!";
        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("UPDATE product SET descuento = ? WHERE pid = ?");
            ps.setInt(1, updatedDiscount);
            ps.setString(2, prodId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = "Descuento actualizado!";
                loadProductsFromDatabase(); // Sincronizar con memoria
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
            } finally {
				DBUtil.closeConnection(con);
				DBUtil.closeConnection(ps);
            }
        return status;
    }


    @Override
    public List<ProductBean> getAllProducts() {
        System.out.println("Productos en linkedList: " + productList);
        return new LinkedList<>(productList);

    }

    @Override
    public List<ProductBean> getAllProductsByType(String type) {
        List<ProductBean> filteredProducts = new LinkedList<>();
        for (ProductBean product : productList) {
            if (product.getProdType().equalsIgnoreCase(type)) {
                filteredProducts.add(product);
            }
        }
        System.out.println("Productos en linkedList al filtrar: " + filteredProducts);
        return filteredProducts;
    }

    @Override
    public List<ProductBean> searchAllProducts(String search) {
        List<ProductBean> searchResults = new LinkedList<>();
        String searchLower = search.toLowerCase();

        for (ProductBean product : productList) {
            if (product.getProdName().toLowerCase().contains(searchLower) ||
                    product.getProdInfo().toLowerCase().contains(searchLower) ||
                    product.getProdType().toLowerCase().contains(searchLower)) {
                searchResults.add(product);
            }
        }
        return searchResults;
    }

    @Override
    public byte[] getImage(String prodId) {
        byte[] image = null;

        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT image FROM product WHERE pid = ?");
            ps.setString(1, prodId);

            rs = ps.executeQuery();

            if (rs.next()) {
                image = rs.getBytes("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return image;
    }

    @Override
    public ProductBean getProductDetails(String prodId) {
        return productMap.get(prodId); // Retorna el producto directamente desde el HashMap
    }

    @Override
    public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct) {

        String status = "No se pudo actualizar el precio!";

        if (!prevProductId.equals(updatedProduct.getProdId())) {

            status = "Los productos son diferentes!";

            return status;
        }

        int prevQuantity = new ProductServiceImpl().getProductQuantity(prevProductId);
        int prevDescuento = new ProductServiceImpl().getProductDiscount(prevProductId);
        Connection con = DBUtil.provideConnection();

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(
                    "UPDATE product SET pname = ?, ptype = ?, pinfo = ?, pprice = ?, pquantity = ?, descuento = ? WHERE pid = ?");
            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setInt(6, updatedProduct.getDescuento());
            ps.setString(7, prevProductId);

            int k = ps.executeUpdate();

            if ((k > 0) && (prevQuantity < updatedProduct.getProdQuantity())) {
                status = "Producto actualizado!";
                loadProductsFromDatabase(); // Sincronizar con memoria

                List<DemandBean> demandList = new DemandServiceImpl().haveDemanded(prevProductId);

                for (DemandBean demand : demandList) {

                    String userFName = new UserServiceImpl().getFName(demand.getUserName());
                    try {
                        MailMessage.productAvailableNow(demand.getUserName(), userFName, updatedProduct.getProdName(),
                                prevProductId);
                    } catch (Exception e) {
                        System.out.println("No se pudo mandar el correo: " + e.getMessage());
                    }
                    boolean flag = new DemandServiceImpl().removeProduct(demand.getUserName(), prevProductId);

                    if (flag)
                        status += " Se mando el correo a los clientes que esperaban el producto!";
                }
            } else if (k > 0)
                status = "Producto actualizado!";
            else
                status = "Producto no disponible";
            
            // if para enviar correo de descuento
			if ((k > 0) && (updatedProduct.getDescuento() > 0) && (prevDescuento != updatedProduct.getDescuento()) ) {
				
				Map<String, UserBean> userMap = new UserServiceImpl().getAllUsers();

			    for (UserBean user : userMap.values()) {
			        try {
			            // Enviar el correo usando la función estática
			            MailMessage.productoConDescuento(user.getEmail(), user.getName(), updatedProduct.getProdName(),
			                    updatedProduct.getProdId(), updatedProduct.getDescuento());
			        } catch (Exception e) {
			            System.out.println("No se pudo mandar el correo a " + user.getEmail() + ": " + e.getMessage());
			        }
			    }
			} else if (k > 0)
                status = "Producto actualizado!";
            else
                status = "Producto no disponible";

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DBUtil.closeConnection(con);
        DBUtil.closeConnection(ps);
        // System.out.println("Prod Update status : "+status);

        return status;
    }

    @Override
    public double getProductPrice(String prodId) {
        ProductBean product = productMap.get(prodId);
        return product != null ? product.getProdPrice() : 0.0;
    }

    @Override
    public boolean sellNProduct(String prodId, int n) {
        boolean flag = false;

        Connection con = DBUtil.provideConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("UPDATE product SET pquantity = (pquantity - ?) WHERE pid = ?");
            ps.setInt(1, n);
            ps.setString(2, prodId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // Actualizar estructuras en memoria
                ProductBean product = productMap.get(prodId);
                if (product != null) {
                    product.setProdQuantity(product.getProdQuantity() - n);
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(con);
            DBUtil.closeConnection(ps);
        }

        return flag;
    }

    @Override
    public int getProductQuantity(String prodId) {
        ProductBean product = productMap.get(prodId);
        return product != null ? product.getProdQuantity() : 0;
    }
    
    @Override
	public int getProductDiscount(String prodId) {
		ProductBean product = productMap.get(prodId);
		return product != null ? product.getDescuento() : 0;
	}
}
