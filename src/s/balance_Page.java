package s;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class balance_Page extends JFrame implements ActionListener {
		
	JPanel balance_panel;
	basic Base=new basic();

	coupon_Page CP=new coupon_Page();
	game_Page GP=new game_Page();
	member_Page MP=new member_Page();

	
	balance_Page(){
		super();
		createGui();
		
		//super.setVisible(false);
		super.setSize(900,800);
		super.setResizable(true);
		
	}
	public void createGui(){
		this.setLayout(null);
		
		balance_panel=new JPanel();
		balance_panel.setLayout(null);
		balance_panel.setBounds(80,150,600,500);
		balance_panel.setBackground(Color.white);
		balance_panel.setBorder(new LineBorder(Color.black));
		
		add(balance_panel);
		add(Base.balance);
		Base.balance.addActionListener(this);
		add(Base.coupon);
		Base.coupon.addActionListener(this);
		add(Base.game);
		Base.game.addActionListener(this);
		add(Base.member_list);
		Base.member_list.addActionListener(this);
		add(Base.a);
		Base.a.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj.equals(Base.balance)){
			System.out.println("right");
			setVisible(true);
			
			
		}
		else if(obj.equals(Base.coupon)){
			setVisible(false);
			CP.setVisible(true);
		}
		else if(obj.equals(Base.game)){
			setVisible(false);
			GP.setVisible(true);
		}
		else if(obj.equals(Base.member_list)){
			setVisible(false);
			MP.setVisible(true);
		}// TODO Auto-generated method stub
		else if(obj.equals(Base.a)){
			setVisible(false);
			mainPage main_P=new mainPage();
			main_P.setVisible(true);
			
		}
	}
}
