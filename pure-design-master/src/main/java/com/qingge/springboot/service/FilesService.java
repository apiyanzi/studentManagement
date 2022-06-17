package com.qingge.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingge.springboot.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface FilesService extends IService<Files> {

    String uploadFiles(MultipartFile file) throws IOException;

    void download (String fileUUID, HttpServletResponse response) throws IOException;

    int updateByIdForFile(Files files);

    int deleteByIdForFile(Integer id);

    boolean deleteByIdForFileBatch( List<Integer> ids);

    IPage<Files> findPage (IPage<Files> page,String name);

}
