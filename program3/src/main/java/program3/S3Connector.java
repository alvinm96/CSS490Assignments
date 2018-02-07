package program3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.io.File;
import java.util.List;

/**
 * 
 * @author amana
 *
 */
public class S3Connector {
	private AmazonS3 s3;
	
	public S3Connector() {
		this.s3 = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
	}
	
	public void addObject(String bucketName, String key, String fileName) {
		if (this.s3.doesObjectExist(bucketName, fileName)) {
			System.out.printf("%s exists in %s", fileName, bucketName);
			return;
		}
		
		try {			
			this.s3.putObject(bucketName, key, new File(fileName));
		} catch (AmazonS3Exception e) {
			System.err.printf("\nThere was an error adding an object...\nError Message: %s", e.getMessage());
			System.exit(1);
		} 
	}
	
	public Bucket createBucket(String bucketName) {
		Bucket bucket = null;
		System.out.printf("Creating new bucket: %s\n", bucketName);
		
		try {
			if (this.s3.doesBucketExist(bucketName)) {
				System.out.printf("%s already exists", bucketName);
			} else {
				try {
					bucket = this.s3.createBucket(bucketName);
					System.out.printf("Created bucket: %s\n", bucketName);
				} catch (AmazonS3Exception e) {
					System.err.printf("\nThere was an error creating the bucket...\nError Message: %s", e.getErrorMessage());
					System.exit(1);
				}
			}
		} catch (SdkClientException e) {
			System.err.printf("\nThere was an error with AWS...\nError Message: %s", e.getMessage());
			System.exit(1);
		}
		
		return bucket;
	}
	
	public void printBuckets() {
		System.out.println("S3 Buckets:");
		List<Bucket> buckets = this.s3.listBuckets();
		
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getName());
		}
	}

}
