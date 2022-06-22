package uz.pdp.class_manager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.class_manager.entity.Attachment;
import uz.pdp.class_manager.entity.AttachmentContent;
import uz.pdp.class_manager.repository.AttachmentContentRepository;
import uz.pdp.class_manager.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    private static final String uploadingDirectoryProfile = "src/main/resources/profile";
    private static final String uploadingDirectoryAssignment = "src/main/resources/assignments";
    private static final String uploadingDirectorySubmission = "src/main/resources/submissions";

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @PostMapping("/uploadSystem")
    public String system_upload(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        String fileName = fileNames.next();

        MultipartFile file = request.getFile(fileName);
        assert file != null;
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        Attachment attachment = new Attachment();
        attachment.setSize(size);
        attachment.setFileOriginalName(originalFilename);
        attachment.setContentType(contentType);
        String[] split = originalFilename.split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        attachment.setName(name); // fayl nomi saqlanadi

        Attachment save = attachmentRepository.save(attachment);

        // serverga yuklash

        if (attachment.isAssignment()) {
            Path path = Paths.get(uploadingDirectoryAssignment + "/" + name);
            Files.copy(file.getInputStream(), path);
        }
        if (attachment.isSubmission()) {
            Path path = Paths.get(uploadingDirectorySubmission + "/" + name);
            Files.copy(file.getInputStream(), path);
        }
        Path path = Paths.get(uploadingDirectoryProfile + "/" + name);
        Files.copy(file.getInputStream(), path);

        return "saqlandi id : " + save.getId();

    }

    @GetMapping("/getFileFromSystem/{id}")
    public void getFilesFromSystem(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> byId = attachmentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();
            // attachmentga nom  o'rnatish yani uni kop'chirib olsa bo'ladimi yo'qmi
            // faqat ko'rish uchunmi yoki :

            // fayl nomi
            response.setHeader("Content-Disposition",
                    "attachment; filename = \""
                            + attachment.getFileOriginalName() + "\"");

            //type:
            response.setContentType(attachment.getContentType());// nima bo'lasa shuni ayatadi
            // content :

            if (attachment.isSubmission()) {
                FileInputStream fileInputStream = new FileInputStream(uploadingDirectoryAssignment + "/" + attachment.getName());
                FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            }
            if (attachment.isAssignment()) {
                FileInputStream fileInputStream = new FileInputStream(uploadingDirectoryAssignment + "/" + attachment.getName());
                FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            }
            FileInputStream fileInputStream = new FileInputStream(uploadingDirectoryProfile + "/" + attachment.getName());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());

        } else
            System.out.println("not found");
    }
    // uploading to the DB when only one file comes
//    @PostMapping("/upload")
//    public String upload(@NotNull MultipartHttpServletRequest request) throws IOException {
//        Iterator<String> fileNames = request.getFileNames();
//        MultipartFile file = request.getFile(fileNames.next());
//
//        // file information
//        assert file != null; //if dek tekshirganda
//        String originalFilename = file.getOriginalFilename();
//        long size = file.getSize();
//        String contentType = file.getContentType();
//
//        Attachment attachment = new Attachment();
//        attachment.setFileOriginalName(originalFilename);
//        attachment.setContentType(contentType);
//        attachment.setSize(size);
//
//        Attachment save = attachmentRepository.save(attachment);
//
//        // file content (asosiy byte[] larda saqlaymiz)  info
//        byte[] bytes = file.getBytes();
//
//        AttachmentContent attachmentContent = new AttachmentContent();
//
//        attachmentContent.setBytes(bytes);
//        attachmentContent.setAttachment(save);
//
//        attachmentContentRepository.save(attachmentContent);
//        return "File saqlandi  id : " + save.getId();
//    }

    // upload to the system :


    // getting from DB
//    @GetMapping("/download/{id}")
//    public void getFile(@PathVariable Integer id,
//                        HttpServletResponse response) throws IOException {
//
//        Optional<Attachment> byId = attachmentRepository.findById(id);
//        if (byId.isPresent()) {
//            Attachment attachment = byId.get();
//
//            Optional<AttachmentContent> byAttachmentId = attachmentContentRepository.findByAttachmentId(attachment.getId());
//            if (byAttachmentId.isPresent()) {
//                AttachmentContent attachmentContent = byAttachmentId.get();
//
//                // fayl nomi
//                response.setHeader("Content-Disposition",
//                        "attachment; filename = \""
//                                + attachment.getFileOriginalName() + "\"");
//
//                //type:
//                response.setContentType(attachment.getContentType());// nima bo'lasa shuni ayatadi
//                // content :
//                FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());
//
//
//            }
//        } else {
//            throw new FileNotFoundException("File not found");
//        }
//    }

//    @PutMapping("/{id}")
//    public HttpEntity<?> editAttachment(@PathVariable Integer id, @NotNull MultipartHttpServletRequest request) throws IOException {
//
//        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
//        if (optionalAttachment.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This attachment not found");
//        }
//
//        Iterator<String> fileNames = request.getFileNames();
//        MultipartFile file = request.getFile(fileNames.next());
//
//        // file information
//        assert file != null; //if dek tekshirganda
//        String originalFilename = file.getOriginalFilename();
//        long size = file.getSize();
//        String contentType = file.getContentType();
//
//        Attachment attachment = new Attachment();
//        attachment.setFileOriginalName(originalFilename);
//        attachment.setContentType(contentType);
//        attachment.setSize(size);
//
//        Attachment save = attachmentRepository.save(attachment);
//
//        // file content (asosiy byte[] larda saqlaymiz)  info
//        byte[] bytes = file.getBytes();
//
//        AttachmentContent attachmentContent = new AttachmentContent();
//
//        attachmentContent.setBytes(bytes);
//        attachmentContent.setAttachment(save);
//
//        attachmentContentRepository.save(attachmentContent);
//        return ResponseEntity.ok("File saqlandi  id : " + save.getId());
//    }


}
