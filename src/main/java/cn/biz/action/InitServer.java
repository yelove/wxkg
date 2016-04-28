package cn.biz.action;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.biz.service.MessageService;
import cn.biz.service.WebImServer;

@Component
public class InitServer {
	
	@Value("${netty.port}")
	private String port;

	@Autowired
	private MessageService messageService;

	@PostConstruct
	public void intiNettyServer() {
		Thread th = new Thread(new WebImServer(Integer.parseInt(port), messageService), "msgsokserver");
		th.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
