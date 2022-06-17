package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface ICourseService extends IService<Course> {

    Page<Course> findPage(Page<Course> page, String name);

    Page<Course> findStudentPage(Page<Course> page,String name, Integer id);

    void setStudentCourse(Integer courseId, Integer studentId);

    Long getCoureseNum();
}
