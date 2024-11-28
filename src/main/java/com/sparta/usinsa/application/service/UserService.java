package com.sparta.usinsa.application.service;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import com.sparta.usinsa.presentation.user.dto.request.UserUpdateRequestDto;
import com.sparta.usinsa.presentation.user.dto.response.UserResponseDto;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .filter(user -> !user.isDeleted())
        .map(UserResponseDto::new)
        .toList();
  }

  @Transactional
  public UserResponseDto updateUser(User user, UserUpdateRequestDto userUpdateRequestDto) {
    if (!passwordEncoder.matches(userUpdateRequestDto.getCurrentPassword(), user.getPassword())) {
      throw new CustomException("현재 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
    user.updateName(userUpdateRequestDto.getName());
    user.updatePassword(passwordEncoder.encode(userUpdateRequestDto.getNewPassword()));
    return new UserResponseDto(user);
  }

  @Transactional
  public void deleteUser(User user) {
    if (user.isDeleted()) {
      throw new CustomException("이미 탈퇴한 회원입니다.", HttpStatus.BAD_REQUEST);
    }
    user.delete();
    userRepository.save(user);
  }
}