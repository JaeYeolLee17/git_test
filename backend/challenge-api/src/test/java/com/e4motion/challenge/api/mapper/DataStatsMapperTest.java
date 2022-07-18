package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.DataStats;
import com.e4motion.challenge.api.dto.DataStatsDto;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataStatsMapperTest {

	DataStatsMapper mapper = Mappers.getMapper(DataStatsMapper.class);
	
	@Test
    public void toDataStatsDto() throws ParseException {
		
		DataStats stats = getDataStats1();
		
		DataStatsDto StatDto = mapper.toDataStatsDto(stats);

		assertMapTo(stats, StatDto);
	}
	
	@Test
    public void toUserDtoList() throws ParseException {

		DataStats stats1 = getDataStats1();
		DataStats stats2 = getDataStats2();

		List<DataStats> stats = new ArrayList<>();
		stats.add(stats1);
		stats.add(stats2);
		
		List<DataStatsDto> statsDtos = mapper.toDataStatsDto(stats);
		assertThat(statsDtos.size()).isEqualTo(2);
		assertMapTo(stats.get(0), statsDtos.get(0));
		assertMapTo(stats.get(1), statsDtos.get(1));
	}

	private void assertMapTo(DataStats stats, DataStatsDto statsDto) {

		assertThat(statsDto.getT()).isEqualTo(stats.getT());
		assertThat(statsDto.getC()).isEqualTo(stats.getC());
		assertThat(statsDto.getI()).isEqualTo(stats.getI());
		assertThat(statsDto.getR()).isEqualTo(stats.getR());
		assertThat(statsDto.getSr()[0]).isEqualTo(stats.getSr0());
		assertThat(statsDto.getSr()[1]).isEqualTo(stats.getSr1());
		assertThat(statsDto.getSr()[2]).isEqualTo(stats.getSr2());
		assertThat(statsDto.getSr()[3]).isEqualTo(stats.getSr3());
		assertThat(statsDto.getSr()[4]).isEqualTo(stats.getSr4());
		assertThat(statsDto.getQmsrLen()).isEqualTo(stats.getQmsrLen());
		assertThat(statsDto.getQmsr()[0]).isEqualTo(stats.getQmsr0());
		assertThat(statsDto.getQmsr()[1]).isEqualTo(stats.getQmsr1());
		assertThat(statsDto.getQmsr()[2]).isEqualTo(stats.getQmsr2());
		assertThat(statsDto.getQmsr()[3]).isEqualTo(stats.getQmsr3());
		assertThat(statsDto.getQmsr()[4]).isEqualTo(stats.getQmsr4());
		assertThat(statsDto.getQtsr()[0]).isEqualTo(stats.getQtsr0());
		assertThat(statsDto.getQtsr()[1]).isEqualTo(stats.getQtsr1());
		assertThat(statsDto.getQtsr()[2]).isEqualTo(stats.getQtsr2());
		assertThat(statsDto.getQtsr()[3]).isEqualTo(stats.getQtsr3());
		assertThat(statsDto.getQtsr()[4]).isEqualTo(stats.getQtsr4());
		assertThat(statsDto.getLu()[0]).isEqualTo(stats.getLu0());
		assertThat(statsDto.getLu()[1]).isEqualTo(stats.getLu1());
		assertThat(statsDto.getLu()[2]).isEqualTo(stats.getLu2());
		assertThat(statsDto.getLu()[3]).isEqualTo(stats.getLu3());
		assertThat(statsDto.getLu()[4]).isEqualTo(stats.getLu4());
		assertThat(statsDto.getQmluLen()).isEqualTo(stats.getQmluLen());
		assertThat(statsDto.getQmlu()[0]).isEqualTo(stats.getQmlu0());
		assertThat(statsDto.getQmlu()[1]).isEqualTo(stats.getQmlu1());
		assertThat(statsDto.getQmlu()[2]).isEqualTo(stats.getQmlu2());
		assertThat(statsDto.getQmlu()[3]).isEqualTo(stats.getQmlu3());
		assertThat(statsDto.getQmlu()[4]).isEqualTo(stats.getQmlu4());
		assertThat(statsDto.getQtlu()[0]).isEqualTo(stats.getQtlu0());
		assertThat(statsDto.getQtlu()[1]).isEqualTo(stats.getQtlu1());
		assertThat(statsDto.getQtlu()[2]).isEqualTo(stats.getQtlu2());
		assertThat(statsDto.getQtlu()[3]).isEqualTo(stats.getQtlu3());
		assertThat(statsDto.getQtlu()[4]).isEqualTo(stats.getQtlu4());
	}

	private DataStats getDataStats1() throws ParseException {

		return DataStats.builder()
				.id(1L)
				.t(DateTimeHelper.parseLocalDateTime("2022-07-13 12:00:00"))
				.c("C0001")
				.i("I0001")
				.r("R01")
				.sr0(1)
				.sr1(2)
				.sr2(3)
				.sr3(4)
				.sr4(5)
				.qmsrLen(10)
				.qmsr0(11)
				.qmsr1(12)
				.qmsr2(13)
				.qmsr3(14)
				.qmsr4(15)
				.qtsr0(21)
				.qtsr1(22)
				.qtsr2(23)
				.qtsr3(24)
				.qtsr4(25)
				.lu0(51)
				.lu1(52)
				.lu2(53)
				.lu3(54)
				.lu4(55)
				.qmluLen(60)
				.qmlu0(61)
				.qmlu1(62)
				.qmlu2(63)
				.qmlu3(64)
				.qmlu4(65)
				.qtlu0(71)
				.qtlu1(72)
				.qtlu2(73)
				.qtlu3(74)
				.qtlu4(75)
				.qtT(100)
				.build();
	}

	private DataStats getDataStats2() throws ParseException {

		return DataStats.builder()
				.id(1L)
				.t(DateTimeHelper.parseLocalDateTime("2022-07-13 12:15:00"))
				.c("C0002")
				.i("I0002")
				.r("R02")
				.sr0(101)
				.sr1(102)
				.sr2(103)
				.sr3(104)
				.sr4(105)
				.qmsrLen(110)
				.qmsr0(111)
				.qmsr1(112)
				.qmsr2(113)
				.qmsr3(114)
				.qmsr4(115)
				.qtsr0(121)
				.qtsr1(122)
				.qtsr2(123)
				.qtsr3(124)
				.qtsr4(125)
				.lu0(151)
				.lu1(152)
				.lu2(153)
				.lu3(154)
				.lu4(155)
				.qmluLen(160)
				.qmlu0(161)
				.qmlu1(162)
				.qmlu2(163)
				.qmlu3(164)
				.qmlu4(165)
				.qtlu0(171)
				.qtlu1(172)
				.qtlu2(173)
				.qtlu3(174)
				.qtlu4(175)
				.qtT(200)
				.build();
	}
}
