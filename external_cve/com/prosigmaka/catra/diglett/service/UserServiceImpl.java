package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.User;
import com.prosigmaka.catra.diglett.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User insertUser(User user, Integer isDelete) {
        user.setIsDelete(isDelete);
        user = userRepository.save(user);
        return user;
    }
}
