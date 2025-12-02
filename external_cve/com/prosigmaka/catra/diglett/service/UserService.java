package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.User;

public interface UserService {

    User insertUser(User user, Integer isDelete);
}
