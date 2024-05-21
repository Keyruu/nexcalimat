package de.keyruu.nexcalimat.filestore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class FilestoreClient
{
	private static final Logger LOG = LoggerFactory.getLogger(FilestoreClient.class);

	@ConfigProperty(name = "de.keyruu.nexcalimat.bucket.name")
	String bucketName;

	@Inject
	S3Client s3;

	private boolean bucketExists = false;

	@PostConstruct
	public void init()
	{
		ensureBucketExists();
	}

	public String uploadFile(FileFormData formData, String prefix, Long id) throws IOException
	{
		if (!bucketExists)
		{
			ensureBucketExists();
		}

		String objectKey = buildObjectKey(formData, id);
		PutObjectRequest putRequest = buildPutRequest(buildFullObjectKey(prefix, objectKey), Files.probeContentType(Path.of(formData.filename)));
		s3.putObject(putRequest, RequestBody.fromFile(formData.file));
		return objectKey;
	}

	public static String buildFullObjectKey(String prefix, String objectKey)
	{
		return prefix + "/" + objectKey;
	}

	public void deleteFile(String objectKey)
	{
		if (!bucketExists)
		{
			ensureBucketExists();
		}

		DeleteObjectRequest deleteRequest = buildDeleteRequest(objectKey);
		s3.deleteObject(deleteRequest);
	}

	private PutObjectRequest buildPutRequest(String objectKey, String mimetype)
	{
		return PutObjectRequest.builder()
			.bucket(bucketName)
			.key(objectKey)
			.contentType(mimetype)
			.build();
	}

	private DeleteObjectRequest buildDeleteRequest(String objectKey)
	{
		return DeleteObjectRequest.builder()
			.bucket(bucketName)
			.key(objectKey)
			.build();
	}

	private String buildObjectKey(FileFormData formData, Long id)
	{
		return UUID.randomUUID() + "_" + id + "." + FilenameUtils.getExtension(formData.filename);
	}

	private void ensureBucketExists()
	{
		boolean exists = bucketExists();
		if (!exists)
		{
			createBucket();
		}
		bucketExists = true;
	}

	private boolean bucketExists()
	{
		HeadBucketRequest headBucketRequest = buildHeadBucketRequest();
		try
		{
			HeadBucketResponse headBucket = s3.headBucket(headBucketRequest);
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
			.bucket(bucketName)
			.build();
		s3.createBucket(createBucketRequest);
		LOG.info("Bucket created");
	}

	private HeadBucketRequest buildHeadBucketRequest()
	{
		return HeadBucketRequest.builder()
			.bucket(bucketName)
			.build();
	}
}