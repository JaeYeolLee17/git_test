package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.mapper.RegionMapper;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.impl.RegionServiceImpl;
import com.e4motion.challenge.common.exception.customexception.RegionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class RegionServiceTest {
	
	@Autowired
	RegionMapper regionMapper;
	
    @Mock
	RegionRepository regionRepository;

	@Mock
	EntityManager entityManager;

    RegionService regionService;
	
	@BeforeEach 
	void setup() { 
		regionService = new RegionServiceImpl(regionRepository, regionMapper, entityManager);
	}
    
	@Test
	public void create() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		Region newRegion = regionMapper.toRegion(regionDto);
		AtomicInteger order = new AtomicInteger();
		newRegion.setGps(regionDto.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(newRegion)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order.incrementAndGet())
						.build()).collect(Collectors.toList()));
		
		doReturn(Optional.empty()).when(regionRepository).findByRegionNo(regionDto.getRegionNo());
		doReturn(newRegion).when(regionRepository).save(any());

		RegionDto createdRegionDto = regionService.create(regionDto);

		assertThat(createdRegionDto).isNotNull();
		assertEquals(createdRegionDto, regionDto);
    }

	@Test
	public void create_withNullGps() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		regionDto.setGps(null);

		Region newRegion = regionMapper.toRegion(regionDto);

		doReturn(Optional.empty()).when(regionRepository).findByRegionNo(regionDto.getRegionNo());
		doReturn(newRegion).when(regionRepository).save(any());

		RegionDto createdRegionDto = regionService.create(regionDto);

		assertThat(createdRegionDto).isNotNull();
		assertEquals(createdRegionDto, regionDto);
	}

	@Test
	public void create_withEmptyGps() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto2();
		regionDto.getGps().clear();

		Region newRegion = regionMapper.toRegion(regionDto);
		AtomicInteger order = new AtomicInteger();
		newRegion.setGps(regionDto.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(newRegion)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order.incrementAndGet())
						.build()).collect(Collectors.toList()));

		doReturn(Optional.empty()).when(regionRepository).findByRegionNo(regionDto.getRegionNo());
		doReturn(newRegion).when(regionRepository).save(any());

		RegionDto createdRegionDto2 = regionService.create(regionDto);

		assertThat(createdRegionDto2).isNotNull();
		assertEquals(createdRegionDto2, regionDto);

	}

	@Test
	public void createDuplicateRegion() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		Region newRegion = regionMapper.toRegion(regionDto);
		AtomicInteger order = new AtomicInteger();
		newRegion.setGps(regionDto.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(newRegion)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order.incrementAndGet())
						.build()).collect(Collectors.toList()));

		doReturn(Optional.of(newRegion)).when(regionRepository).findByRegionNo(regionDto.getRegionNo());

		Exception ex = assertThrows(RegionDuplicateException.class, () -> regionService.create(regionDto));

		assertThat(ex.getMessage()).isEqualTo(RegionDuplicateException.REGION_NO_ALREADY_EXISTS);
    }

	@Test
   	public void update() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();
		regionDto.setRegionNo("R99");
		regionDto.setRegionName("수정구역");
		regionDto.getGps().clear();

		Region region = regionMapper.toRegion(regionDto);
		if (region.getGps() == null) {		// region from persistence has always gps list even if it's empty.
			region.setGps(((RegionServiceImpl)regionService).getRegionGps(regionDto, region));
		}

		doReturn(Optional.of(region)).when(regionRepository).findByRegionNo(regionDto.getRegionNo());
		doReturn(region).when(regionRepository).save(any());
		doNothing().when(entityManager).flush();

		RegionDto updateRegionDto = regionService.update(regionDto.getRegionNo(), regionDto);

		assertThat(updateRegionDto.getRegionNo()).isEqualTo("R99");
		assertThat(updateRegionDto.getRegionName()).isEqualTo("수정구역");
		assertThat(updateRegionDto.getGps().size()).isEqualTo(0);
    }

	@Test
   	public void updateNonexistentRegion() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto2();

		doReturn(Optional.empty()).when(regionRepository).findByRegionNo(regionDto.getRegionNo());

		Exception ex = assertThrows(RegionNotFoundException.class, () -> regionService.update(regionDto.getRegionNo(), regionDto));

		assertThat(ex.getMessage()).isEqualTo(RegionNotFoundException.INVALID_REGION_NO);
    }

	@Test
   	public void delete() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		doNothing().when(regionRepository).deleteByRegionNo(regionDto.getRegionNo());

		regionService.delete(regionDto.getRegionNo());
    }

	@Test
   	public void get() throws Exception {

		RegionDto regionDto = TestDataHelper.getRegionDto1();

		Region newRegion = regionMapper.toRegion(regionDto);
		AtomicInteger order = new AtomicInteger();
		newRegion.setGps(regionDto.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(newRegion)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order.incrementAndGet())
						.build()).collect(Collectors.toList()));

		doReturn(Optional.of(newRegion)).when(regionRepository).findByRegionNo(regionDto.getRegionNo());

		RegionDto foundRegionDto = regionService.get(regionDto.getRegionNo());

		assertEquals(foundRegionDto, regionDto);
    }

	@Test
   	public void getList() throws Exception {

		RegionDto regionDto1 = TestDataHelper.getRegionDto1();
		RegionDto regionDto2 = TestDataHelper.getRegionDto2();

		List<RegionDto> regionDtos = new ArrayList<>();
		regionDtos.add(regionDto1);
		regionDtos.add(regionDto2);

		Region region1 = regionMapper.toRegion(regionDto1);
		AtomicInteger order = new AtomicInteger();
		region1.setGps(regionDto1.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(region1)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order.incrementAndGet())
						.build()).collect(Collectors.toList()));

		Region region2 = regionMapper.toRegion(regionDto2);
		AtomicInteger order2 = new AtomicInteger();
		region2.setGps(regionDto2.getGps().stream().map(gps ->
				RegionGps.builder()
						.region(region2)
						.latitude(gps.getLatitude())
						.longitude(gps.getLongitude())
						.gpsOrder(order2.incrementAndGet())
						.build()).collect(Collectors.toList()));

		List<Region> regions = new ArrayList<>();
		regions.add(region1);
		regions.add(region2);

		Sort sort = Sort.by("regionNo").ascending();
		doReturn(regions).when(regionRepository).findAll(sort);

		List<RegionDto> foundRegionDtos = regionService.getList();

		assertThat(foundRegionDtos.size()).isEqualTo(regionDtos.size());
		assertEquals(foundRegionDtos.get(0), regionDtos.get(0));
		assertEquals(foundRegionDtos.get(1), regionDtos.get(1));
    }

	private void assertEquals(RegionDto regionDto1, RegionDto regionDto2) {

		assertThat(regionDto1.getRegionNo()).isEqualTo(regionDto2.getRegionNo());
		assertThat(regionDto1.getRegionName()).isEqualTo(regionDto2.getRegionName());

		if (regionDto1.getGps() == null) {
			assertThat(regionDto2.getGps()).isNull();
		} else {
			int i = 0;
			for (GpsDto gps : regionDto1.getGps()) {
				assertThat(gps.getLatitude()).isEqualTo(regionDto2.getGps().get(i).getLatitude());
				assertThat(gps.getLongitude()).isEqualTo(regionDto2.getGps().get(i).getLongitude());
				i++;
			}
		}
	}
}
