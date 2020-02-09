package com.tcs.utils;

import com.tcs.controllers.RestController;
import com.tcs.models.Props;
import com.tcs.models.User;
import com.tcs.services.ExcelServiceImpl;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
public class UserItemReader implements ItemReader<User> {
    @Autowired
    private ExcelServiceImpl excelService;
    private List<User> users;
    private int next=0;
    public UserItemReader(){
        System.out.println("Filename from ItemRaeder is "+RestController.filename);
        users=excelService.getUserListFromExcel(RestController.filename);
    }

    @Override
    public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return users.get(next++);
    }

}
