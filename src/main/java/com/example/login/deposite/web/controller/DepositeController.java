package com.example.login.deposite.web.controller;

import com.example.login.deposite.transfer.DepositeCDTO;
import com.example.login.deposite.business.as.DepositeAS;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/deposite")
public class DepositeController {

    private final DepositeAS depositeAS;

    public DepositeController(DepositeAS depositeAS) {
        this.depositeAS = depositeAS;
    }

    @GetMapping("/list")
    public String list(Model model) {
        DepositeCDTO dto = new DepositeCDTO();
        dto.setComdto(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComdto().setReqName("getall");
        DepositeCDTO result = depositeAS.execute(dto);
        model.addAttribute("list", result.getDdto());
        return "deposite/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("deposite", new DepositeCDTO());
        return "deposite/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DepositeCDTO depositeCDTO) {
        depositeCDTO.setComdto(new com.example.login.deposite.transfer.CommonDTO());
        depositeCDTO.getComdto().setReqName("post");
        depositeAS.execute(depositeCDTO);
        return "redirect:/deposite/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        DepositeCDTO dto = new DepositeCDTO();
        dto.setId(id);
        dto.setComdto(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComdto().setReqName("get");
        DepositeCDTO result = depositeAS.execute(dto);
        model.addAttribute("deposite", result.getDdto() != null && !result.getDdto().isEmpty() ? result.getDdto().get(0) : null);
        return "deposite/view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        DepositeCDTO dto = new DepositeCDTO();
        dto.setId(id);
        dto.setComdto(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComdto().setReqName("delete");
        depositeAS.execute(dto);
        return "redirect:/deposite/list";
    }
} 