package top.starrysea.service.mail;

import java.util.List;

import top.starrysea.kql.entity.Entity;

public interface IMailService {

	void sendMailService(Entity entity);
	
	void sendMailService(List<? extends Entity> entitys);
}
