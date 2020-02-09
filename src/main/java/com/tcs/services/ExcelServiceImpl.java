package com.tcs.services;

import com.tcs.models.User;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<User> getUserListFromExcel(String filename) {
        List<User> users=new ArrayList<>();
        FileInputStream fis=null;
        try{

            fis=new FileInputStream(new File("src/main/resources/uploads/"+filename));

            XSSFWorkbook wb=new XSSFWorkbook(fis);
            XSSFSheet sheet=wb.getSheetAt(0);
            for(Row row:sheet){
                if(row.getRowNum()!=0){
                    User user=new User();
                    user.setName(row.getCell(0).getStringCellValue());
                    user.setEmail(row.getCell(1).getStringCellValue());
                    row.getCell(2).setCellType(CellType.STRING);
                    //System.out.println(row.getCell(2).getStringCellValue());
                    user.setContact(row.getCell(2).getStringCellValue());
                    users.add(user);
                }

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            try{
                fis.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return users;
    }
}
