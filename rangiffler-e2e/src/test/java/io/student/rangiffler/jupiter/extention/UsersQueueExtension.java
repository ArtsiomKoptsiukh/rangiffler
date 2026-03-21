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

    public record StaticUser(String name, String password, String friend, String incomeRequest, String outcomeRequest) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    {
        EMPTY_USERS.add(new StaticUser("Britni", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("Anderson", "12345", "Lin", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("Val", "12345", null, "Lin", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("Lin", "12345", null, null, "Val"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
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
                                user = Optional.ofNullable(getQueueByType(ut.value()).poll());
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

    private Queue<StaticUser> getQueueByType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
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
                            UserType ut = p.getAnnotation(UserType.class);
                            getQueueByType(ut.value()).add(user);
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
