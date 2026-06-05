package com.daily_app.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daily_app.demo.Repository.RemindRepository;

@Service
public class RemindService {
    @Autowired
    private RemindRepository remindRepository;

}
