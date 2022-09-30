package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.exception.customexception.InvalidParamException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.common.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class IntersectionDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IntersectionService intersectionService;

    @Test
    public void testToString() {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
        assertThat(intersectionDto.toString()).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

        doReturn(intersectionDto).when(intersectionService).create(any());

        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateIntersectionNo() throws Exception {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

        doReturn(intersectionDto).when(intersectionService).create(any());

        intersectionDto.setIntersectionNo(null);
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        intersectionDto.setIntersectionNo("");
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        intersectionDto.setIntersectionNo(" ");
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        intersectionDto.setIntersectionNo("intersection no");   // length 15
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        intersectionDto.setIntersectionNo("intersecti");        // length 10
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateIntersectionName() throws Exception {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

        doReturn(intersectionDto).when(intersectionService).create(any());

        intersectionDto.setIntersectionName(null);
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);

        intersectionDto.setIntersectionName("");
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);

        intersectionDto.setIntersectionName(" ");
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);

        intersectionDto.setIntersectionName("Long intersection name over 32---");   // length 33
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        intersectionDto.setIntersectionName("Long intersection name under 32-");    // length 32
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateGps() throws Exception {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

        doReturn(intersectionDto).when(intersectionService).create(any());

        // with null lat, lng.
        intersectionDto.getGps().setLat(null);
        intersectionDto.getGps().setLng(null);
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // null gps
        intersectionDto.setGps(null);
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateRegion() throws Exception {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

        doReturn(intersectionDto).when(intersectionService).create(any());

        // without useless region fields.
        intersectionDto.getRegion().setRegionName(null);
        intersectionDto.getRegion().setGps(null);
        intersectionDto.getRegion().setIntersections(null);
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);

        // region without region no.
        intersectionDto.getRegion().setRegionNo(null);
        assertCreate(intersectionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // null region
        intersectionDto.setRegion(null);
        assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
    }

    private void assertCreate(IntersectionDto intersectionDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/intersection";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(intersectionDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("intersection")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}