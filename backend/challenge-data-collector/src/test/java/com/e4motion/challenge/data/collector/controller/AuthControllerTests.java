package com.e4motion.challenge.data.collector.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.data.collector.domain.dto.CameraLoginDto;
import com.e4motion.common.Response;
import com.e4motion.common.exception.customexception.CameraNotFoundException;
import com.e4motion.common.exception.customexception.UnauthorizedException;
import com.e4motion.common.utils.TestUtils;

@SpringBootTest
@ActiveProfiles("unittest")
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureMockMvc
public class AuthControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void login() throws Exception {
		assertLogin("C0001", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0",
				HttpStatus.OK, Response.OK, null);
	}
	
	@Test
	public void loginWithIncorrectPassword() throws Exception {
		assertLogin("C0002", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3------",
				HttpStatus.UNAUTHORIZED, Response.FAIL, UnauthorizedException.CODE);
	}
	
	@Test
	public void loginWithNoexistentCamera() throws Exception {
		assertLogin("C0100", "de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0",
				HttpStatus.NOT_FOUND, Response.FAIL, CameraNotFoundException.CODE);
	}
	
	private void assertLogin(String cameraId, String password, 
			HttpStatus expectedStatus, String expectedResult, String expectedCode) throws Exception {
		
		String uri = "/v1/camera/login";
		
		CameraLoginDto loginDto = CameraLoginDto.builder()
				.cameraId(cameraId)
				.password(password)
				.build();
	    
	    mockMvc.perform(MockMvcRequestBuilders.post(uri)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(TestUtils.getJsonContent(loginDto)))
	    .andExpect(result -> {
	    		MockHttpServletResponse res = result.getResponse();
	    		assertThat(res.getStatus()).isEqualTo(expectedStatus.value());
	    	
	    		Response response = TestUtils.getResponse(res.getContentAsString());
	    		assertThat(response.get(Response.RESULT)).isEqualTo(expectedResult);
	    		if (expectedCode != null) {
	    			assertThat(response.get(Response.CODE)).isEqualTo(expectedCode);
	    		}
	    });
	}

}
