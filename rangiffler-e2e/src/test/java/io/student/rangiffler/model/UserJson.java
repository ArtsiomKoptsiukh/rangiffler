package io.student.rangiffler.model;

public record UserJson(String id, String username, String password, Boolean enabled, Boolean accountNonExpired,
                       Boolean accountNonLocked, Boolean credentialsNonExpired) {
}
