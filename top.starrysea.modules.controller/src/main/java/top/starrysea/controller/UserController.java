package top.starrysea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.view.in.UserForAdd;
import top.starrysea.object.view.in.UserForCheck;
import top.starrysea.service.IUserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
}
