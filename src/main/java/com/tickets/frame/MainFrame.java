package com.tickets.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import com.tickets.App;
import com.tickets.thread.OrderThead;
import com.tickets.Result;
import com.tickets.domain.LoginParam;
import com.tickets.domain.OrderParam;
import com.tickets.ocr.StaticInfo;
import com.tickets.utils.TicketUtils;

/**
 * 
 * @author sahala
 * 
 */
public class MainFrame extends JFrame {
	private boolean bool = true;
	private final Log log = LogFactory.getLog(MainFrame.class);
	private static final long serialVersionUID = 1L;
	static HttpClient client = TicketUtils.getNewHttpClient();
	JFrame jf = new JFrame("12306买票");
	JPanel jp = new JPanel();
	JLabel lName = new JLabel("用户名：");
	JLabel lPassword = new JLabel("密码：");
	JLabel loginRandCodeLabel = new JLabel("登录验证码：");
	JLabel orderRandCodeLabel = new JLabel("下单验证码：");
	static JTextField tName = new JTextField();
	static JTextField tPassword = new JTextField();
	static JTextField loginRandCode = new JTextField();
	static JTextField orderRandCode = new JTextField();
	JLabel loginRandImage = new JLabel("");
	JLabel orderRandImage = new JLabel("");

	JButton ok = new JButton("买票");
	JButton cancel = new JButton("取消");
	
	JButton loginCancel = new JButton("取消");
	JButton loginImageChange = new JButton("获取");
	JButton orderImageChange = new JButton("获取");

	/** 确认订单参数文件 */
	JLabel loName = new JLabel("参数文件");
	JLabel lOrderFilePath = new JLabel("");
	JFileChooser orderFile = new JFileChooser();

	JButton orderOpen = new JButton("打开");

	JButton login = new JButton("登录");
	
	JCheckBox autoRand = new JCheckBox("自动识别验证码");

	JLabel splitLabel = new JLabel(
			"-------------------------------------------------------------------------------------------------------------");

	private boolean isLogin;

	private int threadCount = 1;

