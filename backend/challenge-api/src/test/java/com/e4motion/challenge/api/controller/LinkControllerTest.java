package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.service.LinkService;
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
public class LinkControllerTest {
	
	@Autowired 
	MockMvc mockMvc;
	
	@MockBean
	LinkService linkService;

	@Test
	public void createWithoutRole() throws Exception {

		assertCreate(TestDataHelper.getLinkDto1(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doReturn(linkDto).when(linkService).create(any());

		assertCreate(linkDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void createWithAdminRole_DuplicateLink() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doThrow(new LinkDuplicateException(LinkDuplicateException.LINK_START_END_ALREADY_EXISTS)).when(linkService).create(any());

		assertCreate(linkDto, HttpStatus.CONFLICT, Response.FAIL, LinkDuplicateException.CODE, LinkDuplicateException.LINK_START_END_ALREADY_EXISTS);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void createWithManagerRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doReturn(linkDto).when(linkService).create(any());

		assertCreate(linkDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void createWithInaccessibleRoles() throws Exception {

		assertCreate(TestDataHelper.getLinkDto1(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void updateWithoutRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		assertUpdate(linkDto.getLinkId(), linkDto, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		doReturn(linkDto).when(linkService).update(any(), any());

		assertUpdate(linkDto.getLinkId(), linkDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void updateWithAdminRole_NonexistentLink() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		doThrow(new LinkNotFoundException(LinkNotFoundException.INVALID_LINK_ID)).when(linkService).update(any(), any());

		assertUpdate(linkDto.getLinkId(), linkDto, HttpStatus.NOT_FOUND, Response.FAIL, LinkNotFoundException.CODE, LinkNotFoundException.INVALID_LINK_ID);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void updateWithManagerRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		doReturn(linkDto).when(linkService).update(any(), any());

		assertUpdate(linkDto.getLinkId(), linkDto, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void updateWithInaccessibleRoles() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		assertUpdate(linkDto.getLinkId(), linkDto, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void deleteWithoutRole() throws Exception {

		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void deleteWithAdminRole() throws Exception {

		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(99L, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteWithManagerRole() throws Exception {

		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.OK, Response.OK, null, null);
		assertDelete(88L, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void deleteWitInaccessibleRole() throws Exception {

		assertDelete(TestDataHelper.getLinkDto1().getLinkId(), HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
	}

	@Test
	public void getWithoutRole() throws Exception {

		assertGet(TestDataHelper.getLinkDto1().getLinkId(), true, HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE, UnauthorizedException.UNAUTHORIZED_TOKEN);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doReturn(linkDto).when(linkService).get(linkDto.getLinkId());

		assertGet(linkDto.getLinkId(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getWithAdminRole_NonexistentLink() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doReturn(null).when(linkService).get(linkDto.getLinkId());

		assertGet(linkDto.getLinkId(), false, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void getWithManagerRole() throws Exception {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		doReturn(linkDto).when(linkService).get(linkDto.getLinkId());

		assertGet(linkDto.getLinkId(), true, HttpStatus.OK, Response.OK, null, null);
	}

	@Test
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void getWitInaccessibleRole() throws Exception {

		assertGet(TestDataHelper.getLinkDto1().getLinkId(), true, HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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
	@WithMockUser(roles = {"USER", "DATA", "CAMERA_ADMIN"})
	public void getListWithInaccessibleRole() throws Exception {

		assertGetList(HttpStatus.FORBIDDEN, Response.FAIL, InaccessibleException.CODE, InaccessibleException.ACCESS_DENIED);
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

	private void assertUpdate(Long linkId, LinkDto linkDto, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/link/" + linkId;

	    mockMvc.perform(MockMvcRequestBuilders.put(uri)
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
	
	private void assertDelete(Long linkId, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		doNothing().when(linkService).delete(linkId);
		
		String uri = "/v2/link/" + linkId;

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

	private void assertGet(Long linkId, boolean regionExpected, HttpStatus expectedStatus, String expectedResult, String expectedCode, String expectedMessage) throws Exception {

		String uri = "/v2/link/" + linkId;

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						if (regionExpected) {
							assertThat(body.get("link")).isNotNull();
						} else {
							assertThat(body.get("link")).isNull();
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

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		List<LinkDto> linkDtos = new ArrayList<>();
		linkDtos.add(linkDto);

		doReturn(linkDtos).when(linkService).getList();

		String uri = "/v2/links";

		mockMvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(result -> {
					MockHttpServletResponse response = result.getResponse();
					assertThat(response.getStatus()).isEqualTo(expectedStatus.value());

					Response body = JsonHelper.fromJson(response.getContentAsString(), Response.class);
					assertThat(body.get(Response.RESULT)).isEqualTo(expectedResult);
					if (expectedResult.equals(Response.OK)) {
						assertThat(body.get("links")).isNotNull();
					} else {
						assertThat(body.get(Response.CODE)).isEqualTo(expectedCode);
						if (expectedMessage != null) {
							assertThat(body.get(Response.MESSAGE)).isEqualTo(expectedMessage);
						}
					}
				});
	}
}
