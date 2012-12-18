package org.choncms.aws;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Tools {
	private static final Log log = LogFactory.getLog(S3Tools.class);
	
	private AmazonS3 s3;
	
	public S3Tools(AmazonS3 s3) {
		this.s3 = s3;
	}

	private void deleteAllObjects(List<S3ObjectSummary> ls) {
		for(S3ObjectSummary o : ls) {
			s3.deleteObject(o.getBucketName(), o.getKey());
		}
	}
	
	public void deleteFolder(String amazonBucketName, String remoteDirName) {
		ObjectListing ol = s3.listObjects(amazonBucketName, remoteDirName);
		List<S3ObjectSummary> ls = ol.getObjectSummaries();
		deleteAllObjects(ls);
		while(ol.isTruncated()) {
			ol = s3.listNextBatchOfObjects(ol);
			deleteAllObjects(ls);
		}
	}
	
	public void importDir(String amazonBucketName, String remoteDirName, String localDir) {
		log.info("Deleting object " + amazonBucketName + " / " + remoteDirName);
		deleteFolder(amazonBucketName, remoteDirName);
		
		log.info("List all files from: " + localDir); 
		List<String> ls = FolderUtils.listAllSubPaths(localDir);
		for(String s : ls) {			
			File localFile = new File(localDir, s);
			String remotePath = remoteDirName + "/" + s;
			log.info("Putting Local file: ");
			log.info(" - " + localFile.getAbsolutePath());
			log.info(" Into remte path: " + remotePath);
			PutObjectResult r = s3.putObject(new PutObjectRequest(
					amazonBucketName, remotePath, localFile)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			log.debug("Result: " + r);
		}
	}
	
}
