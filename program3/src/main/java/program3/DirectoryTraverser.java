package program3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import program3.S3Connector;

/**
 * 
 * @author amana
 *
 */
public class DirectoryTraverser {
	private String parentDirectory;
	private String bucketName = "";
	private S3Connector s3 = new S3Connector();
	
	public DirectoryTraverser(String directory) {
		if (!new File(directory).exists()) {
			System.out.printf("%s does not exist.\n", directory);
			System.exit(1);
		}
		
		this.parentDirectory = directory;
	}
	
	public void setBucketName(String name) {
		this.bucketName = name;
	}
	
	public void backupDirectory() {
		if (this.bucketName == "" || this.bucketName == null) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String formattedDate = now.format(format);
						
			this.bucketName = new File(this.parentDirectory).getName().toLowerCase() + "-" + formattedDate;
		}
				
		if (s3.createBucket(this.bucketName) == null) {
			return;
		}

		try {
			Files.walk(Paths.get(this.parentDirectory))
				.filter(Files::isRegularFile)
				.forEach(node -> traverseDirectory(node, this.parentDirectory, this.bucketName, this.s3));	

			System.out.println("Back up complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void traverseDirectory(Path node, String root, String bucketName, S3Connector s3) {
		String relative = new File(root).toPath().relativize(node).toString();
		s3.addObject(bucketName, relative.replace("\\", "/"), node.toString());
		System.out.println(relative.replace("\\", "/"));
	}	
}
