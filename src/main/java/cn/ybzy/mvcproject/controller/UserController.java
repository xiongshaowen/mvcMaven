package cn.ybzy.mvcproject.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ybzy.mvcproject.model.Online;
import cn.ybzy.mvcproject.model.User;
import cn.ybzy.mvcproject.service.FactoryService;
import cn.ybzy.mvcproject.service.OnlineService;
import cn.ybzy.mvcproject.service.UserService;
import cn.ybzy.mvcproject.utils.CookieUtils;

@SuppressWarnings("unused")
@WebServlet(urlPatterns = { "*.udo" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserService userService = FactoryService.getUserService();
	OnlineService onlineService = FactoryService.getOnlineService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String mn = req.getServletPath();
		mn = mn.substring(1);
		mn = mn.substring(0, mn.length() - 4);
		try {
			Method method = this.getClass().getDeclaredMethod(mn, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/* User user = new User();
		user.setUsername(req.getParameter("username"));
		user.setPassword(req.getParameter("password"));
		user.setAddress(req.getParameter("address"));
		user.setPhoneNo(req.getParameter("phoneNo"));
		user.setRegDate(new Date());
		int rows = userService.save(user);
		if (rows > 0) {
			resp.sendRedirect(req.getContextPath() + "/main.jsp");
		} else {
			resp.sendRedirect(req.getContextPath() + "/error.jsp");
		}*/
		User user=new User();
    	//User yUser=null;
    	//String yUsername=null;
    	String xUsername=null;
    	
        xUsername=req.getParameter("username");
    	//if(xUsername!=null) {
    	 //  yUser=userService.get(xUsername);
    	  // yUsername=yUser.getUsername();
    	//}
    	long cout=(long) userService.getCountByName(xUsername);
    	if(cout>0) {
    		req.setAttribute("note",xUsername+",这个用户已被占用，请输入另一个名字试试");
    		req.getRequestDispatcher("/add.jsp").forward(req, resp);
    	}else {
    	user.setUsername(xUsername);
    	user.setPassword(req.getParameter("password"));
    	user.setPhoneNo(req.getParameter("phoneNo"));
    	user.setAddress(req.getParameter("address"));
    	user.setRegDate(new Date());
    	int rows=userService.save(user);
    	if(rows>0)
    		resp.sendRedirect(req.getContextPath()+"/main.jsp");
    	else {
    		//throw new RuntimeException("注册失败！");
    		resp.sendRedirect(req.getContextPath()+"/Error.jsp");
    		
    	 }
    	}
	}

	private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String address = req.getParameter("address");
		String phoneNo = req.getParameter("phoneNo");
		List<User> list = userService.query(username, address, phoneNo);
		req.setAttribute("userList", list);
		req.getRequestDispatcher("/main.jsp").forward(req, resp);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		int rows = userService.deleteUserById(id);
		if (rows > 0) {
			resp.sendRedirect(req.getContextPath() + "/main.jsp");
		} else {
			resp.sendRedirect(req.getContextPath() + "/error.jsp");
		}
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		User user = userService.get(id);
		req.setAttribute("user", user);
		req.getRequestDispatcher("/update.jsp").forward(req, resp);
	}

	private void updatedo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));

		User user = userService.get(id);
		String yUsername = user.getUsername();
		String xUseranme = req.getParameter("username");
		long cout = userService.getCountByName(xUseranme);
		if (!xUseranme.equals(yUsername) && cout > 0) {
			req.setAttribute("note", xUseranme + "已被占用!");
			req.getRequestDispatcher("/update.udo?id=" + id).forward(req, resp);
			return;
		}
		user.setUsername(xUseranme);
		user.setPassword(req.getParameter("password"));
		user.setAddress(req.getParameter("address"));
		user.setPhoneNo(req.getParameter("phoneNo"));
		int rows = userService.updateUserById(user);
		if (rows > 0) {
			resp.sendRedirect(req.getContextPath() + "/main.jsp");
		} else {
			resp.sendRedirect(req.getContextPath() + "/error.jsp");
		}
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String expiredays = req.getParameter("expiredays");
		Cookie[] cookies = req.getCookies();
		boolean login = false;
		String account = null;
		String ssid = null;

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userKey")) {
					account = cookie.getValue();
				}

				if (cookie.getName().equals("ssid")) {
					ssid = cookie.getValue();
				}
			}
		}
		if (account != null && ssid != null) {
			login = ssid.equals(CookieUtils.md5Encrypt(username));
		}

		if (!login) {
			User user = userService.login(username, password);

			if (user != null) {
				expiredays = expiredays == null ? "" : expiredays;
				switch (expiredays) {
				case "7":
					CookieUtils.createCookie(username, req, resp, 7 * 24 * 60 * 60);
					break;
				case "30":
					CookieUtils.createCookie(username, req, resp, 30 * 24 * 60 * 60);
					break;
				case "100":
					CookieUtils.createCookie(username, req, resp, Integer.MAX_VALUE);
					break;
				default:
					CookieUtils.createCookie(username, req, resp, -1);
					break;
				}

				req.getSession().setAttribute("user", user.getUsername());
				HttpSession session = req.getSession();
				Online ol = onlineService.getOnlineBySsid(session.getId());
				if (ol != null) {
					ol.setUsername(username);
					onlineService.updateOnline(ol);
				}
				resp.sendRedirect(req.getContextPath() + "/main.jsp");
			} else {
				req.setAttribute("note", "用户名或密码有误!");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}

		} else {
			req.getSession().setAttribute("user", username);
			HttpSession session = req.getSession();
			Online ol = onlineService.getOnlineBySsid(session.getId());
			if (ol != null) {
				ol.setUsername(username);
				onlineService.updateOnline(ol);
			}
			resp.sendRedirect(req.getContextPath() + "/main.jsp");
		}

	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userKey")) {
					cookie.setMaxAge(0);
					resp.addCookie(cookie);
				}
				if (cookie.getName().equals("ssid")) {
					cookie.setMaxAge(0);
					resp.addCookie(cookie);
				}
			}
		}
		HttpSession session = req.getSession();
		if (session != null) {
			session.removeAttribute("user");
		}
		resp.sendRedirect(req.getContextPath() + "/login.jsp");
	}

	public void online(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Online> list = onlineService.getAllOnline();
		req.setAttribute("online", list);
		req.getRequestDispatcher("/WEB-INF/jsp/online.jsp").forward(req, resp);
	}

}
