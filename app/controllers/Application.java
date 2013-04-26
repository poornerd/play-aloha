package controllers;

import play.*;
import play.mvc.*;
import java.util.Map;  

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
  		return ok();
  	}




}
