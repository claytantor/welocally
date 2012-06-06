package com.sightlyinc.ratecred.admin.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadBean {
    
    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

}
