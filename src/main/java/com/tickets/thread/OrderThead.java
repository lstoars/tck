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
 * ��Ʊ�߳� �ɹ��Ժ� ��JOptionPane.showMessageDialog�ķ�ʽ֪ͨ�û�
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
		log.info("=============��ʼ===================");
		// ���ѯ�����ypInfoDetail ����
		Result clickQueryResult = null;
		do {
			clickQueryResult = App.clickQuery(client,param);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!clickQueryResult.isSuccess() && bool);

		// ���Ԥ��
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

		// ȷ���µ� (������ص�Result.isSuccess ���Ϊtrue
		// ������Ӧ���Ѿ��µ��ɹ�����������¿��Ժ������judgeSuccessһ���ж�)
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
			JOptionPane.showMessageDialog(null, "ò�Ƴɹ���", "ò�Ƴɹ���",
					JOptionPane.ERROR_MESSAGE);
		}

		// �ж��Ƿ��µ��ɹ�
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
					log.info("�Զ���ȡ����֤��Ϊ:"+randCode);
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
