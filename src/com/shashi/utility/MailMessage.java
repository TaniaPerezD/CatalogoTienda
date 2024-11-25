package com.shashi.utility;

import jakarta.mail.MessagingException;

public class MailMessage {
	public static void registrationSuccess(String emailId, String name) {
		String recipient = emailId;
		String subject = "Registro Exitoso";
		String htmlTextMessage = "" + "<html>" + "<body>"
				+ "<h2 style='color:green;'>Bienvenido a Gatobyte</h2>" + ""
				+ "Hola " + name + ","
				+ "<br><br>Gracias por registrarte en Gatobyte.<br>"
				+ "Te invitamos a explorar nuestra última colección de productos electrónicos con descuentos de hasta el 60%."
				+ "<br>Además, como cliente nuevo, tienes un 10% de descuento adicional (hasta 500 Rs) en tu primera compra."
				+ "<br>Usa este código promocional:<br><br><br> CÓDIGO PROMO: " + "ELLISON500<br><br><br>"
				+ "¡Que tengas un buen día!<br>" + "" + "</body>" + "</html>";
		try {
			JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void transactionSuccess(String recipientEmail, String name, String transId, double transAmount) {
		String recipient = recipientEmail;
		String subject = "Pedido Realizado en Ellison Electronics";
		String htmlTextMessage = "<html>" + "  <body>" + "    <p>"
				+ "Hola " + name + ",<br/><br/>"
				+ "Gracias por comprar en Gatobyte.<br/>"
				+ "Tu pedido ha sido realizado exitosamente y está en proceso de envío.<br/><br/>"
				+ "<h6>Nota: Este es un correo de demostración, no has realizado una transacción real.</h6>"
				+ "<br/>Detalles de tu transacción:<br/>"
				+ "<br/><b>Id de Pedido:</b> <font style=\"color:green;\">" + transId + "</font><br/>"
				+ "<b>Monto Pagado:</b> <font style=\"color:green;\">" + transAmount + "</font>"
				+ "<br/><br/>¡Gracias por tu compra!<br/><br/>"
				+ "¡Te esperamos de nuevo! <br/><br/><b>Gatobyte.</b>"
				+ "    </p>" + "  </body>" + "</html>";

		try {
			JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void orderShipped(String recipientEmail, String name, String transId, double transAmount) {
		String recipient = recipientEmail;
		String subject = "¡Tu Pedido Ha Sido Enviado!";
		String htmlTextMessage = "<html>" + "  <body>" + "    <p>"
				+ "Hola " + name + ",<br/><br/>"
				+ "Tu pedido ha sido enviado y está en camino.<br/><br/>"
				+ "<h6>Nota: Este es un correo de demostración, no has realizado una transacción real.</h6>"
				+ "<br/>Detalles del pedido:<br/>"
				+ "<b>Id de Pedido:</b> <font style=\"color:green;\">" + transId + "</font><br/>"
				+ "<b>Monto Pagado:</b> <font style=\"color:green;\">" + transAmount + "</font>"
				+ "<br/><br/>¡Gracias por comprar con nosotros!<br/><br/>"
				+ "¡Te esperamos de nuevo! <br/><br/><b>Gatobyte.</b>"
				+ "    </p>" + "  </body>" + "</html>";

		try {
			JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void productAvailableNow(String recipientEmail, String name, String prodName, String prodId) {
		String recipient = recipientEmail;
		String subject = "Producto " + prodName + " Disponible en Gatobyte";
		String htmlTextMessage = "<html>" + "  <body>" + "    <p>"
				+ "Hola " + name + ",<br/><br/>"
				+ "El producto <b>" + prodName + "</b> (ID: <b>" + prodId
				+ "</b>) que buscabas ahora está disponible.<br/><br/>"
				+ "<h6>Nota: Este es un correo de demostración, no has realizado una transacción real.</h6>"
				+ "<br/>¡Aprovecha antes de que se agote!<br/><br/>"
				+ "¡Gracias por tu interés!<br/><br/>"
				+ "¡Te esperamos de nuevo!<br/><br/><b>Gatobyte.</b>"
				+ "    </p>" + "  </body>" + "</html>";

		try {
			JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static String sendMessage(String toEmailId, String subject, String htmlTextMessage) {
		try {
			JavaMailUtil.sendMail(toEmailId, subject, htmlTextMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			return "FAILURE";
		}
		return "SUCCESS";
	}
}
