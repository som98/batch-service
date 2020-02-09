package com.tcs.config;
import com.tcs.controllers.RestController;
import com.tcs.models.Props;
import com.tcs.models.User;
import com.tcs.utils.UserItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;


@EnableBatchProcessing
public class SpringBatchConfig {
    @Value("${job-name}")
    private String jobName;
    @Value("${step-name}")
    private String stepName;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,ItemReader<User> itemReader, ItemWriter<User> itemWriter){
        Step step=stepBuilderFactory.get(stepName).<User,User>chunk(50).reader(itemReader).writer(itemWriter).build();
        return jobBuilderFactory.get(jobName).incrementer(new RunIdIncrementer()).start(step).build();
    }

    @Bean
    @StepScope
    ItemReader<User> reader(){
        System.out.println("The filename is "+RestController.filename);
        return new UserItemReader();
    }


}
