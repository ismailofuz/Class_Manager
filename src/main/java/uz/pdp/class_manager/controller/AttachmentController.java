package uz.pdp.class_manager.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Iterator;
import java.util.Optional;


@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    private static final String uploadingDirectory = "src/main/resources/uploaded_files";

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    // uploading to the DB when only one file comes
    @PostMapping("/upload")
    public String upload(@NotNull MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());

        // file information
        assert file != null; //if dek tekshirganda
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        String contentType = file.getContentType();

        Attachment attachment = new Attachment();
        attachment.setFileOriginalName(originalFilename);
        attachment.setContentType(contentType);
        attachment.setSize(size);

        Attachment save = attachmentRepository.save(attachment);

        // file content (asosiy byte[] larda saqlaymiz)  info
        byte[] bytes = file.getBytes();

        AttachmentContent attachmentContent = new AttachmentContent();

        attachmentContent.setBytes(bytes);
        attachmentContent.setAttachment(save);

        attachmentContentRepository.save(attachmentContent);
        return "File saqlandi  id : " + save.getId();
    }

//    // upload to the system :
//    @PostMapping("/system_upload")
//    public String system_upload(MultipartHttpServletRequest request) throws IOException {
//        Iterator<String> fileNames = request.getFileNames();
//        String fileName = fileNames.next();
//
//        MultipartFile file = request.getFile(fileName);
//        assert file != null;
//        long size = file.getSize();
//        String originalFilename = file.getOriginalFilename();
//        String contentType = file.getContentType();
//
//        Attachment attachment = new Attachment();
//        attachment.setSize(size);
//        attachment.setFileOriginalName(originalFilename);
//        attachment.setContentType(contentType);
//        String[] split = originalFilename.split("\\.");
//        String name = UUID.randomUUID() + "." + split[split.length - 1];
//        attachment.setName(name); // fayl nomi saqlanadi
//
//        Attachment save = attachmentRepository.save(attachment);
//
//
//        // serverga yuklash
//
//        Path path = Path.of(uploadingDirectory + "/" + name);
//
//        Files.copy(file.getInputStream(), path);
//
//        return "saqlandi id : " + save.getId();
//
//    }

    // getting from DB
    @GetMapping("/download/{id}")
    public void getFile(@PathVariable Integer id,
                        HttpServletResponse response) throws IOException {

        Optional<Attachment> byId = attachmentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();

            Optional<AttachmentContent> byAttachmentId = attachmentContentRepository.findByAttachmentId(attachment.getId());
            if (byAttachmentId.isPresent()) {
                AttachmentContent attachmentContent = byAttachmentId.get();

                // fayl nomi
                response.setHeader("Content-Disposition",
                        "attachment; filename = \""
                                + attachment.getFileOriginalName() + "\"");

                //type:
                response.setContentType(attachment.getContentType());// nima bo'lasa shuni ayatadi
                // content :
                FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());


            }
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

//    @GetMapping("/getFileFromSystem/{id}")
//    public void getFilesfromsystem(@PathVariable Integer id, HttpServletResponse response) throws IOException {
//        Optional<Attachment> byId = attachmentRepository.findById(id);
//        if (byId.isPresent()) {
//            Attachment attachment = byId.get();
//            // attachmentga nom  o'rnatish yani uni kop'chirib olsa bo'ladimi yo'qmi
//            // faqat ko'rish uchunmi yoki :
//
//            // fayl nomi
//            response.setHeader("Content-Disposition",
//                    "attachment; filename = \""
//                            + attachment.getFileOriginalName() + "\"");
//
//            //type:
//            response.setContentType(attachment.getContentType());// nima bo'lasa shuni ayatadi
//            // content :
//            FileInputStream fileInputStream = new FileInputStream(uploadingDirectory + "/" + attachment.getName());
//            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
//
//        }else
//        System.out.println("not found");
//    }

}
