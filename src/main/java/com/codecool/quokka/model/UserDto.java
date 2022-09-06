package com.codecool.quokka.model;

import java.util.Objects;
import java.util.UUID;

public class UserDto {

    private String userName;
    private UUID id;

    public UserDto(String userName, UUID id) {
        this.userName = userName;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}