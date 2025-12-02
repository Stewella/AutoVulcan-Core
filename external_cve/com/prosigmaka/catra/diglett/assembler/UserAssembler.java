package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.UserDto;
import com.prosigmaka.catra.diglett.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler implements InterfaceAssembler<User, UserDto>{

    @Override
    public User fromDto(UserDto dto) {
        if (dto == null) return null;

        User entity = new User();
        if(dto.getIdUser() != null) entity.setIdUser(dto.getIdUser());
        if(dto.getNama() != null) entity.setNama(dto.getNama());
        if(dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if(dto.getType() != null) entity.setType(dto.getType());
        return entity;
    }

    @Override
    public UserDto fromEntity(User entity) {
        if (entity == null) return null;

        return UserDto.builder()
                .idUser(entity.getIdUser())
                .nama(entity.getNama())
                .email(entity.getEmail())
                .type(entity.getType())
                .isDelete(entity.getIsDelete())
                .build();
    }
}
