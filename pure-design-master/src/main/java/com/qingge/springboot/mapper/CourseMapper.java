package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> page, @Param("name") String name);

    Page<Course> findStudentPage(Page<Course> page, @Param("id") Integer id, @Param("name") String name);


    void deleteStudentCourse(@Param("courseId")Integer courseId, @Param("studentId") Integer studentId);

    void setStudentCourse(@Param("courseId")Integer courseId, @Param("studentId") Integer studentId);

}
