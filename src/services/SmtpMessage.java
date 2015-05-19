package services;

/*
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Adapted from 'smtpsend' by
 * @author Max Spivak
 * @author Bill Shannon
 *
 * Available at http://java.net/projects/javamail/downloads/download/javamail-samples.zip
 */

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;



public class SmtpMessage {

    private static final String PROTOCOL = "smtps";
    private static final String MAIL_HOST = "smtp.mail.yahoo.com";
    private static final String USERNAME = "weather.tracker@yahoo.com";
    private static final String PASSWORD = "qazxswedcvfr";
    private static final boolean AUTHENTICATION_REQUIRED = true;
    private static final String FROM_EMAIL = "weather.tracker@yahoo.com";
    private static final String MAILER = "smtpsend";


    public static void sendEmail(List<String> recipientEmails, String subject, String body) throws MessagingException {

	    /*
	     * Initialize the JavaMail Session.
	     */
	    Properties properties = System.getProperties();
		properties.put("mail." + PROTOCOL + ".host", MAIL_HOST);
	    if (AUTHENTICATION_REQUIRED) {
            properties.put("mail." + PROTOCOL + ".AUTHENTICATION_REQUIRED", "true");
        }

	    /*
	     * Create a Provider representing our extended SMTP transport
	     * and set the property to use our provider.
	     */
//	    Provider provider = new Provider(Provider.Type.TRANSPORT, PROTOCOL,
//		"smtpsend$SMTPExtension", "JavaMail", "v1.5.3");
//	    properties.put("mail." + PROTOCOL + ".class", "smtpsend$SMTPExtension");

	    // Get a Session object
	    Session session = Session.getInstance(properties, null);

	    // Register our extended SMTP transport.
//        session.addProvider(provider);

	    /*
	     * Construct the message and send it.
	     */
	    Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM_EMAIL));

            recipientEmails.forEach(recipient -> {
                try {
                    message.addRecipient(
                            Message.RecipientType.TO, new InternetAddress(recipient));
                } catch (MessagingException m) {
                    System.out.println("services.SmtpMessage.sendEmail() Add Recipient Error" + m.getMessage());
                }
            });

            message.setSubject(subject);
            message.setText(body);
            message.setHeader("X-Mailer", MAILER);
            message.setSentDate(new Date());
        } catch (MessagingException m) {
            System.out.println("services.SmtpMessage.sendEmail() Message Error" + m.getMessage());
        }

        // Send message the simple way
//	    Transport.send(message);

        /*Send using some SMTP-specific features requiring
	    managing the Transport object explicitly.*/
        SMTPTransport transport = null;

	    try {
            transport = (SMTPTransport)session.getTransport(PROTOCOL);

            if (AUTHENTICATION_REQUIRED) {
                try {
                    transport.connect(MAIL_HOST, USERNAME, PASSWORD);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            else {
                transport.connect();
            }
            transport.sendMessage(message, message.getAllRecipients());
	    } catch (SendFailedException s) {
            System.out.println("services.SmtpMessage.sendEmail() Send Error" + s.getMessage());
        } catch (NoSuchProviderException p) {
            System.out.println("services.SmtpMessage.sendEmail() Provider Error" + p.getMessage());
        } catch (MessagingException m) {
            System.out.println("services.SmtpMessage.sendEmail() Message Error" + m.getMessage());
        } finally {
            if (transport != null) {
                transport.close();
            }
	    }

	    System.out.println("\nMail was sent successfully.");
	}
}
