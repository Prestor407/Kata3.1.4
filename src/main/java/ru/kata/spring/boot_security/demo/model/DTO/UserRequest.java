package ru.kata.spring.boot_security.demo.model.DTO;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.Arrays;
import java.util.Objects;

public class UserRequest {
    private User user;
    private Long[] roleIds;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return Objects.equals(user, that.user) && Objects.deepEquals(roleIds, that.roleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, Arrays.hashCode(roleIds));
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "user=" + user +
                ", roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
