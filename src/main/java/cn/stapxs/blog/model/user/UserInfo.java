package cn.stapxs.blog.model.user;

import com.mysql.cj.jdbc.Blob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @Version: 1.0
 * @Date: 2022/05/07 上午 10:51
 * @ClassName: UserInfo
 * @Author: Stapxs
 * @Description 用户信息结构
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int user_id;
    private String user_nike;
    private Blob user_avatar;

    /**
     * @Author Stapxs
     * @Description 构建只有 UserId 的空用户信息
     * @Date 上午 11:23 2022/05/07
     * @Param [user_id]
    **/
    public UserInfo(int user_id) {
        this.user_id = user_id;
    }

    /**
     * @Author Stapxs
     * @Description 将 Blob 的原始数据转换为文件流
     * @Date 上午 11:02 2022/05/07
     * @Param []
     * @return InputStream
    **/
    public InputStream getAvatar() throws SQLException {
        return user_avatar.getBinaryStream(0, user_avatar.length());
    }
}
