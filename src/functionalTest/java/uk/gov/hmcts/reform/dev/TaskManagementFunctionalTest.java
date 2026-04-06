package uk.gov.hmcts.reform.dev;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TaskManagementFunctionalTest {

    @Value("${TEST_URL:http://localhost:4000}")
    private String testUrl;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void getAllTasks() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/tasks")
            .then()
            .statusCode(200);
    }

    @Test
    void createTask() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "Functional Test Task",
                    "description": "Created via functional test",
                    "status": "PENDING",
                    "dueDate": "2026-12-01T10:00:00"
                }
                """)
            .when()
            .post("/tasks/create")
            .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("title", equalTo("Functional Test Task"))
            .body("status", equalTo("PENDING"));
    }

    @Test
    void getTaskById() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/tasks/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void updateTaskStatus() {
        int id = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "Task To Update",
                    "description": "Will be updated",
                    "status": "PENDING",
                    "dueDate": "2026-12-01T10:00:00"
                }
                """)
            .when()
            .post("/tasks/create")
            .then()
            .statusCode(200)
            .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
            .body("""
                { "status": "COMPLETED" }
                """)
            .when()
            .patch("/tasks/" + id + "/status")
            .then()
            .statusCode(200)
            .body("status", equalTo("COMPLETED"));
    }

    @Test
    void deleteTask() {
        int id = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "Task To Delete",
                    "description": "Will be deleted",
                    "status": "PENDING",
                    "dueDate": "2026-12-01T10:00:00"
                }
                """)
            .when()
            .post("/tasks/create")
            .then()
            .statusCode(200)
            .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/tasks/delete/" + id)
            .then()
            .statusCode(204);
    }
}
