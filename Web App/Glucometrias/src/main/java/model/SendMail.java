
package model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * send email to recover password
 * @author GTST : Grupo de Tratamiento de Señales y telecomunicaciones
 */
public class SendMail {
    
    /**
     * 
     */
    public SendMail(){
        
    
    }
    
    /**
     * Send email to recover password
     * @param recipient
     * @param password
     * @param user
     */
    public void sendMail(String recipient, String password, String user){
        String sender = "glucometriasgtst@gmail.com";
        String senderPass = "gtst2525";

        
        try
        {
            // Connection's properties
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", sender);
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(recipient));
            message.setSubject("Glucometrías. Envío de contraseña");
            message.setText(
                "Hola "+user+". Tu contraseña para el acceso a la plataforma es: "+password);

            // sent
            Transport t = session.getTransport("smtp");
            t.connect(sender, senderPass);
            t.sendMessage(message, message.getAllRecipients());

            // Close.
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
}
