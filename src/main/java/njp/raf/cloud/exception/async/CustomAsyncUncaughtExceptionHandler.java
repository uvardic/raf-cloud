package njp.raf.cloud.exception.async;

import njp.raf.cloud.configuration.AsyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.error(String.format("%s: %s for method %s", ex.getClass(), ex.getMessage(), method.getName()));
    }

}
