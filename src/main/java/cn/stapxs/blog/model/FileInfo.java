package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Version: 1.0
 * @Date: 2022/05/15 下午 02:51
 * @ClassName: FileInfo
 * @Author: Stapxs
 * @Description 文件信息
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private int user_id;
    private Date file_date;
    private String file_name;
    private double file_size;
    private String file_url;
}
