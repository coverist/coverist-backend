package kw.more.coverist.util

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class AmazonS3Util() {

    @Autowired
    private lateinit var amazonS3: AmazonS3

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    fun uploadFile(file: MultipartFile, dirName: String): String {
        print(bucket)
        val fileName = dirName + "/" + createFileName(file.originalFilename!!)

        val inputStream = file.inputStream

        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = file.size
        objectMetadata.contentType = file.contentType

        amazonS3.putObject(
            PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(
                CannedAccessControlList.PublicReadWrite
            )
        )

        return amazonS3.getUrl(bucket, fileName).toString()
    }

    fun deleteFile(url: String) {
        val fileName = getFileName(url)
        amazonS3.deleteObject(DeleteObjectRequest(bucket, fileName))
    }

    fun createFileName(originalFileName: String): String {
        return UUID.randomUUID().toString() + getFileExtension(originalFileName)
    }

    fun getFileName(url: String): String {
        val paths = url.split("/")
        return paths[paths.size - 2] + "/" + paths[paths.size - 1]
    }

    fun getFileExtension(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf("."))
    }
}