package com.tickets;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Hello extends JFrame {

	private static final long serialVersionUID = 1L;

	private JFrame jFrame = null;

	private JButton jFileButton = null;

	public Hello() {
		// TODO Auto-generated constructor stub
		jFrame = this;
		this.setSize(496, 260);
		jFileButton = new JButton("导入文件");
		jFrame.add(jFileButton);
		jFileButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// System.out.println("mouseClicked()"); // TODO
				// Auto-generated Event stub mouseClicked()
				JFileChooser jfChooser = new JFileChooser("D:\\..\\..");
				jfChooser.setDialogTitle("导入文件");
				jfChooser.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						if (f.getName().endsWith("data") || f.isDirectory())
							return true;
						return false;
					}

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return "数值型数据(*.data)";
					}
				});
				int result = jfChooser.showOpenDialog(jFrame);
				if (result == JFileChooser.APPROVE_OPTION) { // 确认打开

					File fileIn = jfChooser.getSelectedFile();

					if (fileIn.exists()) {
						JOptionPane.showMessageDialog(jFrame, "OPEN"); // 提示框
					} else {
					}
				} else if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("Cancel button is pushed.");
				} else if (result == JFileChooser.ERROR_OPTION) {
					System.err.println("Error when select file.");
				}

			}
		});
	}
	
	public static void main(String[] args) {
		new Hello();
	}
}
