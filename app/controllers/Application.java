package controllers;

import com.sun.tools.javac.util.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import util.PropertiesChanger;
import java.io.InputStream;

import play.*;
import play.mvc.*;
import play.mvc.Http;
import java.util.Map;
import java.util.logging.Level;
import java.util.Enumeration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.commons.io.IOUtils;
import static play.mvc.Controller.request;
import static play.mvc.Controller.session;
import static play.mvc.Results.ok;


import views.html.*;

public class Application extends Controller {

    public static final String MESSAGES_FILENAME = "messages";

    //static Properties props = new Properties();
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result aloha() {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String content = values.get("content")[0];
        final String contentId = values.get("contentId")[0];
        final String pageId = values.get("pageId")[0];
        final String templateFilename = session("aloha.filename." + pageId);
        final String fullTemplateFilename = Play.application().path() + "/app/views/" + templateFilename;

        System.out.println("pageId:" + pageId);
        System.out.println("contentId:" + contentId);
        System.out.println("Content:" + content);

        System.out.println("Project Root:" + Play.application().path());
        System.out.println("Template Filename:" + templateFilename);
        System.out.println("Template FullFilename:" + fullTemplateFilename);


        if (contentId.startsWith("message.")) {
            // save as message
            saveMessages(contentId.substring(8), content);
        } else {
            // save in Template
            if (templateFilename != null) {
                saveToTemplate(fullTemplateFilename, pageId, contentId, content);
            }
        }

        return ok();
    }

    public static Result saveDeveloperFile() {

        String[] url = request().headers().get("x-origurl");
        String[] fpath = request().headers().get("x-filepath");

        System.out.println("url: " + url);
        System.out.println("fpath:" + fpath);
        return ok();
    }

    public static void replaceStringInFile(String myPattern, String myReplacement, String myFileName) throws IOException {
        FileInputStream fis = new FileInputStream(myFileName);
        String content = IOUtils.toString(fis, Charset.defaultCharset());
        content = content.replaceAll(myPattern, myReplacement);
        IOUtils.write(content, new FileOutputStream(myFileName), Charset.defaultCharset());
    }

    public static boolean isAlohaOn() {
        return Play.application().configuration().getBoolean("play-aloha.admin");
    }

    static void saveToTemplate(final String fullTemplateFilename, final String pageId, final String contentId, final String content) {
        try {
            File input = new File(fullTemplateFilename);
            Document doc = Jsoup.parse(input, "UTF-8", request().host() + pageId);

            String selector = "#" + contentId;
            Element part = doc.select(selector).first();

            if (part != null) {
                Element newPart = part.clone();
                newPart.html(content);
                replaceStringInFile(part.toString(), newPart.toString(), input.getPath());
            }
        } catch (IOException io) {
            System.out.println("errors: " + io.getLocalizedMessage());
        }
    }

    static void saveMessages(String _key, String _newValue) {
        InputStream is = null;

        
        // First try loading from the current directory
        try {
            File f = new File(MESSAGES_FILENAME);
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }
        
        // Try loading from classpath
        if (is == null) {
            is = Application.class.getResourceAsStream("/" + MESSAGES_FILENAME);
        }

        try {
            
            // Try loading properties from the file (if found)
            PropertiesChanger props = new PropertiesChanger();
            props.load(is);            
            props.setProperty(_key, _newValue);
            props.save(new FileOutputStream(Play.application().path() + "/conf/" + MESSAGES_FILENAME));
        } catch (IOException ex) {
            System.out.println("Problem saving: " + ex.toString());
        }

    }
}
