package com.rlf.module.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.var;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public User(){}
    public User(String userName){
        this.userName = userName;
    }


    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public void speak() {
        System.out.println("speak");
    }

    @Override
    public void walk() {
        System.out.println("walk");
    }

    public static void main(String[] args) {
        Integer reduce = Stream.iterate(0, i -> i+1).limit(10001).parallel().reduce(0,Integer::sum);
        Integer a = 0;
        for (int i = 1; i <= 10000; i++) {
            a+=i;
        }
        System.out.println(reduce);
        System.out.println(a);
    }
}