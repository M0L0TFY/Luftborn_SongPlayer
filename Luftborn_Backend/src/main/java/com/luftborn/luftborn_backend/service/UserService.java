package com.luftborn.luftborn_backend.service;

import com.luftborn.luftborn_backend.model.User;

public interface UserService {
    User getUserEntityById(Long userId);
}
