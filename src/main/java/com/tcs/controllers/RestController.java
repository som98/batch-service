package com.tcs.controllers;

import com.tcs.config.SpringBatchConfig;
import com.tcs.models.Props;
import com.tcs.models.ResponseModel;
import com.tcs.models.User;
import com.tcs.repository.UserRepository;
import com.tcs.services.ExcelService;
import com.tcs.services.ExcelServiceImpl;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class RestController {

    @Value("${template}")
    private String templateLocation;
    @Autowired
    private ExcelServiceImpl excelService;
//    @Autowired
//    private JobLauncher jobLauncher;
//    @Autowired
//    private Job job;

    @Autowired
    BeanFactory bf;
    @Autowired
    UserRepository userRepository;
    public static String filename="";


    /**
     * Method to return view to browser
     * @return index.html file to view in browser
     */
    @GetMapping(path = "/")
    public String index(){
        return "index";
    }


    /**
     * Method to send excel template for download
     * @return excel template resource
     * @throws FileNotFoundException
     */
    @GetMapping(path="/template")
    @ResponseBody
    public ResponseEntity<Resource> getExcelTemplate()throws Exception

    {

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx");

        File file=null;
        Path path;
        ByteArrayResource resource=null;
        try{
            file=ResourceUtils.getFile("src/main/resources/static/template.xlsx");
            path= Paths.get(file.getAbsolutePath());
            resource = new ByteArrayResource(Files.readAllBytes(path));
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!file.exists() || resource==null){
            System.err.println("File is not found");
            System.out.println("PLAYING AT "+RestController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

            throw new FileNotFoundException("Unable to find template.xlsx");
        }
        System.out.println("File is sent to user");
        return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseModel uploadExcelFile(@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            //redirectAttributes.addFlashAttribute("message","No File Selected");
            return new ResponseModel(false,"No File Selected");
        }
        try{
            byte[] bytes=file.getBytes();
            //System.out.println(file.getOriginalFilename());
            Path path=Paths.get("src/main/resources/uploads/"+file.getOriginalFilename());
            //System.out.println(path.toAbsolutePath());
            Files.write(path,bytes);
            //redirectAttributes.addFlashAttribute("message","Upload Successful");
            //System.out.println("Upload success");
        }catch(IOException e){
            e.printStackTrace();
        }
        return new ResponseModel(true,"Upload Successful");
    }


    @GetMapping(path = "/batch")
    @ResponseBody
    public ResponseModel startBatch(@RequestParam("filename")String filename){
        System.out.println(filename);
        this.filename=filename;
        File file=new File("src/main/resources/uploads/"+filename);
        if(!file.exists()){
            System.out.println(file.getAbsolutePath());
            return new ResponseModel(false,"File Not Found");
        }
        //SpringBatchConfig.filename=filename;
        List<User> list=excelService.getUserListFromExcel(filename);
        list.stream().forEach(o->{
            System.out.println("Saving "+o.toString());
            userRepository.save(o);
        });
//        JobLauncher jobLauncher=bf.getBean(JobLauncher.class);
//        Job job=bf.getBean(Job.class);
//        JobParameters params=new JobParametersBuilder().addString("filename",this.filename).toJobParameters();
//        try{
//            jobLauncher.run(job,params);
//        }catch(Exception e){
//            return new ResponseModel(false,"Error in Running Job");
//        }

        return new ResponseModel(true,"Batch Run Successfully Completed");
    }


}
