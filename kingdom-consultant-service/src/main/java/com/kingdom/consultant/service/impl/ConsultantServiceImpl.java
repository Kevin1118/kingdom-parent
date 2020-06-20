package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.consultant.service.ConsultantService;
import com.kingdom.dao.ConsultantMapper;
import com.kingdom.pojo.Consultant;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Override
    public int register(Consultant consultant){
        return consultantMapper.insertConsultant(consultant);
    }
}
