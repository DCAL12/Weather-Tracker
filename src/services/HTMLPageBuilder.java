package services;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLPageBuilder {

    private final String siteTemplate = "/views/template.html";
    private final Charset charset = StandardCharsets.UTF_8;
    private ServletContext servletContext;
    private String responseHTML;

    public HTMLPageBuilder(ServletContext servletContext, String pageHTMLPath) throws IOException {
        this.servletContext = servletContext;

        responseHTML = readTemplate(siteTemplate);
        setContent("{{main}}", readTemplate(pageHTMLPath));
    }

    public String readTemplate(String pathToTemplate) throws IOException {
        Path realTemplatePath = Paths.get(servletContext.getRealPath(pathToTemplate));
        return new String(Files.readAllBytes(realTemplatePath), charset);
    }

    public void setContent(String replaceWhat, String replaceWith) {
        responseHTML = responseHTML.replace(replaceWhat, replaceWith);
    }

    @Override
    public String toString() {
        return responseHTML;
    }
}
