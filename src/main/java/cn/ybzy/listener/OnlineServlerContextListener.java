package cn.ybzy.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.swing.Timer;

import cn.ybzy.mvcproject.model.Online;
import cn.ybzy.mvcproject.service.FactoryService;
import cn.ybzy.mvcproject.service.OnlineService;

@WebListener
public class OnlineServlerContextListener implements ServletContextListener {
	/**
	 * 10分钟, 访问者超过设置的时间毫秒,没有操作,离线
	 */
	public final int MAX_MILLIS = 10 * 60 * 1000;

	OnlineService onlineService = FactoryService.getOnlineService();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 存放过时的访问者信息
		List<Online> expiresUserList = new ArrayList<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 服务器启动的时候就被执行
		// 定时器
		new Timer(5 * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				List<Online> list = onlineService.getAllOnline();
				if (list != null && list.size() > 0) {
					Date exDate = null;
					for (Online ol : list) {
						exDate = ol.getTime();
						simpleDateFormat.format(exDate);
						Long exMilles;
						try {
							exMilles = simpleDateFormat.parse(exDate.toString()).getTime();
							if ((System.currentTimeMillis() - exMilles) > MAX_MILLIS) {
								expiresUserList.add(ol);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				// 从数据库中删除掉过时的访问者信息
				onlineService.deleteExpiresOnline(expiresUserList);
				//数据库中删除过时的访问者信息
				expiresUserList.clear();
			}
		}).start();

	}

}










