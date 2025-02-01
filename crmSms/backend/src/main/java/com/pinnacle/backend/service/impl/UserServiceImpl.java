package com.pinnacle.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pinnacle.backend.model.UserPayloadModel;
import com.pinnacle.backend.repository.UserPayloadModelRepository;
import com.pinnacle.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserPayloadModelRepository repo;

    @Override
    public List<UserPayloadModel> getAllUsers() {
        return repo.findAll();
    }
}
