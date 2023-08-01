package springboot.cookbook.container;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MySQLJpaContainerExtension implements BeforeAllCallback, BeforeTestExecutionCallback {

    public static MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:latest")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        startContainerIfNeeded();
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        startContainerIfNeeded();
    }

    private void startContainerIfNeeded() {
        if (!mysqlContainer.isCreated()) {
            mysqlContainer.start();
            System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
            System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
            System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        }
    }
}
