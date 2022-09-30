package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.IntersectionDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class IntersectionMapperTest {

	IntersectionMapper mapper = Mappers.getMapper(IntersectionMapper.class);

	@Test
    public void toIntersectionDto() {

		Intersection intersection = TestDataHelper.getIntersection1();

		IntersectionDto intersectionDto = mapper.toIntersectionDto(intersection);
		assertMapTo(intersection, intersectionDto);

		// without intersection gps.
		intersection.setLat(null);
		intersection.setLng(null);

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

	private void assertMapTo(IntersectionDto intersectionDto, Intersection intersection) {

		assertThat(intersection.getIntersectionId()).isNull();
		assertThat(intersectionDto.getIntersectionNo()).isEqualTo(intersection.getIntersectionNo());
		assertThat(intersectionDto.getIntersectionName()).isEqualTo(intersection.getIntersectionName());
		assertThat(intersectionDto.getNationalId()).isEqualTo(intersection.getNationalId());

		if (intersectionDto.getGps() == null) {
			assertThat(intersection.getLat()).isNull();
			assertThat(intersection.getLng()).isNull();
		} else {
			assertThat(intersectionDto.getGps().getLat()).isEqualTo(intersection.getLat());
			assertThat(intersectionDto.getGps().getLng()).isEqualTo(intersection.getLng());
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
