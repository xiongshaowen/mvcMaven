package cn.ybzy.mvcproject.service;

import java.util.List;

import cn.ybzy.mvcproject.dao.FactoryDao;
import cn.ybzy.mvcproject.dao.OnlineDao;
import cn.ybzy.mvcproject.model.Online;

public class OnlineServiceImpl implements OnlineService {

	OnlineDao onlineDao = FactoryDao.getOnlineDao();

	@Override
	public List<Online> getAllOnline() {
		return onlineDao.getAllOnline();
	}

	@Override
	public void insertOnline(Online online) {
		onlineDao.insertOnline(online);
	}

	@Override
	public void updateOnline(Online online) {
		onlineDao.updateOnline(online);
	}

	@Override
	public void deleteExpiresOnline(List<Online> list) {
		if(list!=null && list.size()>0) {
			for(Online ol:list) {
				onlineDao.deleteExpiresOnline(ol.getSsid());
			}
		}
	}

	@Override
	public Online getOnlineBySsid(String ssid) {
		return onlineDao.getOnlineBySsid(ssid);
	}

}
