package pl.edu.agh.misows.AWS_S3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String AWS_ACCESS_KEY_ID = fill_me;
    private static final String AWS_SECRET_ACCESS_KEY = fill_me;
    public static final String BUCKET_NAME = fill_me;
    public static final String DOWNLOADED_FILE_NAME = fill_me;
    public static final String UPLOADED_FILE_NAME = fill_me;
    public static final String INPUT_FILE_NAME = fill_me;

    public static void main(String[] args) {
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY));
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

        String bucketName = BUCKET_NAME;

        System.out.println("Uploading file");
        long startTime = System.nanoTime();
        File potato = new File(INPUT_FILE_NAME);
        s3.putObject(bucketName, UPLOADED_FILE_NAME, potato);
        long startupTime = System.nanoTime() - startTime;
        long convert = TimeUnit.MILLISECONDS.convert(startupTime, TimeUnit.NANOSECONDS);
        System.out.println("upload time in milliseconds:"+convert+"\n");


        System.out.println("Downloading file");
        long startTime2 = System.nanoTime();
        S3Object object = s3.getObject(bucketName, UPLOADED_FILE_NAME);
        InputStream objectContent = object.getObjectContent();
        try {
            saveFile(objectContent, DOWNLOADED_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startupTime2 = System.nanoTime() - startTime2;
        try {
            objectContent.close();
        } catch (IOException e) {
            System.out.println("error closing InputStream");
            e.printStackTrace();
        }
        convert = TimeUnit.MILLISECONDS.convert(startupTime2, TimeUnit.NANOSECONDS);
        System.out.println("download time in milliseconds:"+convert+"\n\n");
    }

    private static void saveFile(InputStream objectContent, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        int read;
        byte[] bytes = new byte[1024];
        while ((read = objectContent.read(bytes)) != -1) {
            fos.write(bytes, 0, read);
        }
        fos.close();
    }

}
