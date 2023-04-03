package cn.ybzy.mvcproject.service;

public class FactoryService {
	public static UserService getUserService() {
		return new UserServiceImpl();
	}
	
	public static OnlineService getOnlineService() {
		return new OnlineServiceImpl();
	}
}
