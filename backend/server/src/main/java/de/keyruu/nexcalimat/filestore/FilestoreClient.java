package de.keyruu.nexcalimat.filestore;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class FilestoreClient
{
  private static final Logger LOG = LoggerFactory.getLogger(FilestoreClient.class);

  @ConfigProperty(name = "de.keyruu.nexcalimat.bucket.name")
  String _bucketName;

  @Inject
  S3Client _s3;

  private boolean _bucketExists = false;

  @PostConstruct
  public void init()
  {
    ensureBucketExists();
  }

  public ResponseInputStream<GetObjectResponse> getFile(String objectKey)
  {
    if (_bucketExists == false)
    {
      ensureBucketExists();
    }

    try
    {
      GetObjectRequest getRequest = buildGetRequest(objectKey);
      return _s3.getObject(getRequest);
    }
    catch (NoSuchKeyException ex)
    {
      LOG.error(ex.getMessage());
      throw new NotFoundException(ex);
    }
  }

  public String uploadFile(FileFormData formData, String prefix) throws IOException
  {
    if (_bucketExists == false)
    {
      ensureBucketExists();
    }

    String objectKey = buildObjectKey(formData);
    PutObjectRequest putRequest = buildPutRequest(buildFullObjectKey(prefix, objectKey), Files.probeContentType(formData.data.toPath()));
    _s3.putObject(putRequest, RequestBody.fromFile(formData.data));
    return objectKey;
  }

  public static String buildFullObjectKey(String prefix, String objectKey)
  {
    return prefix + "/" + objectKey;
  }

  public void deleteFile(String objectKey)
  {
    if (_bucketExists == false)
    {
      ensureBucketExists();
    }

    DeleteObjectRequest deleteRequest = buildDeleteRequest(objectKey);
    _s3.deleteObject(deleteRequest);
  }

  private GetObjectRequest buildGetRequest(String objectKey)
  {
    return GetObjectRequest.builder()
      .bucket(_bucketName)
      .key(objectKey)
      .build();
  }

  private PutObjectRequest buildPutRequest(String objectKey, String mimetype)
  {
    return PutObjectRequest.builder()
      .bucket(_bucketName)
      .key(objectKey)
      .contentType(mimetype)
      .build();
  }

  private DeleteObjectRequest buildDeleteRequest(String objectKey)
  {
    return DeleteObjectRequest.builder()
      .bucket(_bucketName)
      .key(objectKey)
      .build();
  }

  private String buildObjectKey(FileFormData formData)
  {
    return UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(formData.data.getName());
  }

  private void ensureBucketExists()
  {
    boolean exists = bucketExists();
    if (exists == false)
    {
      createBucket();
    }
    _bucketExists = true;
  }

  private boolean bucketExists()
  {
    HeadBucketRequest headBucketRequest = buildHeadBucketRequest();
    try
    {
      HeadBucketResponse headBucket = _s3.headBucket(headBucketRequest);
      return headBucket.sdkHttpResponse().isSuccessful();
    }
    catch (NoSuchBucketException ex)
    {
      LOG.error(ex.getMessage());
      return false;
    }

  }

  private void createBucket()
  {
    LOG.info("Creating bucket...");
    CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
      .bucket(_bucketName)
      .build();
    _s3.createBucket(createBucketRequest);
    LOG.info("Bucket created");
  }

  private HeadBucketRequest buildHeadBucketRequest()
  {
    return HeadBucketRequest.builder()
      .bucket(_bucketName)
      .build();
  }
}