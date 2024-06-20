package org.xpd.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
public class ApplicationController implements ErrorController{

	@RequestMapping("/error")
	@ResponseBody
	public String handleError(HttpServletRequest request) {
		return String.format("<html><head><script type=\"application/javascript\">\n" + 
				"    function updateURL() {\n" + 
				"        document.getElementById(\"openapp\").href = location.protocol+\"//\"+location.host;\n" + 
				"    }\n" + 
				"    </script></head><body onload=\"updateURL()\"><h2>Error Occurred</h2><div>Please do not press refresh or back browser buttons</div>\n" + 
				"	 <div>You can login <b><a id=\"openapp\" href='#' >here</a> </b>again </div><body></html>");
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
}
