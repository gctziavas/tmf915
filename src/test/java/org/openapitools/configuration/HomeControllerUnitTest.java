package org.openapitools.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HomeControllerUnitTest {

    @InjectMocks
    private HomeController homeController;

    @Test
    void index_redirectsToSwaggerUi() {
        String view = homeController.index();
        assertEquals("redirect:swagger-ui.html", view);
    }
}
