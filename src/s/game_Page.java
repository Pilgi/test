package s;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class game_Page extends JFrame implements ActionListener{
	JPanel game_panel;
	basic Base=new basic();
	
	game_Page(){
		super();
		
		game_gui();
		
		super.setSize(900,800);
		super.setResizable(true);
	}
	public void game_gui(){
		this.setLayout(null);
		
		game_panel=new JPanel();
		game_panel.setLayout(null);
		game_panel.setBounds(80,150,600,500);
		game_panel.setBackground(Color.white);
		game_panel.setBorder(new LineBorder(Color.black));
		
		add(game_panel);
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
			coupon_Page CP=new coupon_Page();
			setVisible(false);
			CP.setVisible(true);
		}
		else if(obj.equals(Base.game)){
			
			setVisible(true);
			
		}
		else if(obj.equals(Base.member_list)){
			member_Page MP=new member_Page();
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
