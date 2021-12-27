package core.wefix.lab.configuration.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Component
@SuppressWarnings({"SpellCheckingInspection", "FieldCanBeLocal"})
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class CustomMailSender {

    private final String fromName = "WeFix";
    private final String from = "8419133ac9-0f61af@inbox.mailtrap.io";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JavaMailSender emailSender;
    @Value("src/main/resources/templates/images/69901618385469411.png")
    Resource resourceFile;
    @Value("src/main/resources/templates/recoveryPassword.html")
    private String mailTemplatesPath;

    public void sendMail(String to, String subject, String document, Boolean html, String typeMail){

        try{
            FileTemplateResolver templateResolver = new FileTemplateResolver();
            templateResolver.setPrefix(mailTemplatesPath);
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(to);
            message.setFrom(new InternetAddress(from, fromName));
            message.setSubject(subject);
            message.setText(document, html);
            message.addInline("identifier1234", resourceFile);
            emailSender.send(mimeMessage);
            log.info(String.format("EMAIL (%s) SENDED TO: %s", typeMail, to));
        } catch (Exception ignored) {
            log.error(String.format("ERROR WHILE SENDING EMAIL (%s) TO: %s", typeMail, to));
            throw new RuntimeException("Something went wrong with email");
        }
    }

    public boolean sendResetUser(String to, Map<String, String> data){
        if(!data.containsKey("password")) return false;

        String document = "New password: " + data.get("password");
        String prova = "";
        try {
           prova = Files.readString(Paths.get("src/main/resources/templates/recoveryPassword.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMail(to, "Reset Password",prova, true, "Recovery Password");
        return true;
    }

    public boolean sendResetAdmin(String to, Map<String, String> data){
        if(!data.containsKey("password")) return false;

        String document = "New password: " + data.get("password");

        sendMail(to, "Reset Password", document, false, "Recovery Password");
        return true;
    }

}
