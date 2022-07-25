package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class CameraMapperTest {

	CameraMapper mapper = Mappers.getMapper(CameraMapper.class);

	ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	public void toStringCamera() {

		Camera camera = TestDataHelper.getCamera1();
		assertThat(camera.toString()).isNotNull();
	}

	@Test
	public void toStringCameraDto() {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		assertThat(cameraDto.toString()).isNotNull();
	}

	@Test
	public void mappingCameraRoadLane() throws JsonProcessingException {

		// String[] -> json string
		String[] object = TestDataHelper.getCameraDto1().getRoad().getLane();
		String json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo(TestDataHelper.getCamera1().getRoad().getLane());

		object = new String[]{"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9"};
		json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo("[\"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9\"]");

		object = new String[]{};
		json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo("[]");

		// json string -> String[]
		json = TestDataHelper.getCamera1().getRoad().getLane();
		object = jsonMapper.readValue(json, String[].class);
		assertThat(object).isEqualTo(TestDataHelper.getCameraDto1().getRoad().getLane());

		json = "[\"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9\"]";
		object = jsonMapper.readValue(json, String[].class);
		assertThat(object).isEqualTo(new String[]{"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9"});

		json = "[]";
		object = jsonMapper.readValue(json, String[].class);
		assertThat(object).isEqualTo(new String[]{});
	}

	@Test
	public void mappingCameraRoadDirection() throws JsonProcessingException {

		// Boolean[][] -> json string
		Boolean[][] object = TestDataHelper.getCameraDto1().getRoad().getDirection();
		String json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo(TestDataHelper.getCamera1().getRoad().getDirection());

		object = new Boolean[][]{ new Boolean[]{true, true, false}, new Boolean[]{true, false, false}, new Boolean[]{false, false, false}};
		json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo("[[true,true,false],[true,false,false],[false,false,false]]");

		object = new Boolean[][]{ new Boolean[]{}, new Boolean[]{}};
		json = jsonMapper.writeValueAsString(object);
		assertThat(json).isEqualTo("[[],[]]");

		// json string -> Boolean[][]
		json = TestDataHelper.getCamera1().getRoad().getDirection();
		object = jsonMapper.readValue(json, Boolean[][].class);
		assertThat(object).isEqualTo(TestDataHelper.getCameraDto1().getRoad().getDirection());

		json = "[[true,true,false],[true,false,false],[false,false,false]]";
		object = jsonMapper.readValue(json, Boolean[][].class);
		assertThat(object).isEqualTo(new Boolean[][]{ new Boolean[]{true, true, false}, new Boolean[]{true, false, false}, new Boolean[]{false, false, false}});

		json = "[[],[]]";
		object = jsonMapper.readValue(json, Boolean[][].class);
		assertThat(object).isEqualTo(new Boolean[][]{ new Boolean[]{}, new Boolean[]{}});
	}

	@Test
    public void toCameraDto() throws JsonProcessingException {

		Camera camera = TestDataHelper.getCamera1();

		CameraDto cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without region in intersection.
		camera.getIntersection().setRegion(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without intersection
		camera.setIntersection(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without region in direction.
		camera.getDirection().setRegion(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without direction.
		camera.setDirection(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without gps.
		camera.setLat(null);
		camera.setLng(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);

		// without road.
		camera.setRoad(null);

		cameraDto = mapper.toCameraDto(camera);
		assertMapTo(camera, cameraDto);
	}

	@Test
	public void toCamera() throws JsonProcessingException {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();

		Camera camera = mapper.toCamera(cameraDto);
		assertMapTo(cameraDto, camera);

		// without intersection.
		cameraDto.setIntersection(null);

		camera = mapper.toCamera(cameraDto);
		assertMapTo(cameraDto, camera);

		// without direction.
		cameraDto.setDirection(null);

		camera = mapper.toCamera(cameraDto);
		assertMapTo(cameraDto, camera);

		// without gps.
		cameraDto.setGps(null);

		camera = mapper.toCamera(cameraDto);
		assertMapTo(cameraDto, camera);

		// without road.
		cameraDto.setRoad(null);

		camera = mapper.toCamera(cameraDto);
		assertMapTo(cameraDto, camera);
	}

	private void assertMapTo(Camera camera, CameraDto cameraDto) throws JsonProcessingException {

		assertThat(camera.getCameraNo()).isEqualTo(cameraDto.getCameraNo());
		assertThat(cameraDto.getPassword()).isNull();

		if (camera.getIntersection() == null) {
			assertThat(cameraDto.getIntersection()).isNull();
		} else {
			Intersection intersection = camera.getIntersection();
			IntersectionDto intersectionDto = cameraDto.getIntersection();

			assertThat(intersection.getIntersectionNo()).isEqualTo(intersectionDto.getIntersectionNo());
			assertThat(intersection.getIntersectionName()).isEqualTo(intersectionDto.getIntersectionName());
			assertThat(intersection.getNationalId()).isEqualTo(intersectionDto.getNationalId());

			if (intersection.getLat() == null) {
				assertThat(intersectionDto.getGps()).isNull();
			} else {
				assertThat(intersection.getLat()).isEqualTo(intersectionDto.getGps().getLat());
				assertThat(intersection.getLng()).isEqualTo(intersectionDto.getGps().getLng());
			}

			if (intersection.getRegion() == null) {
				assertThat(intersectionDto.getRegion()).isNull();
			} else {
				assertThat(intersection.getRegion().getRegionNo()).isEqualTo(intersectionDto.getRegion().getRegionNo());
				assertThat(intersection.getRegion().getRegionName()).isEqualTo(intersectionDto.getRegion().getRegionName());
				assertThat(intersectionDto.getRegion().getGps()).isNull();
				assertThat(intersectionDto.getRegion().getIntersections()).isNull();
			}
		}

		if (camera.getDirection() == null) {
			assertThat(cameraDto.getDirection()).isNull();
		} else {
			Intersection intersection = camera.getDirection();
			IntersectionDto intersectionDto = cameraDto.getDirection();

			assertThat(intersection.getIntersectionNo()).isEqualTo(intersectionDto.getIntersectionNo());
			assertThat(intersection.getIntersectionName()).isEqualTo(intersectionDto.getIntersectionName());
			assertThat(intersection.getNationalId()).isEqualTo(intersectionDto.getNationalId());

			if (intersection.getLat() == null) {
				assertThat(intersectionDto.getGps()).isNull();
			} else {
				assertThat(intersection.getLat()).isEqualTo(intersectionDto.getGps().getLat());
				assertThat(intersection.getLng()).isEqualTo(intersectionDto.getGps().getLng());
			}

			if (intersection.getRegion() == null) {
				assertThat(intersectionDto.getRegion()).isNull();
			} else {
				assertThat(intersection.getRegion().getRegionNo()).isEqualTo(intersectionDto.getRegion().getRegionNo());
				assertThat(intersection.getRegion().getRegionName()).isEqualTo(intersectionDto.getRegion().getRegionName());
				assertThat(intersectionDto.getRegion().getGps()).isNull();
				assertThat(intersectionDto.getRegion().getIntersections()).isNull();
			}
		}

		if (camera.getLat() == null) {
			assertThat(cameraDto.getGps()).isNull();
		} else {
			assertThat(camera.getLat()).isEqualTo(cameraDto.getGps().getLat());
			assertThat(camera.getLng()).isEqualTo(cameraDto.getGps().getLng());
		}

		assertThat(camera.getDistance()).isEqualTo(cameraDto.getDistance());
		assertThat(camera.getRtspUrl()).isEqualTo(cameraDto.getRtspUrl());
		assertThat(camera.getRtspId()).isEqualTo(cameraDto.getRtspId());
		assertThat(camera.getRtspPassword()).isEqualTo(cameraDto.getRtspPassword());
		assertThat(camera.getServerUrl()).isEqualTo(cameraDto.getServerUrl());
		assertThat(camera.getSendCycle()).isEqualTo(cameraDto.getSendCycle());
		assertThat(camera.getCollectCycle()).isEqualTo(cameraDto.getCollectCycle());
		assertThat(camera.getSmallWidth()).isEqualTo(cameraDto.getSmallWidth());
		assertThat(camera.getSmallHeight()).isEqualTo(cameraDto.getSmallHeight());
		assertThat(camera.getLargeWidth()).isEqualTo(cameraDto.getLargeWidth());
		assertThat(camera.getLargeHeight()).isEqualTo(cameraDto.getLargeHeight());
		assertThat(camera.getDegree()).isEqualTo(cameraDto.getDegree());
		assertThat(camera.getSettingsUpdated()).isEqualTo(cameraDto.getSettingsUpdated());
		assertThat(camera.getLastDataTime()).isEqualTo(cameraDto.getLastDataTime());

		if (camera.getRoad() == null) {
			assertThat(cameraDto.getRoad()).isNull();
		} else {
			assertThat(camera.getRoad().getStartLine()).isEqualTo(cameraDto.getRoad().getStartLine());
			assertThat(camera.getRoad().getLane()).isEqualTo(jsonMapper.writeValueAsString(cameraDto.getRoad().getLane()));
			assertThat(camera.getRoad().getUturn()).isEqualTo(cameraDto.getRoad().getUturn());
			assertThat(camera.getRoad().getCrosswalk()).isEqualTo(cameraDto.getRoad().getCrosswalk());
			assertThat(camera.getRoad().getDirection()).isEqualTo(jsonMapper.writeValueAsString(cameraDto.getRoad().getDirection()));
		}
	}

	private void assertMapTo(CameraDto cameraDto, Camera camera) throws JsonProcessingException {

		assertThat(camera.getCameraId()).isNull();
		assertThat(cameraDto.getCameraNo()).isEqualTo(camera.getCameraNo());
		assertThat(cameraDto.getPassword()).isEqualTo(camera.getPassword());

		if (cameraDto.getIntersection() == null) {
			assertThat(camera.getIntersection()).isNull();
		} else {
			Intersection intersection = camera.getIntersection();
			IntersectionDto intersectionDto = cameraDto.getIntersection();

			assertThat(intersection.getIntersectionId()).isNull();
			assertThat(intersection.getIntersectionNo()).isEqualTo(intersectionDto.getIntersectionNo());
			assertThat(intersection.getIntersectionName()).isNull();
			assertThat(intersection.getLat()).isNull();
			assertThat(intersection.getLng()).isNull();
			assertThat(intersection.getRegion()).isNull();
			assertThat(intersection.getNationalId()).isNull();
		}

		if (cameraDto.getDirection() == null) {
			assertThat(camera.getDirection()).isNull();
		} else {
			Intersection intersection = camera.getDirection();
			IntersectionDto intersectionDto = cameraDto.getDirection();

			assertThat(intersection.getIntersectionId()).isNull();
			assertThat(intersection.getIntersectionNo()).isEqualTo(intersectionDto.getIntersectionNo());
			assertThat(intersection.getIntersectionName()).isNull();
			assertThat(intersection.getLat()).isNull();
			assertThat(intersection.getLng()).isNull();
			assertThat(intersection.getRegion()).isNull();
			assertThat(intersection.getNationalId()).isNull();
		}

		if (cameraDto.getGps() == null) {
			assertThat(camera.getLat()).isNull();
			assertThat(camera.getLng()).isNull();
		} else {
			assertThat(cameraDto.getGps().getLat()).isEqualTo(camera.getLat());
			assertThat(cameraDto.getGps().getLng()).isEqualTo(camera.getLng());
		}

		assertThat(cameraDto.getDistance()).isEqualTo(camera.getDistance());
		assertThat(cameraDto.getRtspUrl()).isEqualTo(camera.getRtspUrl());
		assertThat(cameraDto.getRtspId()).isEqualTo(camera.getRtspId());
		assertThat(cameraDto.getRtspPassword()).isEqualTo(camera.getRtspPassword());
		assertThat(cameraDto.getServerUrl()).isEqualTo(camera.getServerUrl());
		assertThat(cameraDto.getSendCycle()).isEqualTo(camera.getSendCycle());
		assertThat(cameraDto.getCollectCycle()).isEqualTo(camera.getCollectCycle());
		assertThat(cameraDto.getSmallWidth()).isEqualTo(camera.getSmallWidth());
		assertThat(cameraDto.getSmallHeight()).isEqualTo(camera.getSmallHeight());
		assertThat(cameraDto.getLargeWidth()).isEqualTo(camera.getLargeWidth());
		assertThat(cameraDto.getLargeHeight()).isEqualTo(camera.getLargeHeight());
		assertThat(cameraDto.getDegree()).isEqualTo(camera.getDegree());
		assertThat(cameraDto.getSettingsUpdated()).isEqualTo(camera.getSettingsUpdated());
		assertThat(cameraDto.getLastDataTime()).isEqualTo(camera.getLastDataTime());

		if (cameraDto.getRoad() == null) {
			assertThat(camera.getRoad()).isNull();
		} else {
			assertThat(camera.getRoad().getCameraRoadId()).isNull();
			assertThat(camera.getRoad().getCamera()).isNull();
			assertThat(cameraDto.getRoad().getStartLine()).isEqualTo(camera.getRoad().getStartLine());
			assertThat(jsonMapper.writeValueAsString(cameraDto.getRoad().getLane())).isEqualTo(camera.getRoad().getLane());
			assertThat(cameraDto.getRoad().getUturn()).isEqualTo(camera.getRoad().getUturn());
			assertThat(cameraDto.getRoad().getCrosswalk()).isEqualTo(camera.getRoad().getCrosswalk());
			assertThat(jsonMapper.writeValueAsString(cameraDto.getRoad().getDirection())).isEqualTo(camera.getRoad().getDirection());
		}
	}
}
