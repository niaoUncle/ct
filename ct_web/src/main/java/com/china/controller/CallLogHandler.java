package com.china.controller;


import com.china.bean.CallLog;
import com.china.bean.Contact;
import com.china.dao.CallLogDAO;
import com.china.dao.ContactDAO;
import com.china.entries.QueryInfo;
import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CallLogHandler {
    @RequestMapping("/queryContact")
    public ModelAndView query(Contact contact) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ContactDAO contactDAO = applicationContext.getBean(ContactDAO.class);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("id", String.valueOf(contact.getId()));
        List<Contact> contactList = contactDAO.getContactWithId(paramMap);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jsp/queryContact");
        modelAndView.addObject("contacts", contactList);
        return modelAndView;
    }

    @RequestMapping("/queryContactList")
    public ModelAndView querylist() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ContactDAO contactDAO = applicationContext.getBean(ContactDAO.class);
        List<Contact> contactList = contactDAO.getContacts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jsp/queryContact");
        modelAndView.addObject("contacts", contactList);
        return modelAndView;
    }

    @RequestMapping("/queryCallLogList")
    public ModelAndView queryCallLog(QueryInfo queryInfo){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        CallLogDAO callLogDAO = applicationContext.getBean(CallLogDAO.class);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("telephone", String.valueOf(queryInfo.getTelephone()));
        paramMap.put("year", String.valueOf(queryInfo.getYear()));
        paramMap.put("day", String.valueOf(queryInfo.getDay()));

        List<CallLog> callLogList = callLogDAO.getCallLogList(paramMap);

        Gson gson = new Gson();
        String resultList = gson.toJson(callLogList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jsp/callLogList");
        modelAndView.addObject("callLogList", resultList);
        return modelAndView;
    }

    @RequestMapping("/queryCallLogList2")
    public String queryCallLog2(Model model, QueryInfo queryInfo){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        CallLogDAO callLogDAO = applicationContext.getBean(CallLogDAO.class);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("telephone", String.valueOf(queryInfo.getTelephone()));
        paramMap.put("year", String.valueOf(queryInfo.getYear()));
        paramMap.put("day", String.valueOf(queryInfo.getDay()));

        List<CallLog> callLogList = callLogDAO.getCallLogList(paramMap);

        StringBuilder dateString = new StringBuilder();
        StringBuilder countString = new StringBuilder();
        StringBuilder durationString = new StringBuilder();

        for(int i = 0; i < callLogList.size(); i++){
            CallLog callLog = callLogList.get(i);
            if(Integer.valueOf(callLog.getMonth()) > 0){
                dateString.append(callLog.getMonth()).append("月").append(",");
                countString.append(callLog.getCall_sum()).append(",");
                durationString.append(Float.valueOf(callLog.getCall_duration_sum()) / 60f).append(",");
            }
        }
        //1月,2月,3月,4月,5月,6月,7月,8月,9月,10月,11月,12月,
        model.addAttribute("telephone", callLogList.get(0).getTelephone());
        model.addAttribute("name", callLogList.get(0).getName());
        model.addAttribute("date", dateString.deleteCharAt(dateString.length() - 1));
        model.addAttribute("count", countString.deleteCharAt(countString.length() - 1));
        model.addAttribute("duration", durationString.deleteCharAt(durationString.length() - 1));
        return "jsp/callLogListEchart";
    }

}
