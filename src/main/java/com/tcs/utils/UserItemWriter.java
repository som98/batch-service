package com.tcs.utils;

import com.tcs.models.User;
import com.tcs.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserItemWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void write(List<? extends User> list) throws Exception {
        list.stream().forEach(o -> userRepository.save(o));
    }
}
