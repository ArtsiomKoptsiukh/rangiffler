package io.student.rangiffler.tests;

import io.student.rangiffler.jupiter.extention.UsersQueueExtension;
import io.student.rangiffler.jupiter.extention.UsersQueueExtension.StaticUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.student.rangiffler.jupiter.extention.UsersQueueExtension.*;

@ExtendWith(UsersQueueExtension.class)
public class QueueUsersTest {

    @Test
    void testWithEmptyUser0(@UserType(empty = true) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);
    }

    @Test
    void testWithEmptyUser1(@UserType(empty = false) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);
    }

}
