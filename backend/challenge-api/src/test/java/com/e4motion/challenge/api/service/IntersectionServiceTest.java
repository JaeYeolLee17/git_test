package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.common.exception.customexception.IntersectionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IntersectionServiceTest {

	@Autowired
	RegionService regionService;

	@Autowired
	IntersectionService intersectionService;

	@Test
	public void create() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);

		IntersectionDto found = intersectionService.get(intersectionDto1.getIntersectionNo());
		assertThat(found).isNotNull();
		assertEquals(found, intersectionDto1);

		// duplicated intersection no
		Exception ex = assertThrows(IntersectionDuplicateException.class, () ->intersectionService.create(intersectionDto1));
		assertThat(ex.getMessage()).isEqualTo(IntersectionDuplicateException.INTERSECTION_NO_ALREADY_EXISTS);

		// invalid region no
		IntersectionDto intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionDto2.getRegion().setRegionNo("R99");

		ex = assertThrows(RegionNotFoundException.class, () ->intersectionService.create(intersectionDto2));
		assertThat(ex.getMessage()).isEqualTo(RegionNotFoundException.INVALID_REGION_NO);
    }

	@Test
   	public void update() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();
		regionService.create(regionDto2);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);

		IntersectionDto found = intersectionService.get(intersectionDto1.getIntersectionNo());
		assertThat(found).isNotNull();
		assertEquals(found, intersectionDto1);

		// nothing will be updated.
		IntersectionDto updateIntersectionDto1 = IntersectionDto.builder()
				.intersectionNo(null)
				.intersectionName(null)
				.gps(null)
				.region(null)
				.nationalId(null)
				.build();
		intersectionService.update(intersectionDto1.getIntersectionNo(), updateIntersectionDto1);
		found = intersectionService.get(intersectionDto1.getIntersectionNo());
		assertThat(found).isNotNull();
		assertEquals(found, intersectionDto1);

		// everything will be updated.
		updateIntersectionDto1 = IntersectionDto.builder()
				.intersectionNo("I0099")
				.intersectionName("수정이름")
				.gps(GpsDto.builder().latitude(35.000000).longitude(127.000000).build())
				.region(regionDto2)
				.nationalId(15000001L)
				.build();
		intersectionService.update(intersectionDto1.getIntersectionNo(), updateIntersectionDto1);
		found = intersectionService.get(updateIntersectionDto1.getIntersectionNo());
		assertThat(found).isNotNull();
		assertEquals(found, updateIntersectionDto1);

		// invalid intersection no
		Exception ex = assertThrows(IntersectionNotFoundException.class, () ->intersectionService.update("I0088", intersectionDto1));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);

		// invalid region no
		updateIntersectionDto1.getRegion().setRegionNo("R88");
		IntersectionDto finalUpdateIntersectionDto = updateIntersectionDto1;	// it should be final for lambda

		ex = assertThrows(RegionNotFoundException.class, () ->intersectionService.update(finalUpdateIntersectionDto.getIntersectionNo(), finalUpdateIntersectionDto));
		assertThat(ex.getMessage()).isEqualTo(RegionNotFoundException.INVALID_REGION_NO);
    }

	@Test
   	public void delete() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		regionService.create(regionDto1);

		IntersectionDto intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);

		IntersectionDto created = intersectionService.get(intersectionDto1.getIntersectionNo());
		assertThat(created).isNotNull();

		intersectionService.delete(intersectionDto1.getIntersectionNo());
		IntersectionDto found = intersectionService.get(intersectionDto1.getIntersectionNo());
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

		List<IntersectionDto> founds = intersectionService.getList(null);
		assertThat(founds.size()).isEqualTo(2);
		assertThat(founds.get(0).getIntersectionNo()).isEqualTo(intersectionDto1.getIntersectionNo());
		assertThat(founds.get(1).getIntersectionNo()).isEqualTo(intersectionDto2.getIntersectionNo());

		founds = intersectionService.getList(intersectionDto1.getRegion().getRegionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getIntersectionNo()).isEqualTo(intersectionDto1.getIntersectionNo());

		founds = intersectionService.getList(intersectionDto2.getRegion().getRegionNo());
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getIntersectionNo()).isEqualTo(intersectionDto2.getIntersectionNo());

		founds = intersectionService.getList("R99");
		assertThat(founds.size()).isEqualTo(0);

		// disorder by no.
		intersectionService.delete(intersectionDto1.getIntersectionNo());
		intersectionService.delete(intersectionDto2.getIntersectionNo());

		intersectionDto2 = TestDataHelper.getIntersectionDto2();
		intersectionService.create(intersectionDto2);
		intersectionDto1 = TestDataHelper.getIntersectionDto1();
		intersectionService.create(intersectionDto1);

		founds = intersectionService.getList(null);
		assertThat(founds.size()).isEqualTo(2);
		assertThat(founds.get(0).getIntersectionNo()).isEqualTo(intersectionDto1.getIntersectionNo());	// ordered by no.
		assertThat(founds.get(1).getIntersectionNo()).isEqualTo(intersectionDto2.getIntersectionNo());

    }

	private void assertEquals(IntersectionDto intersectionDto1, IntersectionDto intersectionDto2) {

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
