package com.tickets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.tickets.domain.LoginParam;
import com.tickets.domain.OrderParam;
import com.tickets.domain.Person;
import com.tickets.domain.Train;
import com.tickets.frame.MainFrame;
import com.tickets.ocr.StaticInfo;
import com.tickets.utils.TicketUtils;

/**
 * Hello world!
 * 
 */
public class App {
	private static final Log log = LogFactory.getLog(MainFrame.class);

	/**
	 * 点击预订URL
	 */
	public static String clickBookUrl = "https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=submutOrderRequest";

	/**
	 * 登录验证码URL
	 */
	public static String loginRandUrl = "https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand"
			+ "&" + Math.random();

	/**
	 * 下单验证码URL
	 */
	public static String orderRandUrl = "https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=randp";

	/**
	 * 登录URL
	 */
	public static String loginUrl = "https://dynamic.12306.cn/otsweb/loginAction.do?method=login";

	/**
	 * 检查订单信息
	 */
	public static String checkOrderUtl = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=checkOrderInfo";

	/**
	 * 下单URL
	 * 
	 * @param arg
	 */
	public static String orderUrl = "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=confirmSingleForQueueOrder";

	/**
	 * 确认是否下单成功
	 */
	public static String judgeSuccessUrl = "https://dynamic.12306.cn/otsweb/order/myOrderAction.do?method=queryMyOrderNotComplete";

	/**
	 * 预定参数文件
	 */
	public static String bookParamFile = "E:\\otickets\\tickets02\\doc\\a.txt";

	/**
	 * 下单参数文件
	 */
	public static String orderParamFile = "E:\\otickets\\tickets02\\doc\\b.txt";

	/**
	 * 点击预订
	 * 
	 * @param client
	 * @return
	 */
	public static Result clickBook(HttpClient client, OrderParam param) {
		Result result = new Result();
		// 点预订
		HttpPost post = new HttpPost(clickBookUrl);
		Train train = param.getTrain();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("station_train_code", train
				.getTrainCode()));
		formparams.add(new BasicNameValuePair("train_date", train.getDate()));
		formparams.add(new BasicNameValuePair("seattype_num", ""));
		formparams.add(new BasicNameValuePair("from_station_telecode", train
				.getFromStationCode()));
		formparams.add(new BasicNameValuePair("to_station_telecode", train
				.getToStationCode()));
		formparams.add(new BasicNameValuePair("include_student", "00"));
		formparams.add(new BasicNameValuePair("from_station_telecode_name",
				train.getFromStationName()));
		formparams.add(new BasicNameValuePair("to_station_telecode_name", train
				.getToStationName()));
		formparams.add(new BasicNameValuePair("round_train_date", TicketUtils
				.getCurrentDate()));
		formparams.add(new BasicNameValuePair("round_start_time_str",
				"00:00--24:00"));
		formparams.add(new BasicNameValuePair("single_round_type", "1"));
		formparams.add(new BasicNameValuePair("train_pass_type", "QB"));
		formparams.add(new BasicNameValuePair("train_class_arr",
				"QB#D#Z#T#K#QT#"));
		formparams
				.add(new BasicNameValuePair("start_time_str", "00:00--24:00"));
		formparams.add(new BasicNameValuePair("lishi", train.getLishi()));
		formparams.add(new BasicNameValuePair("train_start_time", train
				.getStartTime()));
		formparams.add(new BasicNameValuePair("trainno4", train.getTrainNo()));
		formparams.add(new BasicNameValuePair("arrive_time", train
				.getArriveTime()));
		formparams.add(new BasicNameValuePair("from_station_name", train
				.getFromStationName()));
		formparams.add(new BasicNameValuePair("to_station_name", train
				.getToStationName()));
		formparams.add(new BasicNameValuePair("from_station_no", train
				.getFromStationNo()));
		formparams.add(new BasicNameValuePair("to_station_no", train
				.getToStationNo()));
		formparams.add(new BasicNameValuePair("ypInfoDetail", param.getTrain()
				.getYpInfo()));
		formparams.add(new BasicNameValuePair("mmStr", param.getTrain()
				.getMmStr()));
		formparams.add(new BasicNameValuePair("locationCode", train
				.getLocationCode()));
		UrlEncodedFormEntity initEntity = null;
		try {
			initEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		post.setEntity(initEntity);

		String location = null;
		try {
			HttpResponse response = client.execute(post);
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");
			int statusCode = response.getStatusLine().getStatusCode();
			log.info("==========clickBook start===========");
			result.setStatusCode(statusCode);
			log.info(content);
			log.info("==========clickBook end===========");
			if (302 == statusCode) { // 这里预期的结果是返回302
				location = response.getFirstHeader("Location").getValue();
				if (!TicketUtils.isBlank(location)) {
					result.setRedirectUrl(location);
				} else {
					result.setSuccess(false);
				}
			} else {
				result.setSuccess(false);
				result.setErrorStackTrace("statusCode 返回结果和预期不一致,statusCode:"
						+ statusCode);
			}
			log.info("-------------------------------location------" + location);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
		} finally {
		}
		return result;
	}

