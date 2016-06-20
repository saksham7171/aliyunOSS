package blog

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.GetObjectRequest
import grails.core.GrailsApplication
import org.springframework.beans.factory.annotation.Value

class AliyunController {

    GrailsApplication grailsApplication

    @Value('${cloud.aliyun.accessKey}')
    private String accessKey

    @Value('${cloud.aliyun.secretKey}')
    private String secretKey

    @Value('${cloud.aliyun.endpoint}')
    private String endpoint

    @Value('${cloud.aliyun.bucketName}')
    private String bucketName

    @Value("myKey")
    private String key

    OSSClient getClient() {
        return new OSSClient(endpoint, accessKey, secretKey)
    }

    def upload() {
        File file = grailsApplication.mainContext.getResource("/data.txt").file
        client.putObject(bucketName, key, file)
        render "File is successfully uploaded !!!"
    }

    def download() {
        InputStream object = client.getObject(new GetObjectRequest(bucketName, key)).objectContent
        File tempFile = File.createTempFile("aliyun_temp_download_", key)
        tempFile.withOutputStream { it.write(object.bytes) }
        object.close()
        tempFile.delete()
        render "File is successfully downloaded !!!"
    }

    def delete() {
        client.deleteObject(bucketName, key)
        render "File is successfully deleted !!!"
    }
}
