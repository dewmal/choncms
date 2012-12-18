package org.choncms.aws;

import java.io.FileInputStream;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	private String awsPropertieslcoation = "c:/temp/AwsCredentials.properties";
	
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	private void doTheJob(List<S3ObjectSummary> ls, int i) {
		System.out.println("doTheJob_"+i);
		for(S3ObjectSummary o : ls) {
			System.out.println("  " + o.getKey());
		}
	}
	
	/**
	 * Rigourous Test :-)
	 * @throws Exception 
	 */
//	public void testApp() throws Exception {
//		FileInputStream fis = new FileInputStream(awsPropertieslcoation);
//		AmazonS3 s3 = AwsToolsApp.createS3Client(fis);
//		ObjectListing ol = s3.listObjects("s3.choncms.com", "the-bucket-folder");
//		List<S3ObjectSummary> ls = ol.getObjectSummaries();
//		int k = 0;
//		doTheJob(ls, k++);
//		while(ol.isTruncated()) {
//			ol = s3.listNextBatchOfObjects(ol);
//			doTheJob(ls, k++);
//		}
//		
//		fis.close();
//	}
//	
//	public void testDeleteFolder() throws Exception {
//		FileInputStream fis = new FileInputStream(awsPropertieslcoation);
//		AmazonS3 s3 = AwsToolsApp.createS3Client(fis);
//		S3Tools s3Tools = new S3Tools(s3);
//		s3Tools.deleteFolder("s3.choncms.com", "the-bucket-folder");
//		fis.close();
//	}
//	
//	public void testImport() throws Exception {
//		FileInputStream fis = new FileInputStream(awsPropertieslcoation);
//		AmazonS3 s3 = AwsToolsApp.createS3Client(fis);
//		S3Tools s3Tools = new S3Tools(s3);
//		//s3Tools.deleteFolder("s3.choncms.com", "the-bucket-folder");
//		s3Tools.importDir("s3.choncms.com", "the-bucket-folder", "C:/temp/joco");
//		fis.close();
//	}
}
