package com.rlf.module.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.rlf.module.mapper.UserMapper;
import com.rlf.module.entity.User;
import com.rlf.module.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


}
