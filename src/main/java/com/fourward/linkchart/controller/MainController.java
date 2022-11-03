package com.fourward.linkchart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class MainController {

    @GetMapping(value = {"index", ""})
    public String index(Model model) {

//        뷰 에 현재날짜 제공
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDate = now.format(formatter);
        model.addAttribute("nowDate", nowDate);

        return "/index";
    }
}
