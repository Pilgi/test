package s;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class login extends JFrame implements ActionListener{//로그인 처음 화면
	private static final String user_id="Username";
	private static final String user_pw="Password";
	private static final String OK_button="LOGIN";
	JButton ok;
	private JLabel id_label,pw_label,re_input;
	JTextField id_text;
	JPasswordField pw_text;
	private static final int ID_COL_WIDTH=50;
	private static final int PW_COL_WIDTH=50;  
	
	String id,pw;
	mainPage m=new mainPage();
	balance_Page bp=new balance_Page();
	
	login(){

		super();

		
		createGui();
		start();
		
		super.setVisible(true);
		super.setSize(900,800);
		super.setResizable(true);
		
		
	}
	
	public void createGui(){
		this.setLayout(null);
		
		ok=new JButton(OK_button);
		
		ok.setBounds(400, 400, 150, 50);
		ok.setFont(new Font("Dialog",Font.BOLD,30));
		ok.setBackground(new Color(255,217,236));
		ok.addActionListener(this);
		
		id_label=new JLabel(user_id);
		id_label.setBorder(null);
		id_label.setFont(new Font("Dialog", Font.BOLD, 20));
		id_label.setBounds(350, 250, 100, 30);
		
		pw_label=new JLabel(user_pw);
		pw_label.setBorder(null);
		pw_label.setFont(new Font("Dialog", Font.BOLD, 20));
		pw_label.setBounds(350, 300, 100, 30);
		
		id_text=new JTextField(ID_COL_WIDTH);
		id_text.setBorder(null);
		id_text.setBounds(500,250,100,30);
		
		pw_text=new JPasswordField(PW_COL_WIDTH);
		pw_text.setBorder(null);
		pw_text.setBounds(500,300,100,30);
		
	
		add(ok);
		add(id_text);
		add(pw_text);
		add(id_label);
		add(pw_label);
	
	}
	
	public void start(){
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	public static void main(String []args){
		
		new login();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj=e.getSource();
		if(obj.equals(ok)){
			
			logincheck();//로그인 ㅣ체크
			
			//접속된 창
			
			
		}
		
	}
	void logincheck(){
		id=id_text.getText().trim();
		pw=pw_text.getText().trim();
		
		try{
			if(id.equals("root") && pw.equals("1234")){
				
				setVisible(false);
			
				m.setVisible(true);
				//m.soc();
			}
			else{//다시 입력하라는 문구 나타내기 
				textClear();


				}
			
		}catch(Exception a){
			a.printStackTrace();
		}
	}//로그인 체크
	
	void textClear(){
		id_text.setText("");
		pw_text.setText("");
	}
}