	public MainFrame() {
		log.info("111111111111111111111111111111111111111");
		
		jp.setLayout(null);

		// 用户名
		lName.setBounds(85, 40, 60, 30);
		jp.add(lName);
		tName.setBounds(145, 40, 150, 30);
		jp.add(tName);

		// 密码
		lPassword.setBounds(85, 90, 60, 30);
		jp.add(lPassword);
		tPassword.setBounds(145, 90, 150, 30);
		jp.add(tPassword);

		// 登录验证码
		loginRandCodeLabel.setBounds(45, 130, 100, 30);
		jp.add(loginRandCodeLabel);
		loginRandImage.setBounds(145, 130, 65, 30);
		jp.add(loginRandImage);
		loginRandCode.setBounds(220, 130, 75, 30);
		jp.add(loginRandCode);
		loginImageChange.setBounds(300, 130, 60, 30);
		jp.add(loginImageChange);

		login.setBounds(160, 165, 60, 30);
		jp.add(login);
		
		loginCancel.setBounds(300, 165, 60, 30);
		jp.add(loginCancel);

		splitLabel.setBounds(45, 210, 500, 10);
		jp.add(splitLabel);

		// 下单验证码
		orderRandCodeLabel.setBounds(45, 240, 100, 30);
		jp.add(orderRandCodeLabel);
		orderRandImage.setBounds(145, 240, 60, 30);
		jp.add(orderRandImage);
		orderRandCode.setBounds(220, 240, 60, 30);
		jp.add(orderRandCode);
		orderImageChange.setBounds(300, 240, 60, 30);
		jp.add(orderImageChange);

		/** 确认订单参数文件 */
		loName.setBounds(45, 280, 150, 30);
		jp.add(loName);
		orderOpen.setBounds(180, 280, 60, 30);
		jp.add(orderOpen);
		lOrderFilePath.setBounds(300, 280, 250, 30);
		jp.add(lOrderFilePath);
		
		autoRand.setBounds(45,320,150,30);
		jp.add(autoRand);

		ok.setBounds(160, 370, 60, 30);
		jp.add(ok);

		cancel.setBounds(300, 370, 60, 30);
		jp.add(cancel);
		jf.add(jp);

		// 事件OK按钮监听
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrderThead.bool = true;
				if (!isLogin) {
					JOptionPane.showMessageDialog(null, "哥们，还没有登录呢!", "error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String orderRandCodeText = orderRandCode.getText();
					String orderParamFile = lOrderFilePath.getText();
					try{
						OrderParam param = TicketUtils.parseFileToParam(orderParamFile);
						ExecutorService service = Executors.newFixedThreadPool(threadCount);
						for (int i = 0; i < threadCount; i++) {
							OrderThead ot = new OrderThead(client,orderRandCodeText,param,autoRand.isSelected());
							service.submit(ot);
						}
					} catch(Exception ex){
						JOptionPane.showMessageDialog(null, ex.getMessage(), "error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});

		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bool = true;
				LoginThread loginThread = new LoginThread(client);
				new Thread(loginThread).start();
			}
		});

		orderOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jp.add(orderFile);
				int returnVal = orderFile.showOpenDialog(jf);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					lOrderFilePath.setText(orderFile.getSelectedFile()
							.getParent()
							+ File.separator
							+ orderFile.getSelectedFile().getName());
				}
			}
		});

		// 事件Cancel按钮监听
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrderThead.bool = false;
			}
		});
		
		loginCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bool = false;
			}
		});

		loginImageChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bool = true;
				Thread changeRand = new Thread(new ChangeRandCode(client,
						loginRandImage, StaticInfo.loginRandCodePath, "login"));
				changeRand.start();
			}
		});

		orderImageChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bool = true;
				Thread changeRand = new Thread(new ChangeRandCode(client,
						orderRandImage, StaticInfo.orderRandCodePath, "order"));
				changeRand.start();
			}
		});

		jf.setVisible(true);
		jf.setSize(550, 500);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocation(500, 150);
	}

	class LoginThread implements Runnable {
		private HttpClient client;

		public LoginThread(HttpClient client) {
			this.client = client;
		}

		@Override
		public void run() {
			log.info("开始登录，请稍后...");
			String userName = tName.getText();
			String passWord = tPassword.getText();
			String loginRandCodeText = loginRandCode.getText();

			// 到登陆界面
			Result goLoginPageResult = null;
			do {
				goLoginPageResult = App.goLoginPage(client,
						"https://dynamic.12306.cn/otsweb/order/../login.jsp");
			} while (!goLoginPageResult.isSuccess() && bool);
			
			
			
			// 登陆
			Result loginResult = null;
			do {
				String aysn = App.goLoginPage(client, "https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest").getResponseHtml();
				JSONObject json = JSONObject.fromObject(aysn);
				LoginParam loginParam = new LoginParam();
				loginParam.setLoginRand((String)json.get("loginRand"));
				loginParam.setRefundFlag((String)json.getString("randError"));
				loginParam.setPassword(passWord);
				loginParam.setUsername(userName);
				loginParam.setRandCode(loginRandCodeText);
				
				loginResult = App.login(client, loginParam);
			} while (!loginResult.isSuccess()
					&& TicketUtils.isBlank(loginResult.getLoginFailPrompt()) && bool);
			if (!loginResult.isSuccess()) {
				JOptionPane.showMessageDialog(null,
						loginResult.getLoginFailPrompt(), "error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				isLogin = true;
				login.setEnabled(false);
				JOptionPane.showMessageDialog(null, "登录成功了", "登录成功了",
						JOptionPane.ERROR_MESSAGE);
				new Reminder();
			}

		}

	}

	/**
	 * 用线程来处理换验证码的请求，防止程序假死
	 * 
	 * @author sahala
	 * 
	 */
	class ChangeRandCode implements Runnable {
		private HttpClient client;
		private JLabel randImage;
		private String fileName;
		private String type;

		public ChangeRandCode(HttpClient client, JLabel randImage,
				String fileName, String type) {
			this.client = client;
			this.randImage = randImage;
			this.fileName = fileName;
			this.type = type;
		}

		public void run() {
			try {
				BufferedImage myPicture;
				do {
					log.info("开始获取"+type+"验证码，请稍后....");
					if ("login".equals(type)) {
						App.getLoginRand(client);
					} else {
						App.getOrderRand(client);
					}
					myPicture = ImageIO.read(new File(fileName));
					if (null == myPicture) {
						log.info("获取" + type + "验证码失败，重试中......");
					} else {
						log.info("获取" + type + "验证码成功");
					}
				} while (myPicture == null && bool);
				if(myPicture != null){
					randImage.setIcon(new ImageIcon(myPicture));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 登录成功后，每隔20分钟重复请求一个地址,来保持session
	 * 
	 * @author sahala
	 * 
	 */
	public class Reminder {
		Timer timer;

		public Reminder() {
			timer = new Timer();
			timer.schedule(new RemindTask(), 0, 20 * 60 * 1000);
		}

		class RemindTask extends TimerTask {
			int numWarningBeeps = 3;

			public void run() {
				App.judgeSuccess(client);
			}
		}

	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
