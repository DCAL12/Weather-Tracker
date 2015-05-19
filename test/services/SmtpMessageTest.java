package services;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SmtpMessageTest {

    @Test
    public void testSendEmail() throws Exception {
        List<String> recipients = new ArrayList<>();
        recipients.add("dcaltemp15@hmamail.com");
        recipients.add("douglas.callaway@my.jcu.edu.au");
        SmtpMessage.sendEmail(recipients, "test", "this is a test email");
        assertTrue(true);
    }
}