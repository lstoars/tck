package com.tickets.thread;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import com.tickets.App;
import com.tickets.Result;
import com.tickets.domain.OrderParam;
import com.tickets.frame.MainFrame;
import com.tickets.ocr.DefaultOcrHandler;
import com.tickets.ocr.StaticInfo;
import com.tickets.utils.TicketUtils;

/**
 * 订票线程 成功以后 以JOptionPane.showMessageDialog的方式通知用户
 * 
 * @author sahala
 * 
 */
public class OrderThead implements Runnable {
	public static boolean bool = true;
	private final Log log = LogFactory.getLog(MainFrame.class);

	private HttpClient client;
	private String orderRandCodeText;
	
	private OrderParam param;
	
	private boolean autoRandCode;

	public OrderThead(HttpClient client, String orderRandCodeText,OrderParam param,boolean autoRandCode) {
		this.client = client;
		this.orderRandCodeText = orderRandCodeText;
		this.param = param;
		this.autoRandCode = autoRandCode;
	}

	@Override
	public void run() {
		log.info("=============开始===================");
		// 点查询，获得ypInfoDetail 参数
		Result clickQueryResult = null;
		do {
			clickQueryResult = App.clickQuery(client,param);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!clickQueryResult.isSuccess() && bool);

		// 点击预定
		Result clickBookResult = null;
		if(bool){
			do {
				clickBookResult = App.clickBook(client,param);
			} while (!clickBookResult.isSuccess() && bool);
		}

		Result goConfirmPageResult = null;
		if(bool){
			do {
				goConfirmPageResult = App
						.goConfirmPage(client,
								"https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
			} while (!goConfirmPageResult.isSuccess() && bool);
		}

		// 确认下单 (如果返回的Result.isSuccess 结果为true
		// 理论上应该已经下单成功，保险情况下可以和下面的judgeSuccess一起判断)
		Result confirmOrderResult = null;
		if (bool) {
			do {
				confirmOrderResult = App.confirmOrder(client,
						goConfirmPageResult.getToken(), orderRandCodeText,
						param,goConfirmPageResult.getLeftTicketStr());
				if (!confirmOrderResult.isSuccess() && bool) {
					goConfirmPageResult = App
							.goConfirmPage(client,
									"https://dynamic.12306.cn/otsweb/order/confirmPassengerAction.do?method=init");
					
					if(autoRandCode){
						getRandCodeText();
					}
				}
			} while (!confirmOrderResult.isSuccess() && bool);
		}
		if (bool) {
			JOptionPane.showMessageDialog(null, "貌似成功了", "貌似成功了",
					JOptionPane.ERROR_MESSAGE);
		}

		// 判断是否下单成功
		// App.judgeSuccess(client);
	}
	
	private void getRandCodeText(){
		boolean goOn = false;
		do{
			App.getOrderRand(client);
			DefaultOcrHandler ocr = new DefaultOcrHandler();
			try {
				String randCode = ocr.recognizeText(new File(StaticInfo.orderRandCodePath),"jpg").replaceAll("\\[", "J");
				if(!TicketUtils.isBlank(randCode) && randCode.matches("[0-9A-Z]{4}")){
					orderRandCodeText = randCode;
					log.info("自动获取的验证码为:"+randCode);
					goOn = false;
				} else {
					goOn = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				goOn = true;
			}
		}while(goOn);
	}
}
