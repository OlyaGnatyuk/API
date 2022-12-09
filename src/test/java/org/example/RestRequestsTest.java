package org.example;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;

@Slf4j
public class RestRequestsTest {

    @Test
    public void testUserEnterTheRoomAtWorkingHours() {
        if (isWorkingHours()) {
            checkRoomsAreEmpty();

            String response = RestRequests.checkAccess("http://localhost:8080/", "ENTRANCE", 1, 1, 200);
            Assert.assertEquals("You are welcome!", response);

            Response roomsInfo = RestRequests.getRoomsInfo("http://localhost:8080/", 200);
            Assert.assertEquals("[{\"roomId\":1,\"userIds\":[1]}]", roomsInfo.asString());

            Response usersInfo = RestRequests.getUsersInfo("http://localhost:8080/", 0, 20, 200);

            Assert.assertEquals("[{\"id\":1,\"roomId\":1},{\"id\":2},{\"id\":3},{\"id\":4},{\"id\":5},{\"id\":6},{\"id\":7},{\"id\":8},{\"id\":9},{\"id\":10}]", usersInfo.asString());

            response = RestRequests.checkAccess("http://localhost:8080/", "EXIT", 1, 1, 200);
            Assert.assertEquals("Goodbye!", response);

            checkRoomsAreEmpty();

            response = RestRequests.checkAccess("http://localhost:8080/", "ENTRANCE", 2, 3, 403);
            Assert.assertEquals("You has no privileges to enter this room", response);

            checkRoomsAreEmpty();
        }
    }

    @Test
    public void testUserEnterTheRoomOutOfWorkingHours() {
        if (!isWorkingHours()) {
            checkRoomsAreEmpty();

            String response = RestRequests.checkAccess("http://localhost:8080/", "ENTRANCE", 1, 1, 403);
            Assert.assertEquals("Too late to work now. Please come back tomorrow, in 8.00", response);

            checkRoomsAreEmpty();

            response = RestRequests.checkAccess("http://localhost:8080/", "ENTRANCE", 2, 3, 403);
            Assert.assertEquals("Too late to work now. Please come back tomorrow, in 8.00", response);

            checkRoomsAreEmpty();
        }
    }

    @Test
    public void testRoomsInfo() {
        RestRequests.getRoomsInfo("http://localhost:8080/", 200);
    }

    @Test
    public void testUsersInfo() {
        RestRequests.getUsersInfo("http://localhost:8080/", 0, 20, 200);
    }

    private static void checkRoomsAreEmpty() {
        Response roomsInfo = RestRequests.getRoomsInfo("http://localhost:8080/", 200);
        Assert.assertEquals("[]", roomsInfo.asString());

        Response usersInfo = RestRequests.getUsersInfo("http://localhost:8080/", 0, 20, 200);
        Assert.assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4},{\"id\":5},{\"id\":6},{\"id\":7},{\"id\":8},{\"id\":9},{\"id\":10}]", usersInfo.asString());
    }

    private boolean isWorkingHours() {
        LocalTime now = LocalTime.now();
        LocalTime workingTimeStart = LocalTime.of(7, 59, 59);
        LocalTime workingTimeEnd = LocalTime.of(22, 0,1);

        return now.isAfter(workingTimeStart) && now.isBefore(workingTimeEnd);
    }

}