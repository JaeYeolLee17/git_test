package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.DataStats;
import com.e4motion.challenge.api.repository.DataStatsRepository;
import com.e4motion.challenge.api.service.UploadService;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadServiceImpl implements UploadService {

    private final static String TEXT_CSV = "text/csv";

    private final DataStatsRepository dataStatsRepository;

    @Transactional
    public void uploadDataStats(MultipartFile file) throws IOException, ParseException {

        List<CSVRecord> records = parseCsv(file);

        ArrayList<DataStats> dataStats = new ArrayList<>();

        for(CSVRecord record : records) {
            AtomicInteger index = new AtomicInteger();
            dataStats.add(DataStats.builder()
                    .t(DateTimeHelper.parseLocalDateTime(record.get(index.getAndIncrement())))
                    .c(record.get(index.getAndIncrement()))
                    .i(record.get(index.getAndIncrement()))
                    .r(record.get(index.getAndIncrement()))
                    .p(Integer.parseInt(record.get(index.getAndIncrement())))
                    .sr0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .sr1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .sr2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .sr3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .sr4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsrLen(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsr0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsr1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsr2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsr3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmsr4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtsr0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtsr1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtsr2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtsr3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtsr4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .lu0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .lu1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .lu2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .lu3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .lu4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmluLen(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmlu0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmlu1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmlu2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmlu3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qmlu4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtlu0(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtlu1(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtlu2(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtlu3(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtlu4(Integer.parseInt(record.get(index.getAndIncrement())))
                    .qtT(Integer.parseInt(record.get(index.getAndIncrement())))
                    .build());
        }
        dataStatsRepository.saveAll(dataStats);
    }

    private List<CSVRecord> parseCsv(MultipartFile file) throws IOException {

        if (!TEXT_CSV.equals(file.getContentType())) {
            return new ArrayList<>();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        return parser.getRecords().stream().skip(1).collect(toList());  // without header.
    }
}
