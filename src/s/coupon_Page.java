package s;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class coupon_Page extends JFrame implements ActionListener{
	basic Base=new basic();
	JPanel coupon_panel;
	
	coupon_Page(){
		super();
		
		coupon_gui();
		
		super.setSize(900,800);
		super.setResizable(true);
	}
	public void coupon_gui(){
		this.setLayout(null);
		
		coupon_panel=new JPanel();
		coupon_panel.setLayout(null);
		coupon_panel.setBounds(80,150,600,500);
		coupon_panel.setBackground(Color.white);
		coupon_panel.setBorder(new LineBorder(Color.black));
		
		add(coupon_panel);
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
			balance_Page BP=new balance_Page();
			System.out.println("right");
			setVisible(false);
			BP.setVisible(true);
			
		}
		else if(obj.equals(Base.coupon)){
			
			setVisible(true);
			
		}
		else if(obj.equals(Base.game)){
			game_Page GP=new game_Page();
			setVisible(false);
			GP.setVisible(true);
			
		}
		else if(obj.equals(Base.member_list)){
			member_Page MP=new member_Page();
			setVisible(false);
			MP.setVisible(true);
		}
		else if(obj.equals(Base.a)){
			setVisible(false);
			mainPage main_P=new mainPage();
			main_P.setVisible(true);
			
		}
	}
}
