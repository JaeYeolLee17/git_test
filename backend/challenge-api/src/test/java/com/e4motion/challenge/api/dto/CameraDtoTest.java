package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.service.CameraService;
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
class CameraDtoTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CameraService cameraService;

	@Test
	public void testToString() {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		assertThat(cameraDto.toString()).isNotNull();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validateOk() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validateCameraNo() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		cameraDto.setCameraNo(null);
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setCameraNo("");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setCameraNo(" ");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setCameraNo("camera no--");	// length 11
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setCameraNo("camera no-");	// length 10
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validatePassword() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		cameraDto.setPassword(null);
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword("");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword(" ");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword("camera password");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword("camera12");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword("12!@12!@");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		cameraDto.setPassword("cha12!@");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		// length 129
		cameraDto.setPassword("camera12!@12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		// length 128
		cameraDto.setPassword("camera12!@1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678");
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validateIntersection() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		// without useless intersection fields.
		cameraDto.getIntersection().setIntersectionName(null);
		cameraDto.getIntersection().setGps(null);
		cameraDto.getIntersection().setRegion(null);
		cameraDto.getIntersection().setNationalId(null);
		cameraDto.getIntersection().setCameras(null);
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);

		// intersection without intersection no.
		cameraDto.getIntersection().setIntersectionNo(null);
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		// null intersection
		cameraDto.setIntersection(null);
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validateDirection() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		// without useless intersection fields.
		cameraDto.getDirection().setIntersectionName(null);
		cameraDto.getDirection().setGps(null);
		cameraDto.getDirection().setRegion(null);
		cameraDto.getDirection().setNationalId(null);
		cameraDto.getDirection().setCameras(null);
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);

		// intersection without intersection no.
		cameraDto.getDirection().setIntersectionNo(null);
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		// null intersection
		cameraDto.setDirection(null);
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void validateGps() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		doReturn(cameraDto).when(cameraService).create(any());

		// with null lat, lng.
		cameraDto.getGps().setLat(null);
		cameraDto.getGps().setLng(null);
		assertCreate(cameraDto, HttpStatus.BAD_REQUEST, Response.FAIL, InvalidParamException.CODE, null);

		// null gps
		cameraDto.setGps(null);
		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	private void assertCreate(CameraDto cameraDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/camera";

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.toJson(cameraDto)))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("camera")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
