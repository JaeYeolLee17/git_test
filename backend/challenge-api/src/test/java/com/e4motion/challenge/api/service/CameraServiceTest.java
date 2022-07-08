package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.common.exception.customexception.CameraDuplicateException;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CameraServiceTest {

	@Autowired
	RegionService regionService;

	@Autowired
	IntersectionService intersectionService;

	@Autowired
	CameraService cameraService;

	@Autowired
	CameraRepository cameraRepository;

	@Test
	public void create() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();
		regionService.create(regionDto2);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionService.create(intersectionDto2);

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		cameraService.create(cameraDto1);

		CameraDto found = cameraService.get(cameraDto1.getCameraNo());
		assertThat(found).isNotNull();
		assertEquals(found, cameraDto1);
		assertThat(found.getSettingsUpdated()).isTrue();

		// duplicated camera no
		Exception ex = assertThrows(CameraDuplicateException.class, () ->cameraService.create(cameraDto1));
		assertThat(ex.getMessage()).isEqualTo(CameraDuplicateException.CAMERA_NO_ALREADY_EXISTS);

		// invalid intersection no
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();
		cameraDto2.getIntersection().setIntersectionNo("I0099");

		ex = assertThrows(IntersectionNotFoundException.class, () ->cameraService.create(cameraDto2));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);

		CameraDto cameraDto2_2 = TestDataHelper.getCameraDto2();
		cameraDto2_2.getIntersection().setIntersectionNo("I0099");

		ex = assertThrows(IntersectionNotFoundException.class, () ->cameraService.create(cameraDto2_2));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);
    }

	@Test
   	public void update() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();
		regionService.create(regionDto2);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionService.create(intersectionDto2);

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		cameraService.create(cameraDto1);

		CameraDto found = cameraService.get(cameraDto1.getCameraNo());
		assertThat(found).isNotNull();
		assertEquals(found, cameraDto1);
		assertThat(found.getSettingsUpdated()).isTrue();

		// reset settings updated
		cameraRepository.findByCameraNo(found.getCameraNo())
				.ifPresent(camera -> {
					camera.setSettingsUpdated(false);
					cameraRepository.saveAndFlush(camera);
				});

		// nothing will be updated.
		CameraDto updateCameraDto1 = CameraDto.builder()
				.cameraNo(null)
				.password(null)
				.intersection(null)
				.direction(null)
				.gps(null)
				.distance(null)
				.rtspUrl(null)
				.rtspId(null)
				.rtspPassword(null)
				.serverUrl(null)
				.sendCycle(null)
				.collectCycle(null)
				.smallWidth(null)
				.smallHeight(null)
				.largeWidth(null)
				.largeHeight(null)
				.degree(null)
				.lastDataTime(null)
				.build();
		cameraService.update(cameraDto1.getCameraNo(), updateCameraDto1);
		found = cameraService.get(cameraDto1.getCameraNo());
		assertThat(found).isNotNull();
		assertEquals(found, cameraDto1);
		assertThat(found.getSettingsUpdated()).isFalse();

		// everything will be updated.
		updateCameraDto1 = CameraDto.builder()
				.cameraNo("C0099")
				.password("camera12!@")
				.intersection(intersectionDto2)
				.direction(intersectionDto1)
				.gps(GpsDto.builder().latitude(35.999999).longitude(127.888).build())
				.distance(200)
				.rtspUrl("rtsp://...")
				.rtspId("id")
				.rtspPassword("password")
				.serverUrl("http://...")
				.sendCycle(180)
				.collectCycle(30)
				.smallWidth(320)
				.smallHeight(240)
				.largeWidth(1920)
				.largeHeight(1280)
				.degree(90)
				.lastDataTime(LocalDateTime.parse("2022-07-01T00:00:00"))
				.road(CameraRoadDto.builder()
						.startLine("aaa")
						.lane(new String[] {})
						.uturn("bbb")
						.crosswalk("ccc")
						.direction(new Boolean[][]{new Boolean[]{}, new Boolean[]{}}).build())
				.build();
		cameraService.update(cameraDto1.getCameraNo(), updateCameraDto1);
		found = cameraService.get(updateCameraDto1.getCameraNo());
		assertThat(found).isNotNull();
		assertEquals(found, updateCameraDto1);
		assertThat(found.getSettingsUpdated()).isTrue();

		// reset settings updated
		cameraRepository.findByCameraNo(updateCameraDto1.getCameraNo())
				.ifPresent(camera -> {
					camera.setSettingsUpdated(false);
					cameraRepository.saveAndFlush(camera);
				});

		// settings updated false
		CameraDto updateCameraDto2 = CameraDto.builder()
				.smallWidth(320)
				.smallHeight(240)
				.largeWidth(1920)
				.largeHeight(1280)
				.degree(90)
				.lastDataTime(LocalDateTime.parse("2022-07-01T00:00:00"))
				.build();
		cameraService.update(updateCameraDto1.getCameraNo(), updateCameraDto2);
		found = cameraService.get(updateCameraDto1.getCameraNo());
		assertThat(found).isNotNull();
		assertThat(found.getSettingsUpdated()).isFalse();

		// invalid camera no
		Exception ex = assertThrows(CameraNotFoundException.class, () ->cameraService.update("C0088", updateCameraDto2));
		assertThat(ex.getMessage()).isEqualTo(CameraNotFoundException.INVALID_CAMERA_NO);

		// invalid intersection no
		updateCameraDto2.setIntersection(IntersectionDto.builder().intersectionNo("I0088").build());
		CameraDto finalUpdateCameraDto1 = updateCameraDto1;

		ex = assertThrows(IntersectionNotFoundException.class, () ->cameraService.update(finalUpdateCameraDto1.getCameraNo(), updateCameraDto2));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);

		updateCameraDto2.setDirection(IntersectionDto.builder().intersectionNo("I0088").build());
		CameraDto finalUpdateCameraDto2 = updateCameraDto1;

		ex = assertThrows(IntersectionNotFoundException.class, () ->cameraService.update(finalUpdateCameraDto2.getCameraNo(), updateCameraDto2));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);
    }

	@Test
   	public void delete() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();
		regionService.create(regionDto2);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionService.create(intersectionDto2);

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		cameraService.create(cameraDto1);

		CameraDto found = cameraService.get(cameraDto1.getCameraNo());
		assertThat(found).isNotNull();

		cameraService.delete(cameraDto1.getCameraNo());

		found = cameraService.get(cameraDto1.getCameraNo());
		assertThat(found).isNull();
    }

	@Test
   	public void getList() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();
		regionService.create(regionDto2);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionService.create(intersectionDto2);

		CameraDto cameraDto1 = TestDataHelper.getCameraDto1();
		cameraService.create(cameraDto1);
		CameraDto cameraDto2 = TestDataHelper.getCameraDto2();
		cameraService.create(cameraDto2);

		List<CameraDto> founds = cameraService.getList(null, null);
		assertThat(founds.size()).isEqualTo(2);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto1.getCameraNo());
		assertThat(founds.get(1).getCameraNo()).isEqualTo(cameraDto2.getCameraNo());

		founds = cameraService.getList(regionDto1.getRegionNo(), intersectionDto1.getIntersectionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto1.getCameraNo());
		founds = cameraService.getList(regionDto1.getRegionNo(), null);
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto1.getCameraNo());
		founds = cameraService.getList(null, intersectionDto1.getIntersectionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto1.getCameraNo());

		founds = cameraService.getList(regionDto2.getRegionNo(), intersectionDto2.getIntersectionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto2.getCameraNo());
		founds = cameraService.getList(regionDto2.getRegionNo(), null);
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto2.getCameraNo());
		founds = cameraService.getList(null, intersectionDto2.getIntersectionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getCameraNo()).isEqualTo(cameraDto2.getCameraNo());

		founds = cameraService.getList("R99", "I0099");
		assertThat(founds.size()).isEqualTo(0);
    }

	private void assertEquals(CameraDto cameraDto1, CameraDto cameraDto2) {

		assertThat(cameraDto1.getCameraNo()).isEqualTo(cameraDto2.getCameraNo());
		assertThat(cameraDto1.getPassword()).isNull();

		assertEquals(cameraDto1.getIntersection(), cameraDto2.getIntersection());
		assertEquals(cameraDto1.getDirection(), cameraDto2.getDirection());
		assertEquals(cameraDto1.getGps(), cameraDto2.getGps());

		assertThat(cameraDto1.getDistance()).isEqualTo(cameraDto2.getDistance());
		assertThat(cameraDto1.getRtspUrl()).isEqualTo(cameraDto2.getRtspUrl());
		assertThat(cameraDto1.getRtspId()).isEqualTo(cameraDto2.getRtspId());
		assertThat(cameraDto1.getRtspPassword()).isEqualTo(cameraDto2.getRtspPassword());
		assertThat(cameraDto1.getServerUrl()).isEqualTo(cameraDto2.getServerUrl());
		assertThat(cameraDto1.getSendCycle()).isEqualTo(cameraDto2.getSendCycle());
		assertThat(cameraDto1.getCollectCycle()).isEqualTo(cameraDto2.getCollectCycle());
		assertThat(cameraDto1.getSmallWidth()).isEqualTo(cameraDto2.getSmallWidth());
		assertThat(cameraDto1.getSmallHeight()).isEqualTo(cameraDto2.getSmallHeight());
		assertThat(cameraDto1.getLargeWidth()).isEqualTo(cameraDto2.getLargeWidth());
		assertThat(cameraDto1.getLargeHeight()).isEqualTo(cameraDto2.getLargeHeight());
		assertThat(cameraDto1.getDegree()).isEqualTo(cameraDto2.getDegree());
		assertThat(cameraDto1.getLastDataTime()).isEqualTo(cameraDto2.getLastDataTime());

		// settings updated 항목은 서버에서 설정되는 값이므로 비교하지 않는다.

		if (cameraDto1.getRoad() == null) {
			assertThat(cameraDto2.getRoad()).isNull();
		} else {
			assertThat(cameraDto1.getRoad().getStartLine()).isEqualTo(cameraDto2.getRoad().getStartLine());
			assertThat(cameraDto1.getRoad().getLane()).isEqualTo(cameraDto2.getRoad().getLane());
			assertThat(cameraDto1.getRoad().getUturn()).isEqualTo(cameraDto2.getRoad().getUturn());
			assertThat(cameraDto1.getRoad().getCrosswalk()).isEqualTo(cameraDto2.getRoad().getCrosswalk());
			assertThat(cameraDto1.getRoad().getDirection()).isEqualTo(cameraDto2.getRoad().getDirection());
		}
	}

	private void assertEquals(IntersectionDto intersectionDto1, IntersectionDto intersectionDto2) {

		if (intersectionDto1 == null) {
			assertThat(intersectionDto2).isNull();
			return;
		}

		assertThat(intersectionDto1.getIntersectionNo()).isEqualTo(intersectionDto2.getIntersectionNo());
		assertThat(intersectionDto1.getIntersectionName()).isEqualTo(intersectionDto2.getIntersectionName());
		assertThat(intersectionDto1.getNationalId()).isEqualTo(intersectionDto2.getNationalId());

		assertEquals(intersectionDto1.getGps(), intersectionDto2.getGps());
		assertEquals(intersectionDto1.getRegion(), intersectionDto2.getRegion());
	}

	private void assertEquals(GpsDto gpsDto1, GpsDto gpsDto2) {

		if (gpsDto1 == null) {
			assertThat(gpsDto2).isNull();
			return;
		}

		assertThat(gpsDto1.getLatitude()).isEqualTo(gpsDto2.getLatitude());
		assertThat(gpsDto1.getLongitude()).isEqualTo(gpsDto2.getLongitude());
	}

	private void assertEquals(RegionDto regionDto1, RegionDto regionDto2) {

		if (regionDto1 == null) {
			assertThat(regionDto2).isNull();
			return;
		}

		assertThat(regionDto1.getRegionNo()).isEqualTo(regionDto2.getRegionNo());
		assertThat(regionDto1.getRegionName()).isEqualTo(regionDto2.getRegionName());

		// Intersection 내 Region Gps 목록 정보는 포함하지 않는다.
	}
}
