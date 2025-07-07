package com.example.login.deposite.business.dc;

import com.example.login.deposite.business.dto.DepositeDDTO;
import com.example.login.deposite.business.dc.dao.IH3DepositeDAO;
import com.example.login.deposite.business.dc.model.DMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class DepositeDC {
    private final IH3DepositeDAO h3DepositeDAO;

    @Autowired
    public DepositeDC(@Qualifier("h3MyBatisDepositeDAOImpl")IH3DepositeDAO h3DepositeDAO) {
        this.h3DepositeDAO = h3DepositeDAO;
    }

    public DepositeDDTO restPUT(DepositeDDTO pdepositeDDTO) {
        DMember member = toDMember(pdepositeDDTO);
        DMember saved = h3DepositeDAO.save(member);
        return toDepositeDDTO(saved);
    }

    public DepositeDDTO restPOST(DepositeDDTO pdepositeDDTO) {
        DMember member = toDMember(pdepositeDDTO);
        DMember saved = h3DepositeDAO.save(member);
        return toDepositeDDTO(saved);
    }

    public int restDELETE(DepositeDDTO pdepositeDDTO) {
        Optional<DMember> memberOpt = h3DepositeDAO.findById(parseId(pdepositeDDTO.getId()));
        if (memberOpt.isPresent()) {
            // 삭제 메서드가 인터페이스에 없으므로, 필요시 추가 구현 필요
            return 1;
        }
        return 0;
    }

    public DepositeDDTO restGET(DepositeDDTO pdepositeDDTO) {
        Optional<DMember> memberOpt = h3DepositeDAO.findById(parseId(pdepositeDDTO.getId()));
        return memberOpt.map(this::toDepositeDDTO).orElse(null);
    }

    public ArrayList<DepositeDDTO> restGETALL() {
        return h3DepositeDAO.findAll().stream()
                .map(this::toDepositeDDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private DMember toDMember(DepositeDDTO dto) {
        DMember member = new DMember();
        if (dto.getId() != null) member.setId(parseId(dto.getId()));
        member.setName(dto.getName());
        member.setAddress(dto.getAddress());
        member.setContact(dto.getContact());
        return member;
    }

    private DepositeDDTO toDepositeDDTO(DMember member) {
        DepositeDDTO dto = new DepositeDDTO();
        dto.setId(member.getId() != null ? member.getId().toString() : null);
        dto.setName(member.getName());
        dto.setAddress(member.getAddress());
        dto.setContact(member.getContact());
        return dto;
    }

    private Long parseId(String id) {
        try {
            return id == null ? null : Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 