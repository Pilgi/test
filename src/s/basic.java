package s;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class basic{
	JButton balance,coupon,game,member_list,a, main_home;
	private static final String balance_Button="선불금";
	private static final String coupon_Button="Coupon";
	private static final String game_Button="GAME";
	private static final String member_Button="회원목록";
	private static final String Main_Home="Main";
	
	basic(){
		super();
		createButton();
		
	}
	
	public void createButton(){
		balance=new JButton(balance_Button);
		balance.setBounds(50, 50, 120, 50);
		balance.setFont(new Font("Dialog",Font.BOLD,20));
		balance.setBackground(new Color(103,153,255));
		
		
		coupon=new JButton(coupon_Button);
		coupon.setBounds(210, 50, 120, 50);
		coupon.setFont(new Font("Dialog",Font.BOLD,20));
		coupon.setBackground(new Color(103,153,255));
		
		game=new JButton(game_Button);
		game.setBounds(370, 50, 120, 50);
		game.setFont(new Font("Dialog",Font.BOLD,20));
		game.setBackground(new Color(103,153,255));
		
		
		member_list=new JButton(member_Button);
		member_list.setBounds(530, 50, 120, 50);
		member_list.setFont(new Font("Dialog",Font.BOLD,20));
		member_list.setBackground(new Color(103,153,255));
		
		
		a=new JButton(Main_Home);
		a.setBounds(690, 50, 120, 50);
		a.setFont(new Font("Dialog",Font.BOLD,20));
		a.setBackground(new Color(103,153,255));
		
	/*	main_home=new JButton(Main_Home);
		main_home.setBounds(690, 50, 120, 50);
		main_home.setFont(new Font("Dialog",Font.BOLD,20));
		main_home.setBackground(new Color(103,153,255));
	*/	
		
	}

}
