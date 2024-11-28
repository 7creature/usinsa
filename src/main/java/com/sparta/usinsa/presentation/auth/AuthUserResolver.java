package com.sparta.usinsa.presentation.auth;

import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.presentation.common.annotation.AuthUser;
import com.sparta.usinsa.presentation.common.config.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
public class AuthUserResolver implements HandlerMethodArgumentResolver {

  private final JwtHelper jwtHelper;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasAuthUserAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
    boolean isUserType = User.class.isAssignableFrom(parameter.getParameterType());
    return hasAuthUserAnnotation && isUserType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    final HttpServletRequest httpServletRequest = webRequest.getNativeRequest(
        HttpServletRequest.class);

    final String token = Objects.requireNonNull(httpServletRequest)
        .getHeader(HttpHeaders.AUTHORIZATION);
    return jwtHelper.getUserIdFromToken(token);
  }

}
