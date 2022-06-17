package com.qingge.springboot.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qingge.springboot.common.Constants;
import com.qingge.springboot.entity.Files;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.mapper.FileMapper;
import com.qingge.springboot.service.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FileMapper, Files>
        implements FilesService {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper filesMapper;

    @Override
    public String uploadFiles(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();//原文件名
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        Files files = new Files();
        String url = "";

        String md5 = SecureUtil.md5(file.getInputStream());

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 从数据库查询是否存在相同的记录
        Files dbfilegetOne = getFileByMd5(md5);


        if (dbfilegetOne != null) { // 文件已存在
            url = dbfilegetOne.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUUID;
        }

        if (getFileByName(originalFilename) == null) {
            //存储数据库
            files.setName(originalFilename);
            files.setType(type);
            files.setSize(size / 1024);
            files.setUrl(url);
            files.setMd5(md5);
            try {
                if (filesMapper.insert(files) > 0) {
                    return url;
                }
                return "";
            } catch (RuntimeException e) {
                throw new ServiceException(Constants.CODE_500, "系统繁忙请稍后重试");
            }
        }
        return url;


    }

    /**
     * 通过文件的md5查询文件
     *
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Files::getMd5, md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);

    }

    /**
     * 通过文件的md5查询文件
     *
     * @param name
     * @return
     */
    private Files getFileByName(String name) {
        // 查询文件的md5是否存在
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Files::getName, name);

        return filesMapper.selectOne(queryWrapper);

    }


    public void download(String fileUUID, HttpServletResponse response) throws IOException {

        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);


        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @Override
    public int updateByIdForFile(Files files) {
        return filesMapper.updateById(files);
    }

    @Override
    public int deleteByIdForFile(Integer id) {
        Files files = filesMapper.selectById(id);
        files.setIsDelete(true);
        return filesMapper.updateById(files);
    }

    @Override
    public boolean deleteByIdForFileBatch(List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = filesMapper.selectList(queryWrapper);
        if (files == null) {
            return false;
        }
        for (Files file : files) {
            file.setIsDelete(true);
            filesMapper.updateById(file);
        }
        return true;
    }

    @Override
    public IPage<Files> findPage(IPage<Files> page, String name) {
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq(Files::getIsDelete, false);
        queryWrapper.orderByDesc(Files::getId);
        if (!"".equals(name)) {
            queryWrapper.like(Files::getName, name);
        }
        return filesMapper.selectPage(page, queryWrapper);
    }
}




