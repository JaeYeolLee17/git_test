package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.IntersectionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
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
public class IntersectionControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	IntersectionService intersectionService;

	@Test
	public void createWithoutRole() throws Exception {

		assertCreate(TestDataHelper.getIntersectionDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).create(any());

		assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole_DuplicateIntersection() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doThrow(new IntersectionDuplicateException(IntersectionDuplicateException.INTERSECTION_NO_ALREADY_EXISTS)).when(intersectionService).create(any());

		assertCreate(intersectionDto, HttpStatus.CONFLICT, Response.FAIL, IntersectionDuplicateException.CODE, IntersectionDuplicateException.INTERSECTION_NO_ALREADY_EXISTS);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).create(any());

		assertCreate(intersectionDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void createWithInaccessibleRoles() throws Exception {

		assertCreate(TestDataHelper.getIntersectionDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void updateWithoutRole() throws Exception {

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		assertUpdate(intersectionDto1.getIntersectionNo(), intersectionDto2, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		doReturn(intersectionDto1).when(intersectionService).update(any(), any());

		assertUpdate(intersectionDto1.getIntersectionNo(), intersectionDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole_NonexistentIntersection() throws Exception {

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		doThrow(new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO)).when(intersectionService).update(any(), any());

		assertUpdate(intersectionDto1.getIntersectionNo(), intersectionDto2,  HttpStatus.NOT_FOUND, Response.FAIL, IntersectionNotFoundException.CODE, IntersectionNotFoundException.INVALID_INTERSECTION_NO);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		doReturn(intersectionDto1).when(intersectionService).update(any(), any());

		assertUpdate(intersectionDto1.getIntersectionNo(), intersectionDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void updateWithInaccessibleRoles() throws Exception {

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		assertUpdate(intersectionDto1.getIntersectionNo(), intersectionDto2, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void deleteWithoutRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {

		assertDelete(TestDataHelper.getIntersectionDto1().getIntersectionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getIntersectionDto2().getIntersectionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {

		assertDelete(TestDataHelper.getIntersectionDto1().getIntersectionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getIntersectionDto2().getIntersectionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void deleteWitInaccessibleRole() throws Exception {

		assertDelete(TestDataHelper.getIntersectionDto1().getIntersectionNo(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void getWithoutRole() throws Exception {

		assertGet(TestDataHelper.getIntersectionDto1().getIntersectionNo(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).get(intersectionDto.getIntersectionNo());

		assertGet(intersectionDto.getIntersectionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole_NonexistentIntersection() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(null).when(intersectionService).get(intersectionDto.getIntersectionNo());

		assertGet(intersectionDto.getIntersectionNo(), false, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).get(intersectionDto.getIntersectionNo());

		assertGet(intersectionDto.getIntersectionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "DATA")
	public void getWithDataRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).get(intersectionDto.getIntersectionNo());

		assertGet(intersectionDto.getIntersectionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA_ADMIN")
	public void getWithCameraAdminRole() throws Exception {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		doReturn(intersectionDto).when(intersectionService).get(intersectionDto.getIntersectionNo());

		assertGet(intersectionDto.getIntersectionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getWitInaccessibleRole() throws Exception {

		assertGet(TestDataHelper.getIntersectionDto1().getIntersectionNo(), true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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

	private void assertUpdate(String intersectionNo, IntersectionDto intersectionDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/intersection/" + intersectionNo;

	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
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
	
	private void assertDelete(String intersectionNo, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(intersectionService).delete(intersectionNo);
		
		String uri = "/v2/intersection/" + intersectionNo;

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

	private void assertGet(String intersectionNo, boolean regionExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/intersection/" + intersectionNo;

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						if (regionExpected) {
							assertThat(body.get("intersection")).isNotNull();
						} else {
							assertThat(body.get("intersection")).isNull();
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

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();

		List<IntersectionDto> intersectionDtos = new ArrayList<>();
		intersectionDtos.add(intersectionDto1);
		intersectionDtos.add(intersectionDto2);

		doReturn(intersectionDtos).when(intersectionService).getList(null);

		String uri = "/v2/intersections";

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("intersections")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
