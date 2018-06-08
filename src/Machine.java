import javax.swing.*;
//import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//import java.util.*;
import java.util.Vector;

@SuppressWarnings("serial")
class Login extends JFrame implements ActionListener {
	private String password = "123";    // 비밀번호를 123으로 설정 
	private JPasswordField passwordField;
	private JButton login;
	JLabel loginText = new JLabel();
	private boolean isLogin = false;    // 로그인이 성공했는지 판별(초기값은 실패)
	int num=0;
	
	public Login(int managerNum) {
		num = managerNum;
		JPanel passPanel = new JPanel();
		passwordField = new JPasswordField(10);
		loginText.setForeground(Color.RED);
		
		JLabel passLabel = new JLabel("PASSWORD");
		passLabel.setBounds(10, 10, 80, 25);
		login = new JButton("LOGIN");
		login.addActionListener(this);
		
		passPanel.add(passLabel);
		passPanel.add(passwordField);
		
		this.add(passPanel);
		this.add(login);
		this.add(loginText);
		
		setLayout(new FlowLayout());
		
		setTitle("LOGIN");
		setSize(300, 200);
		
		setVisible(true);
	}

	public boolean getLogin() {
		return isLogin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e1) {
		if(e1.getSource() == login) {
			try {
				if(password.equals(passwordField.getText()))
					isLogin = true;
				else
					isLogin = false;
				if(isLogin) {
					loginText.setText("로그인되었습니다. 안녕하세요.");
					switch(num) {
					case 1:
						new Menu("메뉴 관리");
						break;
					case 2:
						new Sales("매출 관리");
						break;
					case 3:
						new Money("잔돈 관리");
						break;
					}
				}
				else {
					loginText.setText("PASSWORD가 잘못되었습니다.");
				}
				passwordField.setText("");
			} catch (Exception e2) {
				System.out.println("false");
			}
		}
	}
}

@SuppressWarnings("serial")
class Machine extends JFrame implements ActionListener {    // 실행시키면 실행
	String[] menuArr = new String[12];
	
	public Machine() {
		setTitle("Vending Machine");
		createMachineWindow();
		setSize(450,700);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createMachineWindow() {    // 자판기 창 생성
		JMenuBar menuBar = new JMenuBar();
		JMenuItem[] menuItem = new JMenuItem[3];
		String[] itemTitle = {"메뉴 관리", "매출 관리", "잔돈 관리"};
		JMenu screenMenu = new JMenu("관리자");
		Container c = this.getContentPane();
		JPanel panel = new JPanel();
		
		MenuActionListener listener = new MenuActionListener();
		for(int i=0; i<menuItem.length; i++) {
			menuItem[i] = new JMenuItem(itemTitle[i]);
			menuItem[i].addActionListener(listener);
			screenMenu.add(menuItem[i]);
		}
		menuBar.add(screenMenu);
		setJMenuBar(menuBar);
		
		panel.setLayout(null);
		this.printMenu(panel);
		c.add(panel);
	}
	
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e4) {
			String cmd = e4.getActionCommand();
			
			switch(cmd) {
			case "메뉴 관리":
				new Login(1);
				break;
			case "매출 관리":
				new Login(2);
				break;
			case "잔돈 관리":
				new Login(3);
				break;
			}
		}
	}
	
	public void printMenu(JPanel panel) {
		FileReader fr = null;
		BufferedReader br = null;
		JButton[] menuButton = new JButton[12];
		try {
			fr = new FileReader("./menu.dat");
			br = new BufferedReader(fr);
			int row = Integer.parseInt(br.readLine());
			int col = Integer.parseInt(br.readLine());
			for(int i=0; i<row; i++) {
				if(i>11) break;
				for(int j=0; j<col; j++) {
					String data = br.readLine();
					menuArr[j] = data;
				}
				menuButton[i] = new JButton(menuArr[0]);
				menuButton[i].setFont(new Font("돋움체", Font.PLAIN, 12));
				if(i<3) {
					menuButton[i].setBounds(130*i+30, 30, 100, 30);
				}
				else if(i<6) {
					menuButton[i].setBounds(130*(i-3)+30, 130, 100, 30);
				}
				else if(i<9) {
					menuButton[i].setBounds(130*(i-6)+30, 230, 100, 30);
				}
				else {
					menuButton[i].setBounds(130*(i-9)+30, 330, 100, 30);
				}
				panel.add(menuButton[i]);
				menuButton[i].addActionListener(this);
			}
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(NumberFormatException e1) {
			e1.printStackTrace();
		} catch(IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		Sales sales = new Sales();
		
		for(int i=0; i<12; i++) {
			if(ob == menuArr[i]) {
				sales.addSales();
				sales.saveSales();
			}
		}
	}
}