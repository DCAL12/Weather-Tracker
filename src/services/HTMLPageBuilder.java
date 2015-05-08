package services;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLPageBuilder {

    static final String TEMPLATE_HTML_PATH = "/views/template.html";
    private String responseHTML;

    public HTMLPageBuilder(ServletContext servletContext, String pageHTMLPath) throws IOException {

        // Set the site template
        Path siteTemplatePath = Paths.get(servletContext.getRealPath(TEMPLATE_HTML_PATH));
        responseHTML = new String(Files.readAllBytes(siteTemplatePath), StandardCharsets.UTF_8);

        // Get the page's main content
        Path realPageHTMLPath = Paths.get(servletContext.getRealPath(pageHTMLPath));
        String mainHTML = new String(Files.readAllBytes(realPageHTMLPath), StandardCharsets.UTF_8);

        setContent("{{main}}", mainHTML);
    }

    public void setContent(String replaceWhat, String replaceWith) {
        responseHTML = responseHTML.replace(replaceWhat, replaceWith);
    }

    @Override
    public String toString() {
        return responseHTML;
    }
}
