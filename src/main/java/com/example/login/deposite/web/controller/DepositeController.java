package com.example.login.deposite.web.controller;

import com.example.login.deposite.transfer.DepositeCDTO;
import com.example.login.deposite.business.as.DepositeAS;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * Depos
 */
@Controller
@RequestMapping("/deposite")
public class DepositeController {

    private final DepositeAS depositeAS;
    private static final Logger log = LoggerFactory.getLogger(DepositeController.class);


    public DepositeController(DepositeAS depositeAS) {
        this.depositeAS = depositeAS;
    }

    @GetMapping("/list")
    public String list(Model model) {
        log.info("[START] /deposite/list 요청 시작");

        DepositeCDTO dto = new DepositeCDTO();
        dto.setComDTO(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComDTO().setReqName("getall");
        DepositeCDTO result = depositeAS.execute(dto);
        model.addAttribute("list", result.getDdto());
        log.info("[END] /deposite/list 요청 종료");
        return "deposite/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        log.info("[START] /deposite/form 요청 시작");
        model.addAttribute("deposite", new DepositeCDTO());
        log.info("[END] /deposite/form 요청 종료");
        return "deposite/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DepositeCDTO depositeCDTO) {
        log.info("[START] /deposite/save 요청 시작: 저장할 데이터 = {}", depositeCDTO);
        depositeCDTO.setComDTO(new com.example.login.deposite.transfer.CommonDTO());
        log.info("-----------------------1");
        depositeCDTO.getComDTO().setReqName("post");
        log.info("-----------------------2");
        try {
            depositeAS.execute(depositeCDTO);
        } catch (Exception ex){
            ex.printStackTrace();
        }


        log.info("-----------------------3");
        log.info("[END] /deposite/save 요청 종료");
        return "redirect:/deposite/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        log.info("[START] /deposite/view/{} 요청 시작", id);

        DepositeCDTO dto = new DepositeCDTO();
        dto.setId(id);
        dto.setComDTO(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComDTO().setReqName("get");
        DepositeCDTO result = depositeAS.execute(dto);
        model.addAttribute("deposite", result.getDdto() != null && !result.getDdto().isEmpty() ? result.getDdto().get(0) : null);
        log.info("[END] /deposite/view/{} 요청 종료", id);
        return "deposite/view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        log.info("[START] /deposite/delete/{} 요청 시작", id);
        DepositeCDTO dto = new DepositeCDTO();
        dto.setId(id);
        dto.setComDTO(new com.example.login.deposite.transfer.CommonDTO());
        dto.getComDTO().setReqName("delete");
        depositeAS.execute(dto);
        log.info("[END] /deposite/delete/{} 요청 종료", id);
        return "redirect:/deposite/list";
    }
} 