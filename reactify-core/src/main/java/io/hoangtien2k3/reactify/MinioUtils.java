/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify;

import io.hoangtien2k3.reactify.config.MinioProperties;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.activation.MimetypesFileTypeMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * <p>
 * MinioUtils class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "minio.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class MinioUtils {

    @Getter
    private final MinioProperties minioProperties;

    private final MinioClient minioClient;

    /** Constant <code>IMAGE_PNG_CONTENT_TYPE="image/png"</code> */
    public static final String IMAGE_PNG_CONTENT_TYPE = "image/png";
    /** Constant <code>IMAGE_JPEG_CONTENT_TYPE="image/jpeg"</code> */
    public static final String IMAGE_JPEG_CONTENT_TYPE = "image/jpeg";
    /** Constant <code>PDF_CONTENT_TYPE="application/pdf"</code> */
    public static final String PDF_CONTENT_TYPE = "application/pdf";
    /**
     * Constant
     * <code>XLSX_CONTENT_TYPE="application/vnd.openxmlformats-officedo"{trunked}</code>
     */
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    /**
     * Constant
     * <code>DOCX_CONTENT_TYPE="application/vnd.openxmlformats-officedo"{trunked}</code>
     */
    public static final String DOCX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    /** Constant <code>TEXT_CONTENT_TYPE="text/plain"</code> */
    public static final String TEXT_CONTENT_TYPE = "text/plain";
    /** Constant <code>VIDEO_CONTENT_TYPE="video/mp4"</code> */
    public static final String VIDEO_CONTENT_TYPE = "video/mp4";

    /** Constant <code>IMAGE_FOLDER="images/"</code> */
    public static final String IMAGE_FOLDER = "images/";
    /** Constant <code>PDF_FOLDER="pdf/"</code> */
    public static final String PDF_FOLDER = "pdf/";
    /** Constant <code>EXCEL_FOLDER="excel/"</code> */
    public static final String EXCEL_FOLDER = "excel/";
    /** Constant <code>WORD_FOLDER="word/"</code> */
    public static final String WORD_FOLDER = "word/";
    /** Constant <code>TEXT_FOLDER="text/"</code> */
    public static final String TEXT_FOLDER = "text/";
    /** Constant <code>VIDEO_FOLDER="videos/"</code> */
    public static final String VIDEO_FOLDER = "videos/";

    /** Constant <code>PNG_END=".png"</code> */
    public static final String PNG_END = ".png";
    /** Constant <code>JPEG_END=".jpeg"</code> */
    public static final String JPEG_END = ".jpeg";
    /** Constant <code>PDF_END=".pdf"</code> */
    public static final String PDF_END = ".pdf";
    /** Constant <code>EXCEL_END=".xlsx"</code> */
    public static final String EXCEL_END = ".xlsx";
    /** Constant <code>WORD_END=".docx"</code> */
    public static final String WORD_END = ".docx";
    /** Constant <code>TEXT_END=".txt"</code> */
    public static final String TEXT_END = ".txt";
    /** Constant <code>VIDEO_END=".mp4"</code> */
    public static final String VIDEO_END = ".mp4";
    /** Constant <code>FOLDER_MAP</code> */
    public static final Map<String, String> FOLDER_MAP;

    private static final String UPLOAD_SUCCESS_LOG = "Upload file successfully";

    static {
        FOLDER_MAP = Map.of(
                IMAGE_PNG_CONTENT_TYPE,
                IMAGE_FOLDER,
                IMAGE_JPEG_CONTENT_TYPE,
                IMAGE_FOLDER,
                VIDEO_CONTENT_TYPE,
                VIDEO_FOLDER,
                TEXT_CONTENT_TYPE,
                TEXT_FOLDER,
                PDF_CONTENT_TYPE,
                PDF_FOLDER,
                XLSX_CONTENT_TYPE,
                EXCEL_FOLDER,
                DOCX_CONTENT_TYPE,
                WORD_FOLDER);
    }

    /**
     * <p>
     * isBucketExist.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Boolean> isBucketExist(String bucketName) {
        return Mono.fromCallable(() -> minioClient.bucketExists(
                        BucketExistsArgs.builder().bucket(bucketName).build()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private void makeBucketSync(String bucketName) {
        try {
            log.info("Start making bucket with name {}", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Make bucket successfully");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * makeBucket.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> makeBucket(String bucketName) {
        return Mono.fromRunnable(() -> makeBucketSync(bucketName));
    }

    private void makeFolderSync(String bucketName, String folderName) {
        try {
            log.info("Start making folder with name {}", folderName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(folderName + "/").stream(
                            new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
            log.info("Make folder successfully");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * makeFolder.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param folderName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> makeFolder(String bucketName, String folderName) {
        return Mono.fromRunnable(() -> makeFolderSync(bucketName, folderName));
    }

    private String uploadFileToFolderSync(
            InputStream inputStream, String bucketName, String folderName, String contentType) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String objectName = UUID.randomUUID().toString().replace("-", ""); // dungbd5 remove dash
            String object = handleObjectName(contentType, objectName, folderName);
            log.info("Start upload file {} ", object);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(object).stream(
                            new ByteArrayInputStream(buffer), buffer.length, -1)
                    .contentType(contentType)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(object)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * <p>
     * uploadFile.
     * </p>
     *
     * @param inputStream
     *            a {@link InputStream} object
     * @param bucketName
     *            a {@link String} object
     * @param folderName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> uploadFile(InputStream inputStream, String bucketName, String folderName, String contentType) {
        return isBucketExist(bucketName).flatMap(existed -> {
            if (existed) {
                return Mono.fromCallable(() -> uploadFileToFolderSync(inputStream, bucketName, folderName, contentType))
                        .subscribeOn(Schedulers.boundedElastic());
            } else {
                return Mono.empty();
            }
        });
    }

    private String getFilePath(String contentType, String filename) {
        if (FOLDER_MAP.containsKey(contentType)) {
            return FOLDER_MAP.get(contentType) + filename;
        }
        return filename;
    }

    private void uploadSync(byte[] data, String bucketName, String fileName, String filePath, String contentType) {
        try {
            log.info("Start uploading {} ", fileName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(filePath).stream(
                            new ByteArrayInputStream(data), data.length, -1)
                    .contentType(contentType)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * upload.
     * </p>
     *
     * @param data
     *            an array of {@link byte} objects
     * @param fileName
     *            a {@link String} object
     * @param bucketName
     *            a {@link String} object
     * @param folderName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> upload(byte[] data, String fileName, String bucketName, String folderName, String contentType) {
        String filePath = folderName + "/" + getFilePath(contentType, fileName);
        return Mono.fromRunnable(() -> uploadSync(data, bucketName, fileName, filePath, contentType))
                .then(getObjectUrl(bucketName, filePath));
    }

    private String updateObjectSync(String bucket, String object, byte[] bytes) {
        try {
            log.info("Start update object {}", object);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(object).stream(
                            new ByteArrayInputStream(bytes), bytes.length, -1)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
            return object;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * updateObject.
     * </p>
     *
     * @param bucket
     *            a {@link String} object
     * @param object
     *            a {@link String} object
     * @param bytes
     *            an array of {@link byte} objects
     * @return a {@link Mono} object
     */
    public Mono<String> updateObject(String bucket, String object, byte[] bytes) {
        return Mono.fromCallable(() -> updateObjectSync(bucket, object, bytes))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * <p>
     * uploadFiles.
     * </p>
     *
     * @param inputStreamList
     *            a {@link List} object
     * @param bucketName
     *            a {@link String} object
     * @param folderName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Flux} object
     */
    public Flux<String> uploadFiles(
            List<InputStream> inputStreamList, String bucketName, String folderName, String contentType) {
        return Flux.fromIterable(inputStreamList)
                .flatMap(inputStream -> {
                    String objectName = uploadFileToFolderSync(inputStream, bucketName, folderName, contentType);
                    return getObjectUrl(bucketName, objectName);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private void downloadSync(String bucket, String object, FluxSink<String> stringFluxSink) {
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(object).build())) {
            log.info("bucket,object: {} {}", bucket, object);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                stringFluxSink.next(line);
            }
            stringFluxSink.complete();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            stringFluxSink.error(ex);
        }
    }

    /**
     * <p>
     * downloadFileByFilePath.
     * </p>
     *
     * @param bucket
     *            a {@link String} object
     * @param object
     *            a {@link String} object
     * @return a {@link Flux} object
     */
    public Flux<String> downloadFileByFilePath(String bucket, String object) {
        return Flux.create(stringFluxSink -> downloadSync(bucket, object, stringFluxSink));
    }

    private void downloadFileSync(String bucketName, String objectName, String fileName) {
        try {
            log.info("Start downloading object {}", objectName);
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(fileName)
                    .build());
            log.info("Download file successfully");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * downloadFile.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @param fileName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> downloadFile(String bucketName, String objectName, String fileName) {
        return Mono.fromRunnable(() -> downloadFileSync(bucketName, objectName, fileName));
    }

    /**
     * <p>
     * downloadFileBlock.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @param fileName
     *            a {@link String} object
     */
    public void downloadFileBlock(String bucketName, String objectName, String fileName) {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(fileName)
                    .build());
        } catch (Exception e) {
            log.error("dowload file error ", e);
        }
    }

    private ObjectWriteResponse copyObjectSync(String bucketName, String objectName) {
        try {
            log.info("Start downloading object {}", objectName);
            ObjectWriteResponse o = minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            log.info("Download file successfully");
            return o;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * copyObject.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<ObjectWriteResponse> copyObject(String bucketName, String objectName) {
        return Mono.fromCallable(() -> copyObjectSync(bucketName, objectName)).subscribeOn(Schedulers.boundedElastic());
    }

    private ObjectWriteResponse copyObjectSync(String bucketName, String sourceObject, String destinationObject) {
        try {
            log.info("Copy object from {} to {} ", sourceObject, destinationObject);
            ObjectWriteResponse o = minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(destinationObject)
                    .source(CopySource.builder()
                            .bucket(bucketName)
                            .object(sourceObject)
                            .build())
                    .build());
            log.info("Copy object successfully");
            return o;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * copyObject.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param sourceObject
     *            a {@link String} object
     * @param destinationObject
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<ObjectWriteResponse> copyObject(String bucketName, String sourceObject, String destinationObject) {
        return Mono.fromCallable(() -> copyObjectSync(bucketName, sourceObject, destinationObject))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * <p>
     * getObjectUrl.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> getObjectUrl(String bucketName, String objectName) {
        return isBucketExist(bucketName).flatMap(isBucketExist -> {
            if (isBucketExist) {
                String url = minioProperties.getBaseUrl() + "/" + bucketName + "/" + objectName;
                return Mono.just(url);
            } else {
                return Mono.error(new RuntimeException("Bucket not exist"));
            }
        });
    }

    /**
     * <p>
     * updateFile.
     * </p>
     *
     * @param filePath
     *            a {@link String} object
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> updateFile(String filePath, String bucketName, String objectName, String contentType) {
        return Mono.fromRunnable(() -> {
            try (InputStream in = new FileInputStream(filePath)) {
                byte[] imageBuffer = new byte[in.available()];
                in.read(imageBuffer);
                log.info("Start updating file with object name{}", objectName);
                minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                new ByteArrayInputStream(imageBuffer), imageBuffer.length, -1)
                        .contentType(contentType)
                        .build());
                log.info("Update file successfully ");
            } catch (Exception e) {
                log.error("update File error ", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * <p>
     * getObjectUrl.
     * </p>
     *
     * @param objectName
     *            a {@link String} object
     * @return a {@link String} object
     */
    public String getObjectUrl(String objectName) {
        return minioProperties.getBaseUrl() + "/" + minioProperties.getBucket() + "/" + objectName;
    }

    /**
     * <p>
     * handleObjectName.
     * </p>
     *
     * @param contentType
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @param folderName
     *            a {@link String} object
     * @return a {@link String} object
     */
    public String handleObjectName(String contentType, String objectName, String folderName) {
        switch (contentType) {
            case IMAGE_PNG_CONTENT_TYPE -> objectName = IMAGE_FOLDER + objectName + PNG_END;
            case IMAGE_JPEG_CONTENT_TYPE -> objectName = IMAGE_FOLDER + objectName + JPEG_END;
            case PDF_CONTENT_TYPE -> objectName = PDF_FOLDER + objectName + PDF_END;
            case XLSX_CONTENT_TYPE -> objectName = EXCEL_FOLDER + objectName + EXCEL_END;
            case DOCX_CONTENT_TYPE -> objectName = WORD_FOLDER + objectName + WORD_END;
            case TEXT_CONTENT_TYPE -> objectName = TEXT_FOLDER + objectName + TEXT_END;
            case VIDEO_CONTENT_TYPE -> objectName = VIDEO_FOLDER + objectName + VIDEO_END;
            default -> objectName = objectName;
        }
        if (folderName != null && !folderName.isEmpty()) {
            objectName = folderName + "/" + objectName;
        }
        return objectName;
    }

    private String getPrivateObjectUrlSync(String bucketName, String objectName) {
        try {
            log.info("Start get private object url with bucket name {} and object name = {}", bucketName, objectName);
            return this.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * getPrivateObjectUrl.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> getPrivateObjectUrl(String bucketName, String objectName) {
        return Mono.fromCallable(() -> getPrivateObjectUrlSync(bucketName, objectName));
    }

    private void removeObjectSync(String bucketName, String objectName) {
        try {
            log.info("Start remove object with bucket name = {} and object name = {}", bucketName, objectName);
            this.minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            log.info("Finish remove object with bucket name = {} and object name = {}", bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * removeObject.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> removeObject(String bucketName, String objectName) {
        return Mono.fromRunnable(() -> {
            removeObjectSync(bucketName, objectName);
        });
    }

    /**
     * <p>
     * getPrivateObjectUrl.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @param timeout
     *            a int
     * @param timeUnit
     *            a {@link TimeUnit} object
     * @return a {@link Mono} object
     */
    public Mono<String> getPrivateObjectUrl(String bucketName, String objectName, int timeout, TimeUnit timeUnit) {
        return Mono.fromCallable(() -> this.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(timeout, timeUnit)
                .build()));
    }

    /**
     * <p>
     * validateBucketExisted.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<Void> validateBucketExisted(String bucketName) {
        return isBucketExist(bucketName).flatMap(isBucketExist -> {
            if (!isBucketExist) {
                return makeBucket(bucketName);
            }
            return Mono.just(1).then();
        });
    }

    private void uploadFileNoNameSync(
            InputStream inputStream, String bucketName, String objectName, String contentType) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            log.info("start uploadFileNoRename {}", objectName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                            new ByteArrayInputStream(buffer), buffer.length, -1)
                    .contentType(contentType)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * uploadFileNoRename.
     * </p>
     *
     * @param inputStream
     *            a {@link InputStream} object
     * @param bucketName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link Mono} object
     * @throws Exception
     *             if any.
     */
    public Mono<String> uploadFileNoRename(
            InputStream inputStream, String bucketName, String contentType, String objectName) throws Exception {
        return validateBucketExisted(bucketName)
                .then(Mono.fromRunnable(() -> uploadFileNoNameSync(inputStream, bucketName, objectName, contentType)))
                .then(getObjectUrl(bucketName, objectName));
    }

    private void uploadFileSync(InputStream inputStream, String bucketName, String object, String contentType) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            log.info("Start uploading object " + object);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(object).stream(
                            new ByteArrayInputStream(buffer), buffer.length, -1)
                    .contentType(contentType)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * uploadFile.
     * </p>
     *
     * @param inputStream
     *            a {@link InputStream} object
     * @param bucketName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     * @throws Exception
     *             if any.
     */
    public Mono<String> uploadFile(InputStream inputStream, String bucketName, String contentType) throws Exception {
        String objectName = UUID.randomUUID().toString().replace("-", "");
        String object = handleObjectName(contentType, objectName, null);
        return validateBucketExisted(bucketName)
                .then(Mono.fromRunnable(() -> {
                    uploadFileSync(inputStream, bucketName, object, contentType);
                }))
                .then(getObjectUrl(bucketName, object));
    }

    private void uploadFileSync(String filePath, byte[] file, String bucket) {
        try {
            log.info("Start uploadFile{} ", filePath);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(filePath).stream(
                            new ByteArrayInputStream(file), file.length, -1)
                    .build());
            log.info(UPLOAD_SUCCESS_LOG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * <p>
     * uploadFile.
     * </p>
     *
     * @param filePath
     *            a {@link String} object
     * @param file
     *            an array of {@link byte} objects
     * @param bucket
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> uploadFile(String filePath, byte[] file, String bucket) {
        return validateBucketExisted(bucket)
                .then(Mono.fromRunnable(() -> uploadFileSync(filePath, file, bucket)))
                .then(getObjectUrl(bucket, filePath));
    }

    /**
     * <p>
     * uploadFileReturnObjectName.
     * </p>
     *
     * @param inputStream
     *            a {@link InputStream} object
     * @param bucketName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     * @throws RuntimeException
     *             if any.
     */
    public Mono<String> uploadFileReturnObjectName(InputStream inputStream, String bucketName, String contentType)
            throws RuntimeException {
        String objectName = UUID.randomUUID().toString().replace("-", "");
        String object = handleObjectName(contentType, objectName, null);
        return validateBucketExisted(bucketName)
                .then(Mono.fromRunnable(() -> {
                    try {
                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);
                        log.info("Start uploading object {}", object);
                        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(object).stream(
                                        new ByteArrayInputStream(buffer), buffer.length, -1)
                                .contentType(contentType)
                                .build());
                        log.info("Upload file successfully");
                    } catch (Exception e) {
                        log.error("Upload file error ", e);
                        throw new RuntimeException(e);
                    }
                }))
                .then(Mono.just(object));
    }

    private void removeObjectByUrlSync(String url) {
        try {
            String[] splits = url.split("/");
            String bucketName = "";
            String objectName = "";
            // if start with http
            if (url.startsWith("http")) {
                bucketName = splits[3];
            } else {
                if (url.startsWith("/")) {
                    bucketName = splits[1];
                } else {
                    bucketName = splits[0];
                }
            }
            if ("".equals(bucketName)) throw new RuntimeException("Bucket not valid");
            int indexOfObjName = url.indexOf(bucketName) + bucketName.length() + 1;
            if (indexOfObjName > (url.length() - 1)) throw new RuntimeException("Object not valid");
            objectName = url.substring(indexOfObjName);
            log.info("Start remove object with bucket name = " + bucketName + " and object name = " + objectName);
            this.minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            log.info("Finish remove object with bucket name = " + bucketName + " and object name = " + objectName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * removeObject.
     * </p>
     *
     * @param url
     *            a {@link String} object
     * @return a {@link Mono} object
     * @throws Exception
     *             if any.
     */
    public Mono<Void> removeObject(String url) throws Exception {
        return Mono.fromRunnable(() -> removeObjectByUrlSync(url));
    }

    private String uploadFileSync(byte[] bytes, String bucketName, String object, String contentType) {
        try {
            log.info("Start uploadFile {} ", object);
            MimetypesFileTypeMap mtft = new MimetypesFileTypeMap();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(object)
                            .contentType(mtft.getContentType(object))
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .build());
            log.info(UPLOAD_SUCCESS_LOG);
            return object;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * <p>
     * uploadFile.
     * </p>
     *
     * @param bytes
     *            an array of {@link byte} objects
     * @param bucketName
     *            a {@link String} object
     * @param object
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> uploadFile(byte[] bytes, String bucketName, String object) {
        return isBucketExist(bucketName).flatMap(existed -> {
            if (existed) {
                return Mono.fromCallable(() -> {
                            MimetypesFileTypeMap mtft = new MimetypesFileTypeMap();
                            String contentType = mtft.getContentType(object);
                            return uploadFileSync(bytes, bucketName, object, contentType);
                        })
                        .subscribeOn(Schedulers.boundedElastic());
            } else {
                return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "minio.bucket.not-existed"));
            }
        });
    }

    /**
     * <p>
     * createBucketIfNotExist.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectLock
     *            a boolean
     */
    public void createBucketIfNotExist(String bucketName, boolean objectLock) {
        try {
            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .objectLock(objectLock)
                        .build());
            }
        } catch (Exception e) {
            log.error("CreateBucketIfNotExist error", e);
        }
    }

    /**
     * <p>
     * getObjectNameFromUrl.
     * </p>
     *
     * @param folderContent
     *            a {@link String} object
     * @param url
     *            a {@link String} object
     * @return a {@link String} object
     */
    public static String getObjectNameFromUrl(String folderContent, String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return folderContent + fileName;
    }

    /**
     * <p>
     * getBase64FromUrl.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param url
     *            a {@link String} object
     * @return a {@link String} object
     */
    public String getBase64FromUrl(String bucketName, String url) {
        try {
            InputStream objectData = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(url.substring(url.lastIndexOf('/') + 1))
                    .build());
            byte[] bytes = objectData.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            log.error("Bucket not exist", ex);
        }
        return null;
    }

    /**
     * <p>
     * uploadFileReturnObject.
     * </p>
     *
     * @param inputStream
     *            a {@link InputStream} object
     * @param bucketName
     *            a {@link String} object
     * @param contentType
     *            a {@link String} object
     * @return a {@link Mono} object
     */
    public Mono<String> uploadFileReturnObject(InputStream inputStream, String bucketName, String contentType) {
        String objectName = UUID.randomUUID().toString().replace("-", "");
        String object = handleObjectName(contentType, objectName, null);
        return validateBucketExisted(bucketName)
                .then(Mono.fromRunnable(() -> uploadFileSync(inputStream, bucketName, object, contentType)))
                .then(Mono.just(object));
    }

    /**
     * <p>
     * getUrlOfObject.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link String} object
     */
    public String getUrlOfObject(String bucketName, String objectName) {
        return "/media/" + bucketName + "/" + objectName;
    }

    /**
     * <p>
     * getObject.
     * </p>
     *
     * @param bucketName
     *            a {@link String} object
     * @param objectName
     *            a {@link String} object
     * @return a {@link InputStream} object
     * @throws ServerException
     *             if any.
     * @throws InsufficientDataException
     *             if any.
     * @throws ErrorResponseException
     *             if any.
     * @throws IOException
     *             if any.
     * @throws NoSuchAlgorithmException
     *             if any.
     * @throws InvalidKeyException
     *             if any.
     * @throws InvalidResponseException
     *             if any.
     * @throws XmlParserException
     *             if any.
     * @throws InternalException
     *             if any.
     */
    public InputStream getObject(String bucketName, String objectName)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
                    NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
                    InternalException {
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
