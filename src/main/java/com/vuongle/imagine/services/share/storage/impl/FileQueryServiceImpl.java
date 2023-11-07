package com.vuongle.imagine.services.share.storage.impl;

import com.vuongle.imagine.constants.CommonFileType;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.File;
import com.vuongle.imagine.repositories.FileRepository;
import com.vuongle.imagine.services.share.storage.FileQueryService;
import com.vuongle.imagine.services.share.storage.query.FileQuery;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FileQueryServiceImpl implements FileQueryService {

    private final FileRepository fileRepository;

    private final MongoTemplate mongoTemplate;

    private final List<String> IMAGE_FILE_TYPES = List.of("jpg", "png", "jpeg", "gif", "bmp", "svg");

    private final List<String> VIDEO_FILE_TYPES = List.of("mp4", "mkv", "flv");

    private final List<String> COMPRESS_FILE_TYPES = List.of("zip", "rar", "7z", "gz");

    private final List<String> DOCUMENT_FILE_TYPES = List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx");

    private final List<String> AUDIO_FILE_TYPES = List.of("mp3", "wav");

    public FileQueryServiceImpl(
            FileRepository fileRepository,
            MongoTemplate mongoTemplate
    ) {
        this.fileRepository = fileRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Value("${imagine.root.file.path}")
    private String IMAGING_ROOT_FILE_PATH;

    @Override
    public Resource download(String path) throws IOException {
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            throw new DataNotFoundException("File not found");
        }

        return new ByteArrayResource(Files.readAllBytes(filePath));
    }

    @Override
    public Resource download(ObjectId fileId) throws IOException {
        File fileInfo = fileRepository.findById(fileId).orElse(null);
        if (Objects.isNull(fileInfo)) {
            throw new DataNotFoundException("File not found");
        }

        return new ByteArrayResource(Files.readAllBytes(Paths.get(fileInfo.getPath())));
    }

    @Override
    public <T> Optional<T> findById(ObjectId id, Class<T> classType) {
        return Optional.ofNullable(mongoTemplate.findById(id, classType));
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> classType) {
        return findById(id, classType).orElseThrow(() -> new DataNotFoundException("Not found data has id " + id));
    }

    @Override
    public <T> Page<T> findPage(FileQuery fileQuery, Pageable pageable, Class<T> returnType) {

        Query query = createQuery(fileQuery, pageable);

        List<T> data = mongoTemplate.find(query, returnType);

        return PageableExecutionUtils.getPage(
                data,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), returnType)
        );
    }

    @Override
    public <T> Page<T> findPage(FileQuery fileQuery, Pageable pageable, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public <T> List<T> findList(FileQuery fileQuery, Class<T> returnType) {
        Query query = createQuery(fileQuery);

        return mongoTemplate.find(query, returnType);
    }

    @Override
    public <T> List<T> findList(FileQuery fileQuery, Class<T> returnType, AggregationOperation... aggregationOperationInputs) {
        return null;
    }

    @Override
    public Query createQuery(FileQuery fileQuery, Pageable pageable) {
        Query query = new Query();

        if (Objects.nonNull(pageable)) {
            query.with(pageable);
        }

        query.addCriteria(createCriteria(fileQuery));

        return query;
    }

    @Override
    public Query createQuery(FileQuery fileQuery) {
        return createQuery(fileQuery, null);
    }

    @Override
    public long countByQuery(FileQuery fileQuery) {
        return 0;
    }

    @Override
    public long countByQuery(FileQuery fileQuery, AggregationOperation... aggregationOperationInputs) {
        return 0;
    }

    @Override
    public Criteria createCriteria(FileQuery fileQuery) {

        Criteria criteria = new Criteria();

        List<Criteria> listAndCriteria = new ArrayList<>();

        if (Objects.nonNull(fileQuery.getId())) {
            listAndCriteria.add(Criteria.where("_id").is(fileQuery.getId()));
        }

        if (Objects.nonNull(fileQuery.getType())) {
            switch (fileQuery.getType()) {
                case AUDIO -> {
                    listAndCriteria.add(Criteria.where("extension").in(AUDIO_FILE_TYPES));
                }
                case IMAGE -> {
                    listAndCriteria.add(Criteria.where("extension").in(IMAGE_FILE_TYPES));
                }
                case VIDEO -> {
                    listAndCriteria.add(Criteria.where("extension").in(VIDEO_FILE_TYPES));
                }
                case COMPRESS -> {
                    listAndCriteria.add(Criteria.where("extension").in(COMPRESS_FILE_TYPES));
                }
                case DOCUMENT -> {
                    listAndCriteria.add(Criteria.where("extension").in(DOCUMENT_FILE_TYPES));
                }
                default -> {

                    List<String> fileTypes = new ArrayList<>();
                    fileTypes.addAll(IMAGE_FILE_TYPES);
                    fileTypes.addAll(AUDIO_FILE_TYPES);
                    fileTypes.addAll(VIDEO_FILE_TYPES);
                    fileTypes.addAll(COMPRESS_FILE_TYPES);
                    fileTypes.addAll(DOCUMENT_FILE_TYPES);

                    listAndCriteria.add(Criteria.where("extension").nin(fileTypes));
                }
            }
        }

        if (Objects.nonNull(fileQuery.getLikeName())) {
            listAndCriteria.add(Criteria.where("fileName").regex(fileQuery.getLikeName(), "i"));
        }

        if (Objects.nonNull(fileQuery.getChecksum())) {
            listAndCriteria.add(Criteria.where("checksum").is(fileQuery.getChecksum()));
        }

        if (!listAndCriteria.isEmpty()) {
            criteria.andOperator(listAndCriteria.toArray(new Criteria[0]));
        }

        return criteria;
    }
}
