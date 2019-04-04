package top.starrysea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.view.in.UserForAdd;
import top.starrysea.object.view.in.UserForCheck;
import top.starrysea.object.view.in.UserForLogin;
import top.starrysea.service.IUserService;

import top.starrysea.object.dto.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.USER;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/check")
    @ResponseBody
    public Map<String, Object> checkUserAvailabilityController(@Valid UserForCheck user) {
        ServiceResult serviceResult = userService.checkUserAvailabilityService(user.toDTO());
        Map<String, Object> result = new HashMap<>();
        result.put("userEmail", user.getUserEmail());
        if (serviceResult.isSuccessed()) {
            result.put("isAvailable", true);
        } else {
            result.put("isAvailable", false);
            result.put("errInfo", serviceResult.getErrInfo());
        }
        return result;
    }

    @PostMapping("/register")
    public ModelAndView registerController(@Valid UserForAdd user) {
        ModelAndView modelAndView = new ModelAndView();
        ServiceResult serviceResult = userService.registerService(user.toDTO());
        if (serviceResult.isSuccessed()) {
            //TODO: 注册成功后的动作
        } else {
            //TODO: 注册失败后的动作
        }
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginController(@Valid UserForLogin user, BindingResult bindingResult, Device device){
        ServiceResult serviceResult = userService.userLogin(user.toDTO());
        ModelAndView modelAndView = new ModelAndView();
        if (serviceResult.isSuccessed()) {
            User user1 = serviceResult.getResult(USER);
            modelAndView.addObject(USER_SESSION_KEY, user1.getUserId())
                    .setViewName(device.isMobile() ? MOBILE + "index" : "index");
        }
        else {
            // 登陆失败,返回登陆页面
            modelAndView.addObject(ERRINFO, serviceResult.getErrInfo())
                    .setViewName(device.isMobile() ? MOBILE + "index" : "index");
        }
        return modelAndView;
    }

    @GetMapping("/exit")
    public ModelAndView exitController(Device device) {
        return new ModelAndView(device.isMobile() ? MOBILE + "index" : "index");
    }
}
