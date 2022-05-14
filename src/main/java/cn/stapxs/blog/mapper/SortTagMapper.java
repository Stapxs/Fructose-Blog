package cn.stapxs.blog.mapper;

import cn.stapxs.blog.model.SortInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SortTagMapper {

    @Insert("INSERT INTO fb_sort(sort_name,sort_title,sort_mark) VALUES(#{sort_name},#{sort_title},#{sort_mark})")
    void addSort(SortInfo sortInfo);

    @Select("SELECT * FROM fb_sort")
    public SortInfo[] getSortTag();

    @Delete("DELETE FROM fb_sort WHERE sort_name = #{sort_name}")
    void deleteSort(String sort_name);

}
