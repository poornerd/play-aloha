package controllers;

import com.sun.tools.javac.util.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import play.*;
import play.mvc.*;
import play.mvc.Http;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.commons.io.IOUtils;
import static play.mvc.Controller.request;


import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result aloha() {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String content = values.get("content")[0];
        final String contentId = values.get("contentId")[0];
        final String pageId = values.get("pageId")[0];
        System.out.println("pageId:" + pageId);
        System.out.println("contentId:" + contentId);
        System.out.println("Content:" + content);

        try {
            File input = new File(Play.application().path() + "/app/views/index.scala.html");
            Document doc = Jsoup.parse(input, "UTF-8", request().host() + request().path());

            String selector = "#" + contentId;
            Element part = doc.select(selector).first();

            if (part != null) {
                System.out.println("old:" + part);
                System.out.println("-------");
                Element newPart = part.clone();
                newPart.html(content);
                System.out.println("new:" + newPart);
                System.out.println("-------");


                replaceFileString(part.toString(), newPart.toString(), input.getPath());
            }

            System.out.println("-------");
        } catch (IOException io) {
            System.out.println("errors: " + io.getLocalizedMessage());
        }
        return ok();
    }

    public static void replaceFileString(String myPattern, String myReplacement, String myFileName) throws IOException {
        FileInputStream fis = new FileInputStream(myFileName);
        String content = IOUtils.toString(fis, Charset.defaultCharset());
        content = content.replaceAll(myPattern, myReplacement);
        IOUtils.write(content, new FileOutputStream(myFileName), Charset.defaultCharset());
    }

    public static boolean isAlohaOn() {
        return Play.application().configuration().getBoolean("play-aloha.admin");
    }
}
