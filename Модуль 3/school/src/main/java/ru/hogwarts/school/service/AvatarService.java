package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${avatar.path}")
    private String avatarPath;

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;


    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar on id");
        Student student = studentService.findStudent(id);

        Path filePath = Path.of(avatarPath, id + ".jpg");
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(id);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImageData(filePath));
        avatar.setStudent(student);

        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long id) {
        logger.info("Was invoked method for find avatar on id");
        return avatarRepository.findById(id).orElse(new Avatar());
    }

    public Page<Avatar> getAvatars (Pageable pageable) {
        logger.info("Was invoked method for get avatar");
        return avatarRepository.findAll(pageable);
    }


    private byte[] generateImageData(Path filePath) throws IOException {
        logger.info("Was invoked method for generate image data");
        try ( InputStream is = Files.newInputStream(filePath);
              BufferedInputStream bis = new BufferedInputStream(is, 1024);
              ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        logger.info("Was invoked method getExtension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