	/**
	 * 转到登陆页面
	 * 
	 * @param client
	 * @param location
	 */
	public static Result goLoginPage(HttpClient client, String location) {
		Result result = new Result();
		// 转到登陆界面
		HttpGet get = new HttpGet(location);
		InputStream input = null;
		try {
			HttpResponse response = client.execute(get);
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");
			log.info("=========goLoginPage start============");
			log.info("goLoginPage content :" + content);
			log.info("=========goLoginPage end============");
			result.setResponseHtml(content);
			int statusCode = response.getStatusLine().getStatusCode();
			result.setStatusCode(statusCode);
			if (statusCode != 200
					|| content.indexOf("otsweb/loginAction.do?method=login") == -1) {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取登陆验证码
	 * 
	 * @param client
	 */
	public static Result getLoginRand(HttpClient client) {
		Result result = new Result();
		// 获取验证码
		HttpGet randGet = new HttpGet(loginRandUrl);
		InputStream input = null;
		OutputStream fout = null;
		try {
			HttpResponse response = client.execute(randGet);

			input = response.getEntity().getContent();
			byte[] b = new byte[102400];

			fout = new FileOutputStream(StaticInfo.loginRandCodePath);
			while (input.read(b) != -1) {
				fout.write(b);
			}
			fout.flush();
			result.setStatusCode(response.getStatusLine().getStatusCode());
			result.setResponseHtml("获取登录验证码成功");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 获取下单验证码
	 * 
	 * @param client
	 */
	public static Result getOrderRand(HttpClient client) {
		Result result = new Result();
		// 获取验证码
		HttpGet randGet = new HttpGet(orderRandUrl);
		InputStream input = null;
		OutputStream fout = null;
		try {
			HttpResponse response = client.execute(randGet);

			input = response.getEntity().getContent();
			byte[] b = new byte[102400];

			fout = new FileOutputStream(StaticInfo.orderRandCodePath);
			while (input.read(b) != -1) {
				fout.write(b);
			}
			fout.flush();
			result.setStatusCode(response.getStatusLine().getStatusCode());
			result.setResponseHtml("获取登录验证码成功");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 登陆(302)
	 * 
	 * @param client
	 * @return
	 */
	public static Result login(HttpClient client, LoginParam param) {
		Result result = new Result();
		HttpPost post = new HttpPost(loginUrl);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("loginUser.user_name", param
				.getUsername()));
		formparams.add(new BasicNameValuePair("user.password", param
				.getPassword()));
		formparams.add(new BasicNameValuePair("nameErrorFocus", ""));
		formparams.add(new BasicNameValuePair("passwordErrorFocus", ""));
		formparams.add(new BasicNameValuePair("randErrorFocus", ""));
		formparams.add(new BasicNameValuePair("randCode", param.getRandCode()));
		formparams
				.add(new BasicNameValuePair("loginRand", param.getLoginRand()));
		formparams.add(new BasicNameValuePair("refundFlag", param
				.getRefundFlag()));
		formparams.add(new BasicNameValuePair("refundLogin", param
				.getRefundLogin()));
		UrlEncodedFormEntity initEntity = null;
		try {
			initEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		post.setEntity(initEntity);
		try {
			HttpResponse response = client.execute(post);
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");
			log.info(content);
			int statusCode = response.getStatusLine().getStatusCode();
			result.setStatusCode(statusCode);
			if (statusCode == 200) {
				if (content.indexOf("/otsweb/sysuser/user_info.jsp") == -1
						&& content
								.indexOf("/otsweb/sysuser/userCenterAction.do?method=initForChangePwd") == -1) {
					result.setSuccess(false);
					result.setResponseHtml(content.toString());
					if (content.indexOf("请输入正确的验证码") != -1) {
						result.setLoginFailPrompt("请输入正确的验证码");
					}
				}
			} else {
				result.setSuccess(false);
				result.setResponseHtml(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
		} finally {
		}
		return result;
	}

	/**
	 * 转到确认订单页面
	 * 
	 * @param client
	 * @param location
	 * @return
	 */
	public static Result goConfirmPage(HttpClient client, String location) {
		Result result = new Result();
		HttpGet get = new HttpGet(location);
		String content = "";
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(get);

			content = EntityUtils.toString(response.getEntity(), "utf-8");
			log.info("=========goConfirmPage start============");
			sb.append("goConfirmPage statusCode :"
					+ response.getStatusLine().getStatusCode());
			result.setStatusCode(response.getStatusLine().getStatusCode());
			result.setResponseHtml(content);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
		}
		Document doc = Jsoup.parse(content);
		String token = getToken(doc);
		result.setLeftTicketStr(getLeftTicket(doc));
		if (token != null) {
			result.setToken(token);
			sb.append(",token[").append(token).append("]");
		} else {
			result.setSuccess(false);
		}
		sb.append(",leftTicketStr:[" + result.getLeftTicketStr() + "]");
		log.info(sb.toString());
		log.info("=========goConfirmPage end============");
		return result;

	}

	public static void prepareOrder(HttpClient client, List<NameValuePair> pairs,OrderParam param,String randCode) {
		//checkOrderInfo
		checkOrderUtl += "&rand="+randCode;
		HttpPost checkPost = new HttpPost(checkOrderUtl);
		pairs.add(new BasicNameValuePair("tFlag", "dc"));
		UrlEncodedFormEntity initEntity = null;
		try {
			initEntity = new UrlEncodedFormEntity(pairs, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		checkPost.setEntity(initEntity);
		try {
			HttpResponse response = client.execute(checkPost);
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");
			log.info(content);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		/*//getQueneCount
		String queneCountUrl= "https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=getQueueCount";
		queneCountUrl += "&train_date="+param.getTrain().getDate()+"&train_no="+param.getTrain().getTrainNo();
		queneCountUrl += "&station="+param.getTrain().getTrainCode()*/
		
	}

	/**
	 * 确认下单 randCode=CZP3
	 * org.apache.struts.taglib.html.TOKEN=411a3c36f59cb0e084479cb823f4fef5
	 * 
	 * @param client
	 * @param token
	 */
	public static Result confirmOrder(HttpClient client, String token,
			String orderRand, OrderParam param, String leftTicket) {
		Train train = param.getTrain();
		Result result = new Result();

		HttpPost post = new HttpPost(orderUrl);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(
				"org.apache.struts.taglib.html.TOKEN", token));
		formparams.add(new BasicNameValuePair("leftTicketStr", leftTicket));
		formparams.add(new BasicNameValuePair("textfield", "中文或拼音首字母"));
		formparams.add(new BasicNameValuePair("checkbox0", "0"));
		formparams.add(new BasicNameValuePair("orderRequest.train_date", train.getDate()));
		formparams.add(new BasicNameValuePair("orderRequest.train_no", train.getTrainNo()));
		formparams.add(new BasicNameValuePair("orderRequest.station_train_code", train.getTrainCode()));
		formparams.add(new BasicNameValuePair("orderRequest.from_station_telecode", train.getFromStationCode()));
		formparams.add(new BasicNameValuePair(
				"orderRequest.to_station_telecode", train.getToStationCode()));
		formparams
				.add(new BasicNameValuePair("orderRequest.seat_type_code", ""));
		formparams.add(new BasicNameValuePair(
				"orderRequest.seat_detail_type_code", ""));
		formparams.add(new BasicNameValuePair(
				"orderRequest.ticket_type_order_num", ""));
		formparams.add(new BasicNameValuePair(
				"orderRequest.bed_level_order_num",
				"000000000000000000000000000000"));
		formparams.add(new BasicNameValuePair("orderRequest.start_time", train
				.getStartTime()));
		formparams.add(new BasicNameValuePair("orderRequest.end_time", train
				.getArriveTime()));
		formparams.add(new BasicNameValuePair("orderRequest.from_station_name",
				train.getFromStationName()));
		formparams.add(new BasicNameValuePair("orderRequest.to_station_name",
				train.getToStationName()));
		formparams.add(new BasicNameValuePair("orderRequest.cancel_flag", "1"));
		formparams.add(new BasicNameValuePair("orderRequest.id_mode", "Y"));
		List<Person> persons = param.getPersons();
		int size = persons.size();
		for (int i = 1; i <= 5; i++) {
			if (i <= size) {
				Person person = persons.get(i - 1);
				formparams.add(new BasicNameValuePair("passengerTickets",
						person.getSeatType() + ",0,1," + person.getName()
								+ ",1," + person.getCardCode() + ","
								+ person.getPhone() + ",Y"));
				formparams.add(new BasicNameValuePair("oldPassengers", person
						.getName() + ",1," + person.getCardCode()));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_seat", person.getSeatType()));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_ticket", "1"));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_name", person.getName()));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_seat_detail_select", "0"));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_seat_detail", "0"));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_cardtype", "1"));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_cardno", person.getCardCode()));
				formparams.add(new BasicNameValuePair("passenger_" + i
						+ "_mobileno", person.getPhone()));
				formparams.add(new BasicNameValuePair("checkbox9", "Y"));
			} else {
				formparams.add(new BasicNameValuePair("oldPassengers", ""));
				formparams.add(new BasicNameValuePair("checkbox9", "Y"));

			}
		}
		formparams
				.add(new BasicNameValuePair("orderRequest.reserve_flag", "A"));
		formparams.add(new BasicNameValuePair("randCode", orderRand));
		formparams.add(new BasicNameValuePair("tFlag", "dc"));
		UrlEncodedFormEntity initEntity = null;
		try {
			initEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		post.setEntity(initEntity);

		try {
			prepareOrder(client, formparams, param, orderRand);
			
			HttpResponse response = client.execute(post);
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");

			result.setResponseHtml(content);
			result.setStatusCode(response.getStatusLine().getStatusCode());
			log.info("======================确认下单开始=========================");
			log.info(content);
			if (StringUtils.isNotBlank(content)) {
				JSONObject json = JSONObject.fromObject(content);
				if (!StringUtils.equals("Y", (String) json.get("errMsg"))) {
					result.setSuccess(false);
				}
			} else {
				result.setSuccess(false);
			}

			log.info("======================确认下单结束=========================");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setErrorStackTrace(e.getMessage());
		} finally {
		}
		return result;
	}

	/**
	 * 从页面获取令牌
	 * 
	 * @param soure
	 * @return
	 */
	private static String getToken(Document doc) {
		Elements eles = doc.getElementsByAttributeValue("name",
				"org.apache.struts.taglib.html.TOKEN");
		return eles.get(0).attr("value");
	}

	private static String getLeftTicket(Document doc) {
		Elements eles = doc.select("#left_ticket");
		return eles.get(0).attr("value");
	}

	public static void main(String[] args) {
		Document doc = Jsoup
				.parse("<input type=\"hidden\" name=\"leftTicketStr\" id=\"left_ticket\" value=\"O038850000M060350034P071950008\" />");
		System.out.println(getLeftTicket(doc));
	}

	/**
	 * 判断是否下单成功
	 * 
	 * @param client
	 * @return
	 */
	public static boolean judgeSuccess(HttpClient client) {
		HttpGet get = new HttpGet(judgeSuccessUrl);
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(get);
			response.getStatusLine().getStatusCode();
			String content = EntityUtils
					.toString(response.getEntity(), "utf-8");
			System.out.println(content);

			// 这个判断条件可以优化
			if (sb.indexOf("继续支付") != -1 && sb.indexOf("取消订单") != -1) {
				log.info("##########################成功#################################");
				return true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Result clickQuery(HttpClient client, OrderParam param) {

		Result result = new Result();
		StringBuilder url = new StringBuilder();
		url.append("https://dynamic.12306.cn/otsweb/order/querySingleAction.do?method=queryLeftTicket");
		url.append("&orderRequest.train_date=").append(
				param.getTrain().getDate());
		url.append("&orderRequest.from_station_telecode=").append(
				param.getTrain().getFromStationCode());
		url.append("&orderRequest.to_station_telecode=").append(
				param.getTrain().getToStationCode());
		url.append("&trainPassType=QB&trainClass=QB&includeStudent=00").append(
				"&orderRequest.train_no=");
		url.append("&seatTypeAndNum=&orderRequest.start_time_str=00:00--24:00");

		HttpGet get = new HttpGet(url.toString());
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				result.setSuccess(false);
			}
			result.setStatusCode(statusCode);
			log.info("clickQuery statusCode:" + statusCode);
			log.info("===========clickQuery statusCode start=================");
			log.info("query string :" + url.toString());
			String content = EntityUtils.toString(entity);
			log.info(content);

			Train train = TicketUtils.parseQueryResult(content, param
					.getTrain().getTrainCode());
			if (null != train) {
				train.setDate(param.getTrain().getDate());
				param.setTrain(train);
				log.info(train);
				if (TicketUtils.isBlank(train.getYpInfo())) {
					result.setSuccess(false);
				} else {
					result.setYpInfo(train.getYpInfo());
				}
			}
			log.info("===========clickQuery statusCode end  =================");
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	public static String getYpInfo(String str) {
		try {
			String[] arr = str.split("(getSelected)|(value)");
			String message = arr[1];
			String[] tmd = message.split("#|'");
			if (tmd.length == 12) {
				return tmd[10];
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
