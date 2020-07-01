package com.kingdom.consultant.web.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.HostHolder;
import com.kingdom.pojo.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : long
 * @date : 2020/6/27 9:42
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Reference
    private ConsultantService consultantService;


    @Autowired
    private HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //从cookies中获取凭证
        String ticket = CommonUtils.getValue(request, "loginticket");
        if (ticket != null) {
            //查询凭证
            LoginTicket loginTicket = consultantService.findLoginTicket(ticket);
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 ) {
                //根据凭证查询用户
                Consultant consultant = consultantService.findConsultantById(loginTicket.getUserid());
                //在本次请求持有用户
                hostHolder.setConsultant(consultant);
            }
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}
