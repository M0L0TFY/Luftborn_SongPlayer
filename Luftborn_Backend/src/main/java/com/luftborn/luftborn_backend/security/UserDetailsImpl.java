package com.luftborn.luftborn_backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luftborn.luftborn_backend.model.User;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(
        Long id,
        String username,
        String email,
        @JsonIgnore String hashedPassword,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getHashedPassword(),
                List.of()
        );
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {return authorities;}
    @Override
    public @Nullable String getPassword() {return hashedPassword;}
    @Override
    public @NonNull String getUsername() {return username;}
    @Override
    public boolean isAccountNonExpired() {return true;}
    @Override
    public boolean isAccountNonLocked() {return true;}
    @Override
    public boolean isCredentialsNonExpired() {return true;}
    @Override
    public boolean isEnabled() {return true;}
}
