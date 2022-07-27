package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.exception.customexception.LinkDuplicateException;
import com.e4motion.challenge.common.exception.customexception.LinkNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LinkServiceTest {

	@Autowired
	RegionService regionService;

	@Autowired
	IntersectionService intersectionService;

	@Autowired
	LinkService linkService;

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


		LinkDto linkDto1 = TestDataHelper.getLinkDto1();
		LinkDto created = linkService.create(linkDto1);

		LinkDto found = linkService.get(created.getLinkId());
		assertThat(found).isNotNull();
		assertEquals(found, created);

		// duplicated start, end intersection no
		Exception ex = assertThrows(LinkDuplicateException.class, () ->linkService.create(linkDto1));
		assertThat(ex.getMessage()).isEqualTo(LinkDuplicateException.LINK_START_END_ALREADY_EXISTS);

		// invalid intersection no
		LinkDto linkDto2 = TestDataHelper.getLinkDto1();
		linkDto2.getStart().setIntersectionNo("I0099");

		ex = assertThrows(IntersectionNotFoundException.class, () ->linkService.create(linkDto2));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);

		LinkDto linkDto3 = TestDataHelper.getLinkDto1();
		linkDto3.getEnd().setIntersectionNo("I0088");

		ex = assertThrows(IntersectionNotFoundException.class, () ->linkService.create(linkDto3));
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

		LinkDto linkDto1 = TestDataHelper.getLinkDto1();
		LinkDto created = linkService.create(linkDto1);

		LinkDto found = linkService.get(created.getLinkId());
		assertThat(found).isNotNull();
		assertEquals(found, created);

		// nothing will be updated.
		LinkDto updateLinkDto1 = LinkDto.builder()
				.start(null)
				.end(null)
				.gps(null)
				.build();
		linkService.update(found.getLinkId(), updateLinkDto1);
		found = linkService.get(found.getLinkId());
		assertThat(found).isNotNull();
		assertEquals(found, linkDto1);

		// everything will be updated.
		List<GpsDto> gps = new ArrayList<>();
		gps.add(GpsDto.builder().lat(35.55).lng(128.55).build());
		gps.add(GpsDto.builder().lat(35.66).lng(128.66).build());
		gps.add(GpsDto.builder().lat(35.77).lng(128.77).build());

		updateLinkDto1 = LinkDto.builder()
				.start(intersectionDto2)
				.end(intersectionDto1)
				.gps(gps)
				.build();
		linkService.update(found.getLinkId(), updateLinkDto1);
		found = linkService.get(found.getLinkId());
		assertThat(found).isNotNull();
		assertEquals(found, updateLinkDto1);

		// invalid link id
		Exception ex = assertThrows(LinkNotFoundException.class, () ->linkService.update(100L, linkDto1));
		assertThat(ex.getMessage()).isEqualTo(LinkNotFoundException.INVALID_LINK_ID);

		// invalid intersection no
		updateLinkDto1.getStart().setIntersectionNo("I0099");

		LinkDto finalFound = found;
		LinkDto finalUpdateLinkDto = updateLinkDto1;
		ex = assertThrows(IntersectionNotFoundException.class, () -> linkService.update(finalFound.getLinkId(), finalUpdateLinkDto));
		assertThat(ex.getMessage()).isEqualTo(IntersectionNotFoundException.INVALID_INTERSECTION_NO);

		updateLinkDto1.getEnd().setIntersectionNo("I0088");

		LinkDto finalFound2 = found;
		LinkDto finalUpdateLinkDto2 = updateLinkDto1;
		ex = assertThrows(IntersectionNotFoundException.class, () -> linkService.update(finalFound2.getLinkId(), finalUpdateLinkDto2));
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

		LinkDto linkDto1 = TestDataHelper.getLinkDto1();
		LinkDto created = linkService.create(linkDto1);

		LinkDto found = linkService.get(created.getLinkId());
		assertThat(found).isNotNull();

		linkService.delete(found.getLinkId());
		found = linkService.get(created.getLinkId());
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

		LinkDto linkDto1 = TestDataHelper.getLinkDto1();
		linkService.create(linkDto1);

		List<LinkDto> founds = linkService.getList();
		assertThat(founds.size()).isEqualTo(1);
		assertThat(founds.get(0).getStart().getIntersectionNo()).isEqualTo(linkDto1.getStart().getIntersectionNo());
		assertThat(founds.get(0).getEnd().getIntersectionNo()).isEqualTo(linkDto1.getEnd().getIntersectionNo());
    }

	private void assertEquals(LinkDto linkDto1, LinkDto linkDto2) {

		assertThat(linkDto1.getStart().getIntersectionNo()).isEqualTo(linkDto2.getStart().getIntersectionNo());
		assertThat(linkDto1.getStart().getIntersectionName()).isEqualTo(linkDto1.getStart().getIntersectionName());
		assertThat(linkDto1.getStart().getNationalId()).isEqualTo(linkDto2.getStart().getNationalId());
		assertThat(linkDto1.getEnd().getIntersectionNo()).isEqualTo(linkDto2.getEnd().getIntersectionNo());
		assertThat(linkDto1.getEnd().getIntersectionName()).isEqualTo(linkDto1.getEnd().getIntersectionName());
		assertThat(linkDto1.getEnd().getNationalId()).isEqualTo(linkDto2.getEnd().getNationalId());
		
		assertEquals(linkDto1.getGps(), linkDto2.getGps());
	}

	private void assertEquals(List<GpsDto> gpsDto1, List<GpsDto> gpsDto2) {

		if (gpsDto1 == null) {
			assertThat(gpsDto2).isNull();
			return;
		}

		int i = 0;
		for (GpsDto gps : gpsDto2) {
			assertThat(gps.getLat()).isEqualTo(gpsDto2.get(i).getLat());
			assertThat(gps.getLng()).isEqualTo(gpsDto2.get(i).getLng());
			i++;
		}
	}
}
