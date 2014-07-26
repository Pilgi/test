package s;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class member_Page extends JFrame implements ActionListener{
	basic Base=new basic();
	JPanel member_panel;
	
	member_Page(){
		super();
		
		member_gui();
		
		super.setSize(900,800);
		super.setResizable(true);
	}
	public void member_gui(){
		this.setLayout(null);
		
		member_panel=new JPanel();
		member_panel.setLayout(null);
		member_panel.setBounds(80,150,600,500);
		member_panel.setBackground(Color.white);
		member_panel.setBorder(new LineBorder(Color.black));
		
		add(member_panel);
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
		balance_Page BP=new balance_Page();
		coupon_Page CP=new coupon_Page();
		game_Page GP=new game_Page();
		mainPage main_P=new mainPage();
		
		if(obj.equals(Base.balance)){
			
			System.out.println("right");
			setVisible(false);
			BP.setVisible(true);
			
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
			
			setVisible(true);
			
		}
		else if(obj.equals(Base.a)){
			setVisible(false);
			
			main_P.setVisible(true);
			
		}
	}

}
