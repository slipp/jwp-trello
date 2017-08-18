package slipp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import slipp.domain.User;
import slipp.domain.UserRepository;
import support.domain.UnAuthorizedException;

@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        User user = userRepository.findByEmail(SecurityUtils.getUsername());
        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser.required() && user == null) {
            throw new UnAuthorizedException("You're required Login!");
        }
        
        return user;
    }
}
