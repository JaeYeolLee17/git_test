package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.IntersectionDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class IntersectionMapperTest {

	IntersectionMapper mapper = Mappers.getMapper(IntersectionMapper.class);

	@Test
	public void toStringIntersection() {

		Intersection intersection = TestDataHelper.getIntersection1();
		assertThat(intersection.toString()).isNotNull();
	}

	@Test
	public void toStringIntersectionDto() {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
		assertThat(intersectionDto.toString()).isNotNull();
	}

	@Test
    public void toIntersectionDto() {

		Intersection intersection = TestDataHelper.getIntersection1();

		IntersectionDto intersectionDto = mapper.toIntersectionDto(intersection);
		assertMapTo(intersection, intersectionDto);

		// without intersection gps.
		intersection.setLatitude(null);
		intersection.setLongitude(null);

		intersectionDto = mapper.toIntersectionDto(intersection);
		assertMapTo(intersection, intersectionDto);

		// without region's gps.
		intersection.getRegion().setGps(null);

		intersectionDto = mapper.toIntersectionDto(intersection);
		assertMapTo(intersection, intersectionDto);

		// without region
		intersection.setRegion(null);

		intersectionDto = mapper.toIntersectionDto(intersection);
		assertMapTo(intersection, intersectionDto);
	}

	@Test
	public void toIntersection() {

		IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();

		Intersection intersection = mapper.toIntersection(intersectionDto);
		assertMapTo(intersectionDto, intersection);

		// without intersection gps.
		intersectionDto.setGps(null);

		intersection = mapper.toIntersection(intersectionDto);
		assertMapTo(intersectionDto, intersection);

		// without region
		intersectionDto.setRegion(null);

		intersection = mapper.toIntersection(intersectionDto);
		assertMapTo(intersectionDto, intersection);
	}

	private void assertMapTo(Intersection intersection, IntersectionDto intersectionDto) {

		assertThat(intersection.getIntersectionNo()).isEqualTo(intersectionDto.getIntersectionNo());
		assertThat(intersection.getIntersectionName()).isEqualTo(intersectionDto.getIntersectionName());
		assertThat(intersection.getNationalId()).isEqualTo(intersectionDto.getNationalId());

		if (intersection.getLatitude() == null) {
			assertThat(intersectionDto.getGps()).isNull();
		} else {
			assertThat(intersection.getLatitude()).isEqualTo(intersectionDto.getGps().getLatitude());
			assertThat(intersection.getLongitude()).isEqualTo(intersectionDto.getGps().getLongitude());
		}

		if (intersection.getRegion() == null) {
			assertThat(intersectionDto.getRegion()).isNull();
		} else {
			assertThat(intersection.getRegion().getRegionNo()).isEqualTo(intersectionDto.getRegion().getRegionNo());
			assertThat(intersection.getRegion().getRegionName()).isEqualTo(intersectionDto.getRegion().getRegionName());

			if (intersection.getRegion().getGps() == null) {
				assertThat(intersection.getRegion().getGps()).isNull();
			} else {
				int i = 0;
				for (RegionGps gps : intersection.getRegion().getGps()) {
					assertThat(gps.getLatitude()).isEqualTo(intersectionDto.getRegion().getGps().get(i).getLatitude());
					assertThat(gps.getLongitude()).isEqualTo(intersectionDto.getRegion().getGps().get(i).getLongitude());
					i++;
				}
			}
			assertThat(intersectionDto.getRegion().getIntersections()).isNull();
		}
	}

	private void assertMapTo(IntersectionDto intersectionDto, Intersection intersection) {

		assertThat(intersection.getIntersectionId()).isNull();
		assertThat(intersectionDto.getIntersectionNo()).isEqualTo(intersection.getIntersectionNo());
		assertThat(intersectionDto.getIntersectionName()).isEqualTo(intersection.getIntersectionName());
		assertThat(intersectionDto.getNationalId()).isEqualTo(intersection.getNationalId());

		if (intersectionDto.getGps() == null) {
			assertThat(intersection.getLatitude()).isNull();
			assertThat(intersection.getLongitude()).isNull();
		} else {
			assertThat(intersectionDto.getGps().getLatitude()).isEqualTo(intersection.getLatitude());
			assertThat(intersectionDto.getGps().getLongitude()).isEqualTo(intersection.getLongitude());
		}

		if (intersectionDto.getRegion() == null) {
			assertThat(intersection.getRegion()).isNull();
		} else {
			assertThat(intersection.getRegion().getRegionId()).isNull();
			assertThat(intersectionDto.getRegion().getRegionNo()).isEqualTo(intersection.getRegion().getRegionNo());
			assertThat(intersection.getRegion().getRegionName()).isNull();
			assertThat(intersection.getRegion().getGps()).isNull();
			assertThat(intersection.getRegion().getIntersections()).isNull();
		}
	}
}
