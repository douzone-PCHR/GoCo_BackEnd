package com.pchr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.pchr.dto.FileDTO;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RequiredArgsConstructor
@Component
public class S3Util {

	private final AmazonS3Client amazonS3Client;

	private AmazonS3 s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@PostConstruct
	public void setS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

		s3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	// S3 업로드
	public void uploadFile(String fileName, InputStream inputStream)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
//		S3Client client = S3Client.builder().build();

//		PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(fileName).build();

		s3Client.putObject(bucket, fileName, inputStream, null);
//		client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));

//		S3Waiter waiter = client.waiter();
//		HeadObjectRequest waitRequest = HeadObjectRequest.builder().bucket(bucket).key(fileName).build();
//		WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(waitRequest);
//
//		waiterResponse.matched().response().ifPresent(x -> {
//			System.out.println("The file " + fileName + " is now ready");
//		});
	}

	// S3 파일 삭제
	public void deleteFile(String fileName) {
//		S3Client client = S3Client.builder().build();
//
//		DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(fileName).build();
//
//		client.deleteObject(deleteRequest);
		boolean isExistObject = amazonS3Client.doesObjectExist(bucket, fileName);
		if (isExistObject == true) {
			amazonS3Client.deleteObject(bucket, fileName);
		}
		System.out.println(getFileUrl(fileName));

	}

	// URL 받아오기
	private String getFileUrl(String fileName) {
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	// S3 업로드 적용
	public FileDTO S3Upload(MultipartFile multipart, String directory)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		String originalFileName = multipart.getOriginalFilename();
		// 파일을 같이 업로드하지 않았을 경우 s3에 저장되지 않고 null을 반환
		if (originalFileName.length() != 0) {
			String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
			try {
				System.out.println(multipart.getInputStream());
				uploadFile(directory + fileName, multipart.getInputStream()); // S3 File Upload 부분 신경 안써도 됨.
				String filePath = getFileUrl(directory + fileName); // S3 url 값 받아오기
				FileDTO fileDTO = FileDTO.builder().fileName(fileName).originalName(originalFileName).filePath(filePath)
						.build();
				return fileDTO;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
