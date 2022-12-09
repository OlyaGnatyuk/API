package org.example.steps;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.example.RestRequests;
import org.junit.Assert;

public class AccessSystem {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String ENTER_ACTION = "ENTRANCE";
    private static final String EXIT_ACTION = "EXIT";
    private Integer keyId;
    private Integer roomId;
    private String actionResult;

    @Дано("^Ключ пользователя (\\d+) и номер комнаты (\\d+)$")
    public void setUserKeyRoomId(Integer keyId, Integer roomId) {
        this.keyId = keyId;
        this.roomId = roomId;
    }

    @Когда("^Пользователь входит в комнату$")
    public void enterRoom() {
        actionResult = RestRequests.checkAccess(BASE_URL, ENTER_ACTION, this.keyId, this.roomId, 200);
    }

    @Тогда("^Результат (.*?)$")
    public void checkActionResult(String actionResult) {
        Assert.assertEquals(actionResult, this.actionResult);
    }

    @Когда("^Пользователь выходит из комнаты$")
    public void userExitsRoom() {
        actionResult = RestRequests.checkAccess(BASE_URL, EXIT_ACTION, this.keyId, this.roomId, 200);
    }

    @Когда("^Пользователь не может войти в комнату$")
    public void userCanNotEnterRoom() {
        actionResult = RestRequests.checkAccess(BASE_URL, ENTER_ACTION, this.keyId, this.roomId, 403);
    }

    @Когда("^Проверяем состояние комнат$")
    public void checkRooms() {
        actionResult = RestRequests.getRoomsInfo("http://localhost:8080/", 200).asString();
    }

    @Когда("^Проверяем список пользователей$")
    public void checkUsers() {
        actionResult = RestRequests.getUsersInfo("http://localhost:8080/", 0, 20, 200).asString();
    }
}
