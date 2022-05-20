package cn.stapxs.blog.mapper;

import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into fb_user(user_id,user_name,user_mail,user_password,reg_ip) values(#{user_id},#{user_name},#{user_mail},#{user_password},#{reg_ip})")
    boolean saveUser(User user);
    @Insert("insert into fb_userinfo(user_id) values(#{user_id})")
    void newUserInfo(int id);        // 初始化一个只有 ID 的 UserInfo

    @Select("select count(*) from fb_user")
    int getUserCount();
    @Select("select * from fb_user where user_name=#{name}")
    User getUser(String name);
    @Select("select * from fb_user where user_id=#{id}")
    User getUserByID(int id);
    @Select("select * from fb_user where user_mail=#{mail}")
    User getUserByMail(String mail);
    @Select("select * from fb_userinfo where user_id=#{id}")
    UserInfo getUserInfoByID(int id);

    @Update("update fb_user set login_key=#{key} where user_id=#{id}")
    boolean setKey(@Param("id")int id, @Param("key")String key);
    @Update("update fb_user set user_token=#{token} where user_id=#{id}")
    boolean setToken(@Param("id")int id, @Param("token")String token);
    @Update("update fb_user set login_ip=#{ip} where user_id=#{id}")
    boolean setLoginIP(@Param("id")int id, @Param("ip")String ip);
    @Update("update fb_user set ${name}=#{value} where user_id=#{id}")
    void updateUser(@Param("id")int id, @Param("name")String name, @Param("value")String value);
    @Update("update fb_userinfo set ${name}=#{value} where user_id=#{id}")
    void updateUserInfo(@Param("id")int id, @Param("name")String name, @Param("value")String value);
    @Update("update fb_user set user_token=null where user_id=#{id}")
    void delLogInfo(int id);
}
