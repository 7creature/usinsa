package com.sparta.usinsa.presentation.auth;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.common.annotation.AuthUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasAuthUserAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
    boolean isUserType = User.class.isAssignableFrom(parameter.getParameterType());
    return hasAuthUserAnnotation && isUserType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());

    User user = (User) authentication.get().getPrincipal();
    return user;
  }

}
