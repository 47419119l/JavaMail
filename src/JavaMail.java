
import java.io.IOException;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;

/**
 * Created by 47419119l on 2016/3/3.
 */
public class JavaMail {

   private static Session session;
    static Properties properties;
    static String user;
    static String pass;
    /**
     * Metode que s'exeucta al iniciar la clase.
     * @param args
     */
    public static void main(String[] args) {
       Scanner teclat = new Scanner(System.in);
        int menu=0;
        System.out.println("El correo ha de estar iniciat al buscador");
        System.out.println("El teu mail : ");
        user = teclat.next();
        System.out.println("Contrasenya :");
        pass = teclat.next();
        configuracióMail(user, pass);
        do {
           System.out.println("Que vols fer 1. enviar un correo 2.Veure els teus missatges 3. Sortir");
            menu = teclat.nextInt();
            switch (menu){
                case 1:
                    System.out.println("Destinatari:");
                    String dest = teclat.next();
                    System.out.println("Asumpte :");
                    String asump = teclat.next();
                    System.out.println("Missatge :");
                    String miss = teclat.nextLine();
                    System.out.println("Aquí pots veure els teus missatges :");
                    enviarMissatge(user, dest, asump, miss);
                    break;
                case 2 :
                    lleguirmissatges();
                    break;
                case 3:
                    System.out.println("FI");
            }

       }while(menu!=3);
    }

    /**
     * Metoe per lleguir correos.
     */
    public static void lleguirmissatges(){
       try {
           Session emailSession = Session.getDefaultInstance(properties);
           Store store = emailSession.getStore("pop3s");
           store.connect("pop.gmail.com", user, pass);
           Folder carpeta = store.getFolder("INBOX");
           carpeta.open(Folder.READ_ONLY);
           Message[] messages = carpeta.getMessages();
           System.out.println("Tens "+messages.length+" missatges");
           for(int y =0; y<3;y++){
               System.out.println("Emisor : "+messages[y].getFrom()+" Asumpte : "+messages[y].getSubject()+"\n "+messages[y].getContent().toString());
           }

       }catch (NoSuchProviderException e) {
           System.out.println("Hi ha un error");
       } catch (MessagingException e) {
           System.out.println("Hi ha un error");
       } catch (IOException e) {
           System.out.println("Hi ha un error");
       }catch (SecurityException e){
           System.out.println("Hi ha un problema de Seguretat.");
       }

    }
    /**
     * Metode que permet configurar el missatg que enviarem
     * @param user
     * @param dest
     * @param asump
     * @param miss
     */
    public static void enviarMissatge(String user,String dest,String asump,String miss){
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(dest));
            message.setSubject(asump);
            message.setText(miss);

            Transport.send(message);

            System.out.println("Missatge enviat");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metode que configura el servidorMail.
     * @param user
     * @param pass
     */
    public static void configuracióMail(final String user, final String pass){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,pass);

                    }
                });
    }
}
