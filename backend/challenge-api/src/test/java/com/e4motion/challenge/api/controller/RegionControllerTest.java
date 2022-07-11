package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.service.RegionService;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.RegionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
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
public class RegionControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	RegionService regionService;

	@Test
	public void createWithoutRole() throws Exception {

		assertCreate(TestDataHelper.getRegionDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).create(any());

		assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole_DuplicateRegion() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doThrow(new RegionDuplicateException(RegionDuplicateException.REGION_NO_ALREADY_EXISTS)).when(regionService).create(any());

		assertCreate(regionDto, HttpStatus.CONFLICT, Response.FAIL, RegionDuplicateException.CODE, RegionDuplicateException.REGION_NO_ALREADY_EXISTS);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).create(any());

		assertCreate(regionDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void createWithInaccessibleRoles() throws Exception {

		assertCreate(TestDataHelper.getRegionDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void updateWithoutRole() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		assertUpdate(regionDto1.getRegionNo(), regionDto2, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		doReturn(regionDto1).when(regionService).update(any(), any());

		assertUpdate(regionDto1.getRegionNo(), regionDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole_NonexistentRegion() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		doThrow(new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_NO)).when(regionService).update(any(), any());

		assertUpdate(regionDto1.getRegionNo(), regionDto2, HttpStatus.NOT_FOUND, Response.FAIL, RegionNotFoundException.CODE, RegionNotFoundException.INVALID_REGION_NO);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		doReturn(regionDto1).when(regionService).update(any(), any());

		assertUpdate(regionDto1.getRegionNo(), regionDto2, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void updateWithInaccessibleRoles() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		assertUpdate(regionDto1.getRegionNo(), regionDto2, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void deleteWithoutRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto1().getRegionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto1().getRegionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.OK, Response.OK, null, null);
		assertDelete("another_user", HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void deleteWitInaccessibleRole() throws Exception {

		assertDelete(TestDataHelper.getRegionDto2().getRegionNo(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void getWithoutRole() throws Exception {

		assertGet(TestDataHelper.getRegionDto1().getRegionNo(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).get(regionDto.getRegionNo());

		assertGet(regionDto.getRegionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole_NonexistentRegion() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(null).when(regionService).get(regionDto.getRegionNo());

		assertGet(regionDto.getRegionNo(), false, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).get(regionDto.getRegionNo());

		assertGet(regionDto.getRegionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "DATA")
	public void getWithDataRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).get(regionDto.getRegionNo());

		assertGet(regionDto.getRegionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "CAMERA_ADMIN")
	public void getWithCameraAdminRole() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		doReturn(regionDto).when(regionService).get(regionDto.getRegionNo());

		assertGet(regionDto.getRegionNo(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getWitInaccessibleRole() throws Exception {

		assertGet(TestDataHelper.getRegionDto1().getRegionNo(), true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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

	private void assertUpdate(String regionNo, RegionDto regionDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/region/" + regionNo;

	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
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
	
	private void assertDelete(String regionNo, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(regionService).delete(regionNo);
		
		String uri = "/v2/region/" + regionNo;

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

	private void assertGet(String regionNo, boolean regionExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/region/" + regionNo;

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						if (regionExpected) {
							assertThat(body.get("region")).isNotNull();
						} else {
							assertThat(body.get("region")).isNull();
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

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		List<RegionDto> regionDtos = new ArrayList<>();
		regionDtos.add(regionDto1);
		regionDtos.add(regionDto2);

		doReturn(regionDtos).when(regionService).getList();

		String uri = "/v2/regions";

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("regions")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
