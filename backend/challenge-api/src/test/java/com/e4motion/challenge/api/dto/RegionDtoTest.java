package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.service.RegionService;
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
class RegionDtoTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegionService regionService;

    @Test
    public void testToString() {

        RegionDto regionDto = TestDataHelper.getRegionDto1();
        assertThat(regionDto.toString()).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateOk() throws Exception {

        RegionDto regionDto = TestDataHelper.getRegionDto1();

        doReturn(regionDto).when(regionService).create(any());

        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateRegionNo() throws Exception {

        RegionDto regionDto = TestDataHelper.getRegionDto1();

        doReturn(regionDto).when(regionService).create(any());

        regionDto.setRegionNo(null);
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        regionDto.setRegionNo("");
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        regionDto.setRegionNo(" ");
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        regionDto.setRegionNo("region no--");   // length 11
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        regionDto.setRegionNo("region no-");    // length 10
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateRegionName() throws Exception {

        RegionDto regionDto = TestDataHelper.getRegionDto1();

        doReturn(regionDto).when(regionService).create(any());

        regionDto.setRegionName(null);
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);

        regionDto.setRegionName("");
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);

        regionDto.setRegionName(" ");
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);

        regionDto.setRegionName("Long region name exceed length 32");   // length 33
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        regionDto.setRegionName("Long region name length 32 12345");    // length 32
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void validateGps() throws Exception {

        RegionDto regionDto = TestDataHelper.getRegionDto1();

        doReturn(regionDto).when(regionService).create(any());

        // with null lat, lng.
        for (GpsDto gps : regionDto.getGps()) {
            gps.setLat(null);
            gps.setLng(null);
        }
        assertCreate(regionDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

        // empty gps
        regionDto.getGps().clear();
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);

        // null gps
        regionDto.setGps(null);
        assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
    }

    private void assertCreate(RegionDto regionDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

        String uri = "/v2/region";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(regionDto)))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

                    Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
                    assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
                    if (expectedResult.equals(Response.OK)) {
                        assertThat(body.get("region")).isNotNull();
                    } else {
                        assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
                        if (expectedMessage != null) {
                            assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
                        }
                    }
                });
    }
}