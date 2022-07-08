package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.exception.customexception.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CameraControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	CameraService cameraService;

	@Test
	public void createWithoutRole() throws Exception {

		assertCreate(TestDataHelper.getCameraDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).create(any());

		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole_DuplicateCamera() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doThrow(new CameraDuplicateException(CameraDuplicateException.CAMERA_NO_ALREADY_EXISTS)).when(cameraService).create(any());

		assertCreate(cameraDto, HttpStatus.CONFLICT, Response.FAIL, CameraDuplicateException.CODE, CameraDuplicateException.CAMERA_NO_ALREADY_EXISTS);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).create(any());

		assertCreate(cameraDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void createWithInaccessibleRoles() throws Exception {

		assertCreate(TestDataHelper.getCameraDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void updateWithoutRole() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		doReturn(cameraDto1).when(cameraService).update(any(), any());

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole_NonexistentCamera() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		doThrow(new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO)).when(cameraService).update(any(), any());

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2,  HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE, CameraNotFoundException.INVALID_CAMERA_NO);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		doReturn(cameraDto1).when(cameraService).update(any(), any());

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA_ADMIN")
	public void updateWithCameraAdminRole() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		doReturn(cameraDto1).when(cameraService).update(any(), any());

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA"})
	public void updateWithInaccessibleRoles() throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		assertUpdate(cameraDto1.getCameraNo(), cameraDto2, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void deleteWithoutRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {

		assertDelete(TestDataHelper.getCameraDto1().getCameraNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getCameraDto2().getCameraNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {

		assertDelete(TestDataHelper.getCameraDto1().getCameraNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getCameraDto2().getCameraNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void deleteWitInaccessibleRole() throws Exception {

		assertDelete(TestDataHelper.getCameraDto1().getCameraNo(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void getWithoutRole() throws Exception {

		assertGet(TestDataHelper.getCameraDto1().getCameraNo(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole_NonexistentCamera() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(null).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), false, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "DATA")
	public void getWithDataRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA_ADMIN")
	public void getWithCameraAdminRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA")
	public void getWithCameraCameraRole() throws Exception {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		doReturn(cameraDto).when(cameraService).get(cameraDto.getCameraNo());

		assertGet(cameraDto.getCameraNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getWitInaccessibleRole() throws Exception {

		assertGet(TestDataHelper.getCameraDto1().getCameraNo(), true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}
	
	@Test
	public void getListWithoutRole() throws Exception {

		assertGetList(HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getListWithAdminRole() throws Exception {

		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}
	
	@Test
	@WithMockUser(roles = "MANAGER")
	public void getListWithManagerRole() throws Exception {

		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "DATA")
	public void getListWithDataRole() throws Exception {

		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA_ADMIN")
	public void getListWithCameraDataRole() throws Exception {

		assertGetList(HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getListWithInaccessibleRole() throws Exception {

		assertGetList(HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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

	private void assertUpdate(String cameraNo, CameraDto cameraDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/camera/" + cameraNo;

	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
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
	
	private void assertDelete(String cameraNo, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(cameraService).delete(cameraNo);
		
		String uri = "/v2/camera/" + cameraNo;

	    mockMvc.perform(MockMvcRequestBuilders.delete(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
	    			assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    			Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
	    			assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
	    			if (!expectedResult.equals(Response.OK)) {
	    				assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
	    			}
	    });
	}

	private void assertGet(String cameraNo, boolean regionExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/camera/" + cameraNo;

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						if (regionExpected) {
							assertThat(body.get("camera")).isNotNull();
						} else {
							assertThat(body.get("camera")).isNull();
						}
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}

	private void assertGetList(HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();

		List<CameraDto> cameraDtos = new ArrayList<>();
		cameraDtos.add(cameraDto1);
		cameraDtos.add(cameraDto2);

		doReturn(cameraDtos).when(cameraService).getList(null, null);

		String uri = "/v2/cameras";

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("cameras")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
