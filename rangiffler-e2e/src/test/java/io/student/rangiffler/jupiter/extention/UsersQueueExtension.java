package io.student.rangiffler.jupiter.extention;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String name, String password, boolean empty) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedQueue<>();

    {
        EMPTY_USERS.add(new StaticUser("Britni", "12345", true));
        NOT_EMPTY_USERS.add(new StaticUser("Daren", "12345", false));
        NOT_EMPTY_USERS.add(new StaticUser("Raphael", "12345", false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        boolean empty() default true;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(
                        p -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            UserType ut = p.getAnnotation(UserType.class);
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = ut.empty()
                                        ? Optional.ofNullable(EMPTY_USERS.poll())
                                        : Optional.ofNullable(NOT_EMPTY_USERS.poll());
                            }
                            Allure.getLifecycle().updateTestCase(tesCase -> {
                                tesCase.setStart(new Date().getTime());
                            });

                            user.ifPresentOrElse(
                                    u -> context.getStore(NAMESPACE).put(p.getName(), u),
                                    () -> { throw new IllegalStateException("Can't find user after 30 seconds"); }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(
                        p -> {
                            StaticUser user = context.getStore(NAMESPACE).get(p.getName(), StaticUser.class);
                            if (user == null) {
                                throw new IllegalStateException("User not found in store: " + p.getName());
                            }
                            if (user.empty()) {
                                EMPTY_USERS.add(user);
                            } else {
                                NOT_EMPTY_USERS.add(user);
                            }
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(parameterContext.getParameter().getName(), StaticUser.class);
    }
}
