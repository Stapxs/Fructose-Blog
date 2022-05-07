package cn.stapxs.blog.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @Version: 1.0
 * @Date: 2022/02/24 下午 04:01
 * @ClassName: User
 * @Author: Stapxs
 * @Description 用户结构
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int user_id;
    private String user_name;
    private String user_password;
    private String user_mail;
    private String user_token;

    private String login_key;

    private String pass_code;
    private String pass_code_time;

    private int account_type;
    private int account_status;

    private String login_ip;
    private String reg_ip;

    /**
     * @Author Stapxs
     * @Description 生成 token， token 结构为：时间（前五位）+ UUID（16 位）+ 用户 ID + 时间（后五位），共 26 位
     * @Date 下午 02:00 2022/05/01
     * @Param []
     * @return java.lang.String
    **/
    public String createToken() {
        System.out.print("操作 > 生成 token > getToken > " + this.user_id);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println(uuid +", " + time);
        this.user_token = time.substring(0, 5) + uuid.substring(0, 16) + user_id + time.substring(5);
        return this.user_token;
    }
}
