package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.service.LinkService;
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
class LinkDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LinkService linkService;

    @Test
    public void testToString() {

        LinkDto linkDto = TestDataHelper.getLinkDto1();
        assertThat(linkDto.toString()).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        LinkDto linkDto = TestDataHelper.getLinkDto1();

        doReturn(linkDto).when(linkService).create(any());

        assertCreate(linkDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateStart() throws Exception {

        LinkDto linkDto = TestDataHelper.getLinkDto1();

        doReturn(linkDto).when(linkService).create(any());

        // without useless intersection fields.
        linkDto.getStart().setIntersectionName(null);
        linkDto.getStart().setGps(null);
        linkDto.getStart().setRegion(null);
        linkDto.getStart().setNationalId(null);
        linkDto.getStart().setCameras(null);
        assertCreate(linkDto, HttpStatus.OK, Response.OK, null, null);

        // intersection without intersection no.
        linkDto.getStart().setIntersectionNo(null);
        assertCreate(linkDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // null intersection
        linkDto.setStart(null);
        assertCreate(linkDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateEnd() throws Exception {

        LinkDto linkDto = TestDataHelper.getLinkDto1();

        doReturn(linkDto).when(linkService).create(any());

        // without useless intersection fields.
        linkDto.getEnd().setIntersectionName(null);
        linkDto.getEnd().setGps(null);
        linkDto.getEnd().setRegion(null);
        linkDto.getEnd().setNationalId(null);
        linkDto.getEnd().setCameras(null);
        assertCreate(linkDto, HttpStatus.OK, Response.OK, null, null);

        // intersection without intersection no.
        linkDto.getEnd().setIntersectionNo(null);
        assertCreate(linkDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // null intersection
        linkDto.setEnd(null);
        assertCreate(linkDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);
    }

    private void assertCreate(LinkDto linkDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/link";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(linkDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("link")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}