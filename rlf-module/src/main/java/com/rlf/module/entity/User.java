package com.rlf.module.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User extends Model<User> implements Serializable,People {
    private static final long serialVersionUID = -7757471143347689303L;
    @TableId
    protected Integer id;

    @TableField("user_name")
    protected String userName;

    @TableField("age")
    protected String age;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public void speak() {
        System.out.println("speak");
    }
}