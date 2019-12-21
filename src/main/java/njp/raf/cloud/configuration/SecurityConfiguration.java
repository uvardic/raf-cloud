package njp.raf.cloud.configuration;

import njp.raf.cloud.annotation.AuthorizationRole;
import njp.raf.cloud.exception.user.UserForbiddenException;
import njp.raf.cloud.user.domain.UserRole;
import njp.raf.cloud.user.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class SecurityConfiguration {

    private final TokenService tokenService;

    public SecurityConfiguration(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(njp.raf.cloud.annotation.AuthorizationRole))")
    public Object aroundEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Method endpointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        String userRoleName = tokenService.parseToken(joinPoint).get("role", String.class);

        if (isRoleAuthorized(endpointMethod, UserRole.valueOf(userRoleName)))
            return joinPoint.proceed();

        throw new UserForbiddenException(
                String.format("User with role %s is forbidden from accessing: %s!",
                        userRoleName, endpointMethod.getName())
        );
    }

    private boolean isRoleAuthorized(Method endpointMethod, UserRole role) {
        AuthorizationRole authorizationRole = endpointMethod.getAnnotation(AuthorizationRole.class);

        return Arrays.asList(authorizationRole.roles()).contains(role);
    }

}
