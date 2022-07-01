package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionMapperTest {

	RegionMapper mapper = Mappers.getMapper(RegionMapper.class);
	
	@Test
    public void toRegionDto() {

		Region region = TestDataHelper.getRegion1();

		RegionDto regionDto = mapper.toRegionDto(region);

		assertEquals(region, regionDto);
	}

	@Test
	public void toRegion() {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		Region region = mapper.toRegion(regionDto);

		assertEquals(regionDto, region);
	}

	private void assertEquals(Region region, RegionDto regionDto) {

		assertThat(region.getRegionNo()).isEqualTo(regionDto.getRegionNo());
		assertThat(region.getRegionName()).isEqualTo(regionDto.getRegionName());
		// TODO: intersection
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
	}

	private void assertEquals(RegionDto regionDto, Region region) {

		assertThat(regionDto.getRegionNo()).isEqualTo(region.getRegionNo());
		assertThat(regionDto.getRegionName()).isEqualTo(region.getRegionName());
		assertThat(region.getGps()).isNull();
	}
}
