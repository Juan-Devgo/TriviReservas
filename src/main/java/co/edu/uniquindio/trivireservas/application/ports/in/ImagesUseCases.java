package co.edu.uniquindio.trivireservas.application.ports.in;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImagesUseCases {

    Map saveImage(MultipartFile image) throws Exception;

    Map deleteImage(String imageId)  throws Exception;
}
