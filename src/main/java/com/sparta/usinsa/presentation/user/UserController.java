package com.sparta.usinsa.presentation.user;

import com.sparta.usinsa.application.service.UserService;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.common.annotation.AuthUser;
import com.sparta.usinsa.presentation.user.dto.request.UserUpdateRequestDto;
import com.sparta.usinsa.presentation.user.dto.response.UserResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @GetMapping()
  public ResponseEntity<Map<String, Object>> getAllUsers() {
    List<UserResponseDto> users = userService.getAllUsers();
    Map<String, Object> response = new HashMap<>();
    response.put("content", users);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<UserResponseDto> updateUser(@AuthUser User user, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
    UserResponseDto updatedUser = userService.updateUser(user, userUpdateRequestDto);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping()
  public ResponseEntity<Void> deleteUser(@AuthUser User user) {
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }
}