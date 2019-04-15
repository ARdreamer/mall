package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    public ServerResponse<String> checkValid(String str, String type);

    public ServerResponse selectQuestion(String username);

    public ServerResponse<String> checkAnswer(String username, String question, String answer);

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    public ServerResponse<User> updateInformation(User user);

    public ServerResponse<User> getInformation(Integer userId);

    public ServerResponse checkAdminRole(User user);
}
