package springboot.cookbook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.cookbook.container.MySQLJpaContainerExtension;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({MySQLJpaContainerExtension.class})
class CookbookApplicationTests {

    @Test
    void contextLoads() {
    }

}
