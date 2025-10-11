package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.ports.in.ImagesUseCases;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImagesServices implements ImagesUseCases {

    private final Cloudinary cloudinary;

    @Override
    public Map saveImage(MultipartFile image) throws Exception {

        File file = convert(image);

        return cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "TriviReservas"));
    }

    @Override
    public Map deleteImage(String imageId) throws Exception {

        return cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile image) throws IOException { // TODO Asignar excepción
        File file = File.createTempFile(image.getOriginalFilename(), null);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(image.getBytes());
        fos.close();
        return file;
    }
}
