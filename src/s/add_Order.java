package s;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class add_Order extends JFrame implements ActionListener{

	private static final String send_Button = "Send";
	private static final int NAME_COL_WIDTH=50;
	
	JTextField menu_name,price,table_num;
	JButton send;
	String menu_N, Price, table_Num;
	
	add_Order(){
		super();
		
		super.setVisible(false);
		super.setSize(500,400);
		super.setResizable(true);
		
		createGui();
	}

	public void createGui(){
		this.setLayout(null);
		
		send=new JButton(send_Button);
		
		send.setBounds(250, 300, 150, 50);
		send.setFont(new Font("Dialog",Font.BOLD,30));
		send.setBackground(new Color(255,217,236));
		send.addActionListener(this);
	
		menu_name=new JTextField(NAME_COL_WIDTH);
		menu_name.setBorder(null);
		menu_name.setBounds(50,50,100,30);
		
		price=new JTextField(NAME_COL_WIDTH);
		price.setBorder(null);
		price.setBounds(50,100,100,30);
		
		table_num=new JTextField(NAME_COL_WIDTH);
		table_num.setBorder(null);
		table_num.setBounds(50,150,100,30);
		
		add(send);
		add(menu_name);
		add(price);
		add(table_num);
		
	}
	public void actionPerformed(ActionEvent e) {
		menu_N = menu_name.getText().trim();
		Price = price.getText().trim();
		table_Num = table_num.getText().trim();
		
		Object obj=e.getSource();
		if(obj.equals(send)){// TODO Auto-generated method stub
			mainPage a = new mainPage();
			a.soc();
			
			this.setVisible(false);
		}
	}
}
