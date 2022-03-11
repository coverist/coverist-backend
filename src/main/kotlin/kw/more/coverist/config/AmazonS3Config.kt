package kw.more.coverist.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonS3Config {

    @Bean
    fun amazonS3Client(
        @Value("\${cloud.aws.credentials.access_key}") accessKey: String,
        @Value("\${cloud.aws.credentials.secret_key}") secretKey: String,
        @Value("\${cloud.aws.region.static}") region: String,
    ): AmazonS3Client {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build() as AmazonS3Client
    }
}