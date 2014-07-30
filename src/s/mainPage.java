package s;




/*서버에서 받아서 나열 된 것 주문 목록이랑 노래*/
//socket client 부분
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


class mainPage extends JFrame implements ActionListener{//로그인 후 메인 화면


	private static final String delete_Button="DELETE";
	private static final String add_Button="ADD";


	String hostname = "localhost";
	int port = 6795;
	int i=0;
	ObjectOutputStream ss = null; 
     
    Socket socket = null;
    data k = new data("why in here");
    
    ObjectInputStream ois = null;
    BufferedReader read = null;


	JPanel order_list,song;
	JLabel order;
	JButton delete, add;
	basic Base=new basic();
	TextArea recv;


	balance_Page BP=new balance_Page();
	coupon_Page CP=new coupon_Page();
	game_Page GP=new game_Page();
	member_Page MP=new member_Page();
	add_Order AO = new add_Order();	


	public  mainPage(){


		super.setVisible(false);
		super.setSize(900,800);


		super.setResizable(true);
		createGui();
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					ois.close();
					ss.close();
				    socket.close(); 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   
				System.exit(0);
			}});
	}
	public void createGui(){
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


		add = new JButton(add_Button);
		add.setBounds(30, 650, 100, 30);
		add.setFont(new Font("Dialog",Font.BOLD,15));
		add.setBackground(new Color(103,153,255));


		delete = new JButton(delete_Button);
		delete.setBounds(150, 650, 100, 30);
		delete.setFont(new Font("Dialog",Font.BOLD,15));
		delete.setBackground(new Color(103,153,255));






		/*recv = new TextArea();	//서버에서 받는 부분을 text area로 할까 해서 해본 것
		recv.setBounds(100, 200, 300, 400);
		recv.setBackground(Color.red);
		
		//add(recv);
*/
		add(add);
		add.addActionListener(this);
		add(delete);
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


		if(obj.equals(add)){
			 AO.setVisible(true);
		} 
	}
	public void soc(){	

		System.out.println("soc시작");

     try {
 		System.out.println("soc시작1");
            socket = new Socket(hostname, port);
     		System.out.println("soc시작2");
     		
            ss=new ObjectOutputStream(socket.getOutputStream());
            read = new BufferedReader(new InputStreamReader(System.in));

        	System.out.println("여기는??");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }




	if (socket == null ) {
	    System.err.println( "error" );
	    return;
	}

	System.out.println("여기는??");




	try {

	    ois = new ObjectInputStream(socket.getInputStream());
		data x = new data("test");
		String a = null;
		List<data.data_structure> kkk = new ArrayList<data.data_structure>();


		    int j = 0;
		    int abc=0;
		while(true){		
			System.out.println(abc++ +"입니다0");
			
			ss.reset();
			ss.writeObject(k);
			ss.flush();

				//주문 내역 서버에서 받는 부분
				x = (data) ois.readObject();;
				a = (String)x.toString();
				System.out.println(a);


				order = new JLabel(x.content.toString());
				order.setBounds(100, 200+j,300,400);
				order.setBackground(Color.red);
				order.setFont(new Font("Dialog",Font.BOLD,15));






				add(order);


				j = j+30;//라벨 위치 바꿀려고 


				//서버로 보내는 부분 추가된 주문 
				AO.menu_N = AO.menu_name.getText().trim();
				AO.Price = AO.price.getText().trim();
				AO.menu_N = AO.menu_name.getText().trim();


				data.data_structure ab = k.new data_structure("menu_name",AO.menu_N);
				data.data_structure ac = k.new data_structure("price",AO.Price);
				data.data_structure ad = k.new data_structure("table_num",AO.table_Num);
				

				kkk.add(ab);
				kkk.add(ac);
				kkk.add(ad);


				k.purpose = "Order_add";
				ss.reset();
				ss.writeObject(k);
				ss.flush();




				if(k.equals("\n")){
					break;
				}
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




}
