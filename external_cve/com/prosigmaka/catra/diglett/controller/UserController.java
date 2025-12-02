package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.UserAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.UserDto;
import com.prosigmaka.catra.diglett.model.entity.User;
import com.prosigmaka.catra.diglett.repository.UserRepository;
import com.prosigmaka.catra.diglett.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserAssembler assembler;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserAssembler assembler, UserService userService) {
        this.userRepository = userRepository;
        this.assembler = assembler;
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public DefaultResponse<List<UserDto>> getListUser() {
        List<User> userList = userRepository.findAllByIsDeleteEquals(0);
        List<UserDto> userDtos =
                userList.stream()
                        .map(user -> assembler.fromEntity(user))
                        .collect(Collectors.toList());
        return DefaultResponse.ok(userDtos);
    }

    @PostMapping("/insert")
    public DefaultResponse<UserDto> insertEditUser(@RequestBody UserDto userDto) {
        User user = assembler.fromDto(userDto);
        user = userService.insertUser(user, 0);
        UserDto userDtoDB = assembler.fromEntity(user);
        if (user == null) {
            return DefaultResponse.error("Gagal menyimpan");
        }
        return DefaultResponse.ok(userDtoDB, "Berhasil menyimpan");
    }

    @PostMapping("/delete/{id}")
    public DefaultResponse<UserDto> deleteUser(@PathVariable String id) {
        User user = userRepository.findByIdUserEquals(id);
        user = userService.insertUser(user, 1);
        UserDto userDtoDB = assembler.fromEntity(user);
        if (user == null) {
            return DefaultResponse.error("Gagal menghapus");
        }

        return DefaultResponse.ok(userDtoDB, "Berhasil menghapus");
    }
}
