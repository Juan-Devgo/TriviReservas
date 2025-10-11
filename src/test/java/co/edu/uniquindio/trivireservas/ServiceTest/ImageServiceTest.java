package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.service.ImagesServices;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImagesServicesTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private ImagesServices imagesServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void saveImage_ShouldUploadAndReturnMap() throws Exception {
        MultipartFile mockFile = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                "dummy image content".getBytes()
        );

        Map<String, Object> expectedResponse = Map.of("url", "http://cloudinary.com/test.png");
        when(uploader.upload(any(File.class), anyMap())).thenReturn(expectedResponse);

        Map result = imagesServices.saveImage(mockFile);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/test.png", result.get("url"));
        verify(uploader, times(1)).upload(any(File.class), anyMap());
    }

    @Test
    void deleteImage_ShouldCallDestroyAndReturnMap() throws Exception {
        String imageId = "sample-id";

        Map<String, Object> expectedResponse = Map.of("result", "ok");
        when(uploader.destroy(eq(imageId), anyMap())).thenReturn(expectedResponse);

        Map result = imagesServices.deleteImage(imageId);

        assertNotNull(result);
        assertEquals("ok", result.get("result"));
        verify(uploader, times(1)).destroy(eq(imageId), anyMap());
    }
}
