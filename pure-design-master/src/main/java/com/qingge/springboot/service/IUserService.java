package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.controller.dto.UserPasswordDTO;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Page<User> findPage(Page<User> objectPage, String username, String email, String address);

    Map<String,Long> getUserCon();


}
