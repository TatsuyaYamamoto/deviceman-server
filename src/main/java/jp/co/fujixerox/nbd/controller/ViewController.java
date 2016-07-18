package jp.co.fujixerox.nbd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String getView(Model model){
        model.addAttribute("voice", "ちゅん！");
        return "index";
    }
}