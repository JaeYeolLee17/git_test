package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.repository.*;
import com.e4motion.challenge.api.service.UploadService;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

    private final static String TEXT_CSV = "text/csv";
    private final CameraRepository cameraRepository;
    private final PasswordEncoder passwordEncoder;
    private final IntersectionRepository intersectionRepository;
    private final DataStatsRepository dataStatsRepository;
    private final RegionRepository regionRepository;
    private final LinkRepository linkRepository;


    @Transactional
    public void uploadCamera(MultipartFile file) throws IOException {

        Iterable<CSVRecord> records = parseCsv(file, CameraHeaders.class);
        if (records == null) {
            return;
        }

        String password = passwordEncoder.encode("camera12!@");
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Camera> cameras = new ArrayList<>();

        for (CSVRecord record : records) {

            Camera camera = Camera.builder()
                    .cameraNo(getString(record, CameraHeaders.camera_no))
                    .password(password)
                    .intersection(getIntersection(getString(record, CameraHeaders.intersection_no)))
                    .direction(getIntersection(getString(record, CameraHeaders.direction_no)))
                    .lat(getDouble(record, CameraHeaders.lat))
                    .lng(getDouble(record, CameraHeaders.lng))
                    .distance(getInteger(record, CameraHeaders.distance))
                    .rtspUrl(getString(record, CameraHeaders.rtsp_url))
                    .rtspId(getString(record, CameraHeaders.rtsp_id))
                    .rtspPassword(getString(record, CameraHeaders.rtsp_password))
                    .serverUrl(getString(record, CameraHeaders.server_url))
                    .sendCycle(getInteger(record, CameraHeaders.send_cycle))
                    .collectCycle(getInteger(record, CameraHeaders.collect_cycle))
                    .smallWidth(getInteger(record, CameraHeaders.s_width))
                    .smallHeight(getInteger(record, CameraHeaders.s_height))
                    .largeWidth(getInteger(record, CameraHeaders.l_width))
                    .largeHeight(getInteger(record, CameraHeaders.l_height))
                    .degree(getInteger(record, CameraHeaders.degree))
                    .settingsUpdated(true)
                    .lastDataTime(null)
                    .build();

            String lane = null;
            String l = getString(record, CameraHeaders.lane);
            if (l != null) {
                lane = mapper.writeValueAsString(mapper.readValue(l.replace("{", "[").replace("}", "]"), String[].class));
            }

            String direction = null;
            String d = getString(record, CameraHeaders.direction);
            if (d != null) {
                direction = mapper.writeValueAsString(mapper.readValue(d.replace("{", "[").replace("}", "]"), Boolean[][].class));
            }

            camera.setRoad(CameraRoad.builder()
                    .camera(camera)
                    .startLine(record.get(CameraHeaders.start_line))
                    .lane(lane)
                    .uturn(record.get(CameraHeaders.uturn))
                    .crosswalk(record.get(CameraHeaders.crosswalk))
                    .direction(direction)
                    .build());

            cameras.add(camera);
        }

        cameraRepository.saveAll(cameras);
    }

    @Transactional
    public void uploadDataStats(MultipartFile[] files) throws IOException, ParseException {

        for (MultipartFile file : files) {

            Iterable<CSVRecord> records = parseCsv(file, DataStatsHeaders.class);
            if (records == null) {
                continue;
            }

            ArrayList<DataStats> dataStats = new ArrayList<>();

            for (CSVRecord record : records) {
                dataStats.add(DataStats.builder()
                        .t(DateTimeHelper.parseLocalDateTime(record.get(DataStatsHeaders.time)))
                        .c(record.get(DataStatsHeaders.camera_no))
                        .i(record.get(DataStatsHeaders.intersection_no))
                        .r(record.get(DataStatsHeaders.region_no))
                        .p(Integer.parseInt(record.get(DataStatsHeaders.p)))
                        .sr0(Integer.parseInt(record.get(DataStatsHeaders.sr0)))
                        .sr1(Integer.parseInt(record.get(DataStatsHeaders.sr1)))
                        .sr2(Integer.parseInt(record.get(DataStatsHeaders.sr2)))
                        .sr3(Integer.parseInt(record.get(DataStatsHeaders.sr3)))
                        .sr4(Integer.parseInt(record.get(DataStatsHeaders.sr4)))
                        .qmsrLen(Integer.parseInt(record.get(DataStatsHeaders.qmsr_len)))
                        .qmsr0(Integer.parseInt(record.get(DataStatsHeaders.qmsr0)))
                        .qmsr1(Integer.parseInt(record.get(DataStatsHeaders.qmsr1)))
                        .qmsr2(Integer.parseInt(record.get(DataStatsHeaders.qmsr2)))
                        .qmsr3(Integer.parseInt(record.get(DataStatsHeaders.qmsr3)))
                        .qmsr4(Integer.parseInt(record.get(DataStatsHeaders.qmsr4)))
                        .qtsr0(Integer.parseInt(record.get(DataStatsHeaders.qtsr0)))
                        .qtsr1(Integer.parseInt(record.get(DataStatsHeaders.qtsr1)))
                        .qtsr2(Integer.parseInt(record.get(DataStatsHeaders.qtsr2)))
                        .qtsr3(Integer.parseInt(record.get(DataStatsHeaders.qtsr3)))
                        .qtsr4(Integer.parseInt(record.get(DataStatsHeaders.qtsr4)))
                        .lu0(Integer.parseInt(record.get(DataStatsHeaders.lu0)))
                        .lu1(Integer.parseInt(record.get(DataStatsHeaders.lu1)))
                        .lu2(Integer.parseInt(record.get(DataStatsHeaders.lu2)))
                        .lu3(Integer.parseInt(record.get(DataStatsHeaders.lu3)))
                        .lu4(Integer.parseInt(record.get(DataStatsHeaders.lu4)))
                        .qmluLen(Integer.parseInt(record.get(DataStatsHeaders.qmlu_len)))
                        .qmlu0(Integer.parseInt(record.get(DataStatsHeaders.qmlu0)))
                        .qmlu1(Integer.parseInt(record.get(DataStatsHeaders.qmlu1)))
                        .qmlu2(Integer.parseInt(record.get(DataStatsHeaders.qmlu2)))
                        .qmlu3(Integer.parseInt(record.get(DataStatsHeaders.qmlu3)))
                        .qmlu4(Integer.parseInt(record.get(DataStatsHeaders.qmlu4)))
                        .qtlu0(Integer.parseInt(record.get(DataStatsHeaders.qtlu0)))
                        .qtlu1(Integer.parseInt(record.get(DataStatsHeaders.qtlu1)))
                        .qtlu2(Integer.parseInt(record.get(DataStatsHeaders.qtlu2)))
                        .qtlu3(Integer.parseInt(record.get(DataStatsHeaders.qtlu3)))
                        .qtlu4(Integer.parseInt(record.get(DataStatsHeaders.qtlu4)))
                        .qtT(Integer.parseInt(record.get(DataStatsHeaders.qt_time)))
                        .build());
            }

            dataStatsRepository.saveAll(dataStats);
        }
    }

    @Transactional
    public void uploadRegion(MultipartFile file) throws IOException, ParseException {
        Iterable<CSVRecord> records = parseCsv(file, RegionHeaders.class);
        if (records == null) {
            return;
        }

        ArrayList<RegionGps> regionGps = new ArrayList<>();
        AtomicInteger gpsOrder = new AtomicInteger(0);
        Region region = new Region();

        String regionNo = "";
        String regionName = "";
        String lastRegionNo = "";

        for (CSVRecord record : records) {

            if (getString(record, RegionHeaders.region_no) != null) {
                regionNo = getString(record, RegionHeaders.region_no);
                regionName = getString(record, RegionHeaders.region_name);
                regionGps.clear();
            }

            Optional<Region> savedRegion = regionRepository.findByRegionNo(regionNo);

            if (savedRegion.isPresent()) {
                region = savedRegion.get();

                if (Objects.equals(regionNo, getString(record, RegionHeaders.region_no))) {
                    region.getGps().clear();
                    regionRepository.saveAndFlush(region);
                }
            } else {
                region = Region.builder()
                        .regionNo(regionNo)
                        .regionName(regionName)
                        .intersections(getIntersectionList(regionNo))
                        .build();
                region.setGps(regionGps);
            }

            if (!Objects.equals(lastRegionNo, regionNo)) {
                gpsOrder = new AtomicInteger(0);
            }

            region.getGps().add(RegionGps.builder()
                    .region(region)
                    .lat(getDouble(record, RegionHeaders.lat))
                    .lng(getDouble(record, RegionHeaders.lng))
                    .gpsOrder(gpsOrder.incrementAndGet())
                    .build());
            regionRepository.save(region);

            lastRegionNo = regionNo;
        }
    }

    @Transactional
    public void uploadLink(MultipartFile file) throws IOException, ParseException {
        Iterable<CSVRecord> records = parseCsv(file, LinkHeaders.class);
        if (records == null) {
            return;
        }

        ArrayList<LinkGps> linkGps = new ArrayList<>();
        AtomicInteger gpsOrder = new AtomicInteger(0);
        Link link = new Link();

        String startNo = "";
        String endNo = "";
        String lastStratNo = "";

        for (CSVRecord record : records) {
            if (getString(record, LinkHeaders.start_no) != null) {
                startNo = getString(record, LinkHeaders.start_no);
                endNo = getString(record, LinkHeaders.end_no);
                linkGps.clear();
            }
            Optional<Link> savedLink = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(startNo, endNo);

            if (savedLink.isPresent()) {
                link = savedLink.get();

                if (Objects.equals(startNo, getString(record, LinkHeaders.start_no))) {
                    link.getGps().clear();
                    linkRepository.saveAndFlush(link);
                }
            } else {
                link = link.builder()
                        .start(getIntersection(startNo))
                        .end(getIntersection(endNo))
                        .build();
                link.setGps(linkGps);
            }

            if (!Objects.equals(lastStratNo, startNo)) {
                gpsOrder = new AtomicInteger(0);
            }

            link.getGps().add(LinkGps.builder()
                    .link(link)
                    .lat(getDouble(record, LinkHeaders.lat))
                    .lng(getDouble(record, LinkHeaders.lng))
                    .gpsOrder(gpsOrder.incrementAndGet())
                    .build());
            linkRepository.save(link);

            lastStratNo = startNo;
        }
    }

    private String getString(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return s;
        }
        return null;
    }

    private Integer getInteger(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return Integer.parseInt(s);
        }
        return null;
    }

    private Double getDouble(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return Double.parseDouble(s);
        }
        return null;
    }

    private Intersection getIntersection(String intersectionNo) {

        if (intersectionNo != null) {
            return intersectionRepository.findByIntersectionNo(intersectionNo)
                    .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
        }
        return null;
    }

    private List<Intersection> getIntersectionList(String regionNo) {

        if (regionNo != null) {
            return intersectionRepository.findAllByRegion_RegionNo(regionNo, Sort.sort(Intersection.class));
        }
        return null;
    }

    private CSVParser parseCsv(MultipartFile file, Class<? extends Enum<?>> headers) throws IOException {

        if (!TEXT_CSV.equals(file.getContentType())) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
    }

    private enum CameraHeaders {
        camera_no,
        intersection_no,
        lat,
        lng,
        direction_no,
        rtsp_url,
        server_url,
        collect_cycle,
        password,
        rtsp_id,
        rtsp_password,
        send_cycle,
        distance,
        settings_updated,
        last_data_time,
        s_width,
        s_height,
        l_width,
        l_height,
        degree,
        start_line,
        uturn,
        crosswalk,
        lane,
        direction
    }

    private enum DataStatsHeaders {
        time,
        camera_no,
        intersection_no,
        region_no,
        p,
        sr0,
        sr1,
        sr2,
        sr3,
        sr4,
        qmsr_len,
        qmsr0,
        qmsr1,
        qmsr2,
        qmsr3,
        qmsr4,
        qtsr0,
        qtsr1,
        qtsr2,
        qtsr3,
        qtsr4,
        lu0,
        lu1,
        lu2,
        lu3,
        lu4,
        qmlu_len,
        qmlu0,
        qmlu1,
        qmlu2,
        qmlu3,
        qmlu4,
        qtlu0,
        qtlu1,
        qtlu2,
        qtlu3,
        qtlu4,
        qt_time
    }

    private enum RegionHeaders {
        region_no,
        region_name,
        lat,
        lng
    }

    private enum LinkHeaders {
        start_no,
        end_no,
        start_name,
        end_name,
        lat,
        lng
    }
}
