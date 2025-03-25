package com.mechanical.workshops.utils;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class EmailUtil {

    private static final String API_KEY = "re_AvxmVqYh_PzgxACHcZ8m4FuEjZ19EuBGj"; // Reemplaza con tu API Key real
    private static final String TEMPLATE_PATH = "src/main/resources/templates/"; // Ruta en resources
    private static final String FROM_EMAIL = "Acme <onboarding@resend.dev>";

    private static Resend resend = new Resend(API_KEY);

    /**
     * Envía un correo electrónico con una plantilla HTML.
     * @param toEmail Destinatario del correo
     * @param subject Asunto del correo
     * @param parameters parametros de la plantilla
     */
    public static void sendEmail(String toEmail, String subject, String template, Map<String, String> parameters) {
        try {

            String htmlContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH + template + ".html")));
            String personalizedHtml = replaceTemplateVariables(htmlContent, parameters);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(toEmail)
                    .subject(subject)
                    .html(personalizedHtml)
                    .build();

            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Correo enviado con éxito. ID: " + data.getId());
        } catch (IOException | ResendException e) {
            e.printStackTrace();
        }
    }

    private static String replaceTemplateVariables(String template, Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
