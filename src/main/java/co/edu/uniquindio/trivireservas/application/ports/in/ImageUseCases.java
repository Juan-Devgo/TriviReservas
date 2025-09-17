package co.edu.uniquindio.trivireservas.application.ports.in;

public interface ImageUseCases {

    Void saveImage(String base64);

    Void deleteImage(String path);
}
