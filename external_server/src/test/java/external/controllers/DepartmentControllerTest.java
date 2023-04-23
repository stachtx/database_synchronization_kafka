package external.controllers;

import com.database.integration.external.controllers.DepartmentController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

  @Autowired
  MockMvc mockMvc;

//    @Test
//    public void shouldGetDepartment() throws Exception {
//        //given
//        UUID uuid = UUID.fromString("5cbfeed2-1f26-11ea-978f-2e728ce88125");
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.get("/secured/departments/" + uuid))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(uuid)));
//        //then
//    }

}