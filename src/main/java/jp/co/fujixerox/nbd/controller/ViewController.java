package jp.co.fujixerox.nbd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ViewController {
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/")
    public String getView(Model model){
        System.out.println("httpSession");
        System.out.println(httpSession.getId());

        System.out.println("httpServletRequest");
        System.out.println(httpServletRequest.getSession(true).getId());

        model.addAttribute("voice", "ちゅん！");
        return "index";
    }
}