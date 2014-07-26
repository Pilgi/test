package s;


/*�������� �޾Ƽ� ���� �� �� �ֹ� ����̶� �뷡*/

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import s.data.data_structure;

class mainPage extends JFrame implements ActionListener{//�α��� �� ���� ȭ��

	private static final String delete_Button="DELETE";
	private static final String add_Button="ADD";
	
	JList order2;
	JPanel order_list,song;
	JLabel order;
	JButton delete, add;
	basic Base=new basic();
	TextArea recv;
	
	balance_Page BP=new balance_Page();
	coupon_Page CP=new coupon_Page();
	game_Page GP=new game_Page();
	member_Page MP=new member_Page();
	

	public  mainPage(){
		super.setVisible(false);
		super.setSize(900,800);
		
		super.setResizable(true);
		
		this.setLayout(null);
		
		order_list=new JPanel();
		order_list.setLayout(null);
		order_list.setBounds(80,150,400,500);
		order_list.setBackground(Color.white);
		order_list.setBorder(new LineBorder(Color.black));
		
		song=new JPanel();
		song.setLayout(null);
		song.setBounds(500,150,250,500);
		song.setBackground(Color.white);
		song.setBorder(new LineBorder(Color.black));
		
/*		add = new JButton(add_Button);
		add.setBounds(30, 750, 100, 30);
		add.setFont(new Font("Dialog",Font.BOLD,15));
		add.setBackground(new Color(103,153,255));
		
		delete = new JButton(delete_Button);
		delete.setBounds(150, 750, 100, 30);
		delete.setFont(new Font("Dialog",Font.BOLD,15));
		delete.setBackground(new Color(103,153,255));
		
	*/	
		
		recv = new TextArea();
		recv.setBounds(100, 200, 300, 400);
		recv.setBackground(Color.red);
		
		//add(recv);

		
		add(song);
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
	
	//	add(order_list);

		}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj=e.getSource();
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
			setVisible(false);
			MP.setVisible(true);
		}
	}
	public void soc(){	
		String hostname = "localhost";
		int port = 6795;
		int i=0;
		ObjectOutputStream ss = null;
	     
        Socket socket = null;  
        
        ObjectInputStream ois = null;
        BufferedReader read = null;
     
    	
     try {
            socket = new Socket(hostname, port);
           
            ss=new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            read = new BufferedReader(new InputStreamReader(System.in));
        
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }
	
	
	if (socket == null ) {
	    System.err.println( "error" );
	    return;
	}

	
	
	try {
			Scanner keyboard = new Scanner(System.in);
			data k = new data();
		    String a = null;
		    String b;
		    List<data.data_structure> kkk = new ArrayList<data.data_structure>();
		    int j = 0;
		for(i = 0; i<10; i++){		
				
		/*		System.out.print( "����� ���� �Է�:" );
				b = keyboard.next();
				
				data.data_structure ab = k.new data_structure(a,b);
				kkk.add(ab);
				k.purpose = a;
				k.content = kkk;
				ss.reset();
				
				ss.writeObject(k);
				ss.flush();
				
		*/		
				k = (data)ois.readObject();
			
			
				order = new JLabel(k.content.toString());
				order.setBounds(100, 200,300,400+j);
				order.setBackground(Color.red);
				order.setFont(new Font("Dialog",Font.BOLD,15));
				
			
				
				add(order);
			//	data.data_structure abc = k.new data_structure(k.purpose,k.content.);
				
				//for(data.data_structure abc : k.content){
				//	recv.append(k.toString());
					
			//	}
	
				j = j+30;
	   
		}	
		    ois.close();
		    ss.close();
		    socket.close();    
		    
		    
		} catch (UnknownHostException e) {
		    System.err.println("Trying to connect to unknown host: " + e);
		} catch (IOException e) {
		    System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
public static void main(String []args){
		
		new mainPage().soc();
	}

}


