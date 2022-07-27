package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.domain.LinkGps;
import com.e4motion.challenge.api.dto.LinkDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkMapperTest {

	LinkMapper mapper = Mappers.getMapper(LinkMapper.class);

	@Test
	public void toStringLink() {

		Link link = TestDataHelper.getLink1();
		assertThat(link.toString()).isNotNull();
	}

	@Test
	public void toStringLinkDto() {

		LinkDto linkDto = TestDataHelper.getLinkDto1();
		assertThat(linkDto.toString()).isNotNull();
	}

	@Test
    public void toRegionDto() {

		Link link = TestDataHelper.getLink1();

		LinkDto linkDto = mapper.toLinkDto(link);
		assertMapTo(link, linkDto);

		// without region gps.
		link.setGps(null);

		linkDto = mapper.toLinkDto(link);
		assertMapTo(link, linkDto);
	}

	@Test
	public void toRegion() {

		LinkDto linkDto = TestDataHelper.getLinkDto1();

		Link link = mapper.toLink(linkDto);
		assertMapTo(linkDto, link);
	}

	private void assertMapTo(Link link, LinkDto linkDto) {

		assertThat(link.getStart().getIntersectionNo()).isEqualTo(linkDto.getStart().getIntersectionNo());
		assertThat(link.getStart().getIntersectionName()).isEqualTo(linkDto.getStart().getIntersectionName());
		assertThat(link.getEnd().getIntersectionNo()).isEqualTo(linkDto.getEnd().getIntersectionNo());
		assertThat(link.getEnd().getIntersectionName()).isEqualTo(linkDto.getEnd().getIntersectionName());

		if (link.getGps() == null) {
			assertThat(linkDto.getGps()).isNull();
		} else {
			int i = 0;
			for (LinkGps gps : link.getGps()) {
				assertThat(gps.getLat()).isEqualTo(linkDto.getGps().get(i).getLat());
				assertThat(gps.getLng()).isEqualTo(linkDto.getGps().get(i).getLng());
				i++;
			}
		}
	}

	private void assertMapTo(LinkDto linkDto, Link link) {

		assertThat(link.getLinkId()).isNull();
		assertThat(linkDto.getStart().getIntersectionNo()).isEqualTo(link.getStart().getIntersectionNo());
		assertThat(linkDto.getEnd().getIntersectionNo()).isEqualTo(link.getEnd().getIntersectionNo());
		assertThat(link.getGps()).isNull();
	}
}
