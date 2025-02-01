package com.pinnacle.backend.service;

import java.util.List;

import com.pinnacle.backend.model.UserPayloadModel;

public interface UserService {
    public List<UserPayloadModel> getAllUsers();
}
