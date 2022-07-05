package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.RegionDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionMapperTest {

	RegionMapper mapper = Mappers.getMapper(RegionMapper.class);

	@Test
	public void toStringRegion() {

		Region region = TestDataHelper.getRegion1();
		assertThat(region.toString()).isNotNull();
	}

	@Test
	public void toStringRegionDto() {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		assertThat(regionDto.toString()).isNotNull();
	}

	@Test
    public void toRegionDto() {

		Region region = TestDataHelper.getRegion1();

		RegionDto regionDto = mapper.toRegionDto(region);
		assertMapTo(region, regionDto);

		// without region gps.
		region.setGps(null);

		regionDto = mapper.toRegionDto(region);
		assertMapTo(region, regionDto);

		// with intersections.
		List<Intersection> intersections = new ArrayList<>();
		Intersection intersection1 = TestDataHelper.getIntersection1();
		intersection1.setLatitude(null);
		intersection1.setLongitude(null);
		intersections.add(intersection1);	// an intersection without gps.
		intersections.add(TestDataHelper.getIntersection2());
		region.setIntersections(intersections);

		regionDto = mapper.toRegionDto(region);
		assertMapTo(region, regionDto);
	}

	@Test
	public void toRegion() {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		Region region = mapper.toRegion(regionDto);
		assertMapTo(regionDto, region);
	}

	private void assertMapTo(Region region, RegionDto regionDto) {

		assertThat(region.getRegionNo()).isEqualTo(regionDto.getRegionNo());
		assertThat(region.getRegionName()).isEqualTo(regionDto.getRegionName());

		if (region.getGps() == null) {
			assertThat(regionDto.getGps()).isNull();
		} else {
			int i = 0;
			for (RegionGps gps : region.getGps()) {
				assertThat(gps.getLatitude()).isEqualTo(regionDto.getGps().get(i).getLatitude());
				assertThat(gps.getLongitude()).isEqualTo(regionDto.getGps().get(i).getLongitude());
				i++;
			}
		}

		if (region.getIntersections() == null) {
			assertThat(region.getIntersections()).isNull();
		} else {
			int i = 0;
			for (Intersection intersection : region.getIntersections()) {
				assertThat(intersection.getIntersectionNo()).isEqualTo(regionDto.getIntersections().get(i).getIntersectionNo());
				assertThat(intersection.getIntersectionName()).isEqualTo(regionDto.getIntersections().get(i).getIntersectionName());
				if (intersection.getLatitude() == null) {
					assertThat(regionDto.getIntersections().get(i).getGps()).isNull();
				} else {
					assertThat(intersection.getLatitude()).isEqualTo(regionDto.getIntersections().get(i).getGps().getLatitude());
					assertThat(intersection.getLongitude()).isEqualTo(regionDto.getIntersections().get(i).getGps().getLongitude());
				}
				assertThat(regionDto.getIntersections().get(i).getRegion()).isNull();
				assertThat(intersection.getNationalId()).isEqualTo(regionDto.getIntersections().get(i).getNationalId());
				i++;
			}
		}
	}

	private void assertMapTo(RegionDto regionDto, Region region) {

		assertThat(region.getRegionId()).isNull();
		assertThat(regionDto.getRegionNo()).isEqualTo(region.getRegionNo());
		assertThat(regionDto.getRegionName()).isEqualTo(region.getRegionName());
		assertThat(region.getGps()).isNull();
		assertThat(region.getIntersections()).isNull();
	}
}
