package org.choncms.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


public class AwsToolsApp {
	private static final Log log = LogFactory.getLog(AwsToolsApp.class);
	
	public static AmazonS3 createS3Client(InputStream awsCredentialsProperties) throws IOException {
		log.info("Creating s3 client");
		return new AmazonS3Client(new PropertiesCredentials(awsCredentialsProperties));
	}

	public static void main(String[] args) throws Exception {
		
		Options options = new Options();
		options.addOption("b", true, "Amazon bucket name eg. s3.choncms.com ");
		options.addOption("n", true, "Amazon top folder in bucket eg. my/folder ");
		options.addOption("d", true, "Absolute path from local directory to import");
		options.addOption("a", true, "Aws Credintials properties file, defalut: ./AwsCredentials.properties ");
		options.addOption("h", false, "Print this message");
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse( options, args);
		
		if (!validInput(cmd)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("AwsToolsApp", options);
		} else {
			try {
				process(cmd);
			} catch (Exception e) {
				e.printStackTrace();
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("AwsToolsApp", options);
			}
		}
	}

	
	
	
	private static void process(CommandLine cmd) throws Exception {
		String bucket = cmd.getOptionValue("b");
		String name = cmd.getOptionValue("n");
		String folder = cmd.getOptionValue("d");
		
		System.out.println("OK");
		System.out.println("Bucket: " + bucket);
		System.out.println("Name: " + name);
		System.out.println("Local Folder: " + folder);
		File dir = new File(folder);
		if(dir.exists() && dir.isDirectory()) {
			//long size = FolderUtils.getFolderSize(dir);
			//System.out.println("Folder " + dir.getAbsolutePath() + " size: " + FolderUtils.readableFileSize(size));
			String props = cmd.getOptionValue("a", "AwsCredentials.properties");
			FileInputStream fis = new FileInputStream(props);
			AmazonS3 s3 = createS3Client(fis);
			fis.close();
			S3Tools s3Tools = new S3Tools(s3);
			s3Tools.importDir(bucket, name, dir.getAbsolutePath());
		} else {
			throw new Exception("Invalid directory specified.");
		}
		
	}

	private static boolean validInput(CommandLine cmd) {
		if(!cmd.hasOption("b")) return false;
		if(!cmd.hasOption("n")) return false;
		if(!cmd.hasOption("d")) return false;
		return true;
	}
}
