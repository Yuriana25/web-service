package com.syvodid.webservice;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class ImageController {

    private final ImageService imageService;
    private final ResourceLoader resourceLoader;
    private static final Logger logger = Logger.getLogger(ImageController.class.getName());
    private final String UPLOAD_DIR = "C:/Users/Olena/IdeaProjects/web-service/uploads/";

    public ImageController(ImageService imageService, ResourceLoader resourceLoader) {
        this.imageService = imageService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Получаем все изображения из базы данных
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index"; // имя HTML-шаблона
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.severe("Ошибка: файл не загружен.");
            return "redirect:/"; // Перенаправление на главную страницу
        }
        // Убедитесь, что директория существует
        File uploadDir = new File(UPLOAD_DIR);
        boolean dirCreated = uploadDir.mkdirs(); // Создайте директорию, если её нет
        if (dirCreated) {
            logger.info("Директория создана: " + UPLOAD_DIR);
        } else {
            logger.info("Директория уже существует или не удалось создать: " + UPLOAD_DIR);
        }

        try {
            // Путь для сохранения оригинального изображения
            String originalImagePath = UPLOAD_DIR + file.getOriginalFilename();
            File savedImage = new File(originalImagePath);

            // Сохраняем оригинальное изображение на диск
            file.transferTo(savedImage);

            // Небольшая задержка, чтобы убедиться, что файл полностью записан
            Thread.sleep(800); // 100 мс задержки

            // Проверяем, что файл был успешно сохранен
            if (!savedImage.exists()) {
                logger.severe("Ошибка: файл не был сохранен на диск.");
                return "redirect:/";
            }

            // Путь для сохранения миниатюры
            String thumbnailPath = UPLOAD_DIR + "thumb_" + file.getOriginalFilename();

            // Создаем миниатюру на основе сохраненного файла
            Thumbnails.of(savedImage)
                    .size(150, 150)
                    .toFile(thumbnailPath);

            // Сохраняем информацию в базу данных
            Image image = new Image();
            image.setImagePath(originalImagePath);
            image.setThumbnailPath(thumbnailPath);
            imageService.saveImage(image);
        } catch (IOException | InterruptedException e) {
            logger.severe("Ошибка при загрузке изображения: " + e.getMessage());
        }
        return "redirect:/";
    }
}