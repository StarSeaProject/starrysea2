package top.starrysea.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import top.starrysea.common.Common;
import top.starrysea.kql.facede.KumaRedisDao;
import top.starrysea.object.view.out.Position;

@Controller
public class PositionController {

	@Autowired
	private KumaRedisDao kumaRedisDao;

	@MessageMapping("/update_position")
	@SendTo("/update_position")
	public Success updatePosition(Position position) {
		kumaRedisDao.mapSet("position", position.getName(), Common.toJson(position));
		return new Success("更新成功");
	}

	@MessageMapping("/get_position")
	@SendTo("/get_position")
	public List<String> getPosition() {
		Map<String,String> positions=kumaRedisDao.mapGetAll("position");
		List<String> result=new ArrayList<>();
		positions.forEach((name,position)->result.add(position));
		return result;
	}
}

class Success {
	private String message;

	public Success(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
