package com.sparta.usinsa.presentation.common.config.security;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.auth.UserType;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
  private final User user;

  public UserPrincipal(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user.getType() == UserType.OWNER) {
      return Collections.singletonList(new SimpleGrantedAuthority("ROLE_OWNER"));
    } else {
      return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
  }

  public User getUser() {
    return this.user;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
