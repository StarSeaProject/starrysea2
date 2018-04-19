package top.starrysea.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import top.starrysea.object.view.out.Position;
import top.starrysea.object.view.out.Positions;

@Controller
public class PositionController {

	@MessageMapping("/get_position")
    @SendTo("/position")
	public Positions getPosition() {
		List<Position> position=new ArrayList<>();
		Position p1=new Position("1","1");
		Position p2=new Position("2","2");
		Position p3=new Position("3","3");
		position.add(p1);
		position.add(p2);
		position.add(p3);
		Positions ps=new Positions();
		ps.setPositions(position);
		return ps;
	}
}
