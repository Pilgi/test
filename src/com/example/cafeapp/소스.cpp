#include <iostream>
#include <string>
#include <vector>
#include <iterator>

#include "winsock2.h"
#pragma comment(lib, "ws2_32.lib")

#define DESIRED_WINSOCK_VERSION        0x0202
#define MINIMUM_WINSOCK_VERSION        0x0001

#define MAXBUF 1024
using namespace std;
/*
user define struct
java�� dataŬ������ �����ϱ� ���� ����
*/

class node{
private:
	string type;
	string value;
public:
	node(string t, string v)
	{
		type = t;
		value = v;
	}
	string getType()
	{
		return type;
	};
	string getValue()
	{
		return value;
	};
	string toString(){
		return "type :" + type + "__ value :" + value;
	};
};
class Data{
private:
	string purpose;
	vector<node> content;
public:
	string getPurpose()
	{
		return purpose;
	};
	node getNode(int idx)
	{
		return content.at(idx);
	};
	void addNode(string type, string value)
	{
		node temp(type, value);
		content.push_back(temp);
	};
	void deleteLastNode()
	{
		content.pop_back();
	}
	Data(string pp)
	{
		purpose = pp;
	};
	int getSize()
	{
		return content.size();
	}
	string toString()
	{
		return "data -- purpose :" + purpose;
	}
};

boolean DataToStream(SOCKET m_client_socket, Data* data);
Data StreamToData(SOCKET m_client_socket);
boolean bufferSizeCheck(int * idx, SOCKET m_client_socket, char buffer[MAXBUF]);
int byte2Int(char src[], int *idx);

 // ���� ��� �ڵ� 
 // http://howspace.kr/gbs/bbs/board.php?bo_table=gr2_10_data&wr_id=108&sfl=&stx=&sst=wr_datetime&sod=asc&sop=and&page=7&nca=
int main()
{
	SOCKET m_client_socket;
	Data * temp;
	WSADATA wsadata;
	struct sockaddr_in server_addr;
	char buffer[MAXBUF] = {0,};

	// ������ �ʱ�ȭ
	if (!WSAStartup(DESIRED_WINSOCK_VERSION, &wsadata)){
		if (wsadata.wVersion < MINIMUM_WINSOCK_VERSION){
			WSACleanup();
			exit(1);
		}
	}

	// ������ ����
	m_client_socket = socket(PF_INET, SOCK_STREAM, 0);
	if (m_client_socket == INVALID_SOCKET){
		printf("socket error : ");
		WSACleanup();
		exit(1);
	}

	// ������ ������ �ּ� ����
	memset(&server_addr, 0, sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	server_addr.sin_port = htons(6799);

	// ������ �ּҷ� ����
	if (connect(m_client_socket, (LPSOCKADDR)&server_addr, sizeof(server_addr)) != 0){
		printf("connect error : ");
		exit(1);
	}
	printf("connect\n");
	// ������ ó��

	temp = new Data("SHOW MENU");
	temp->addNode("category", "Ŀ��");
//	temp->addNode("�׽�Ʈ2", "testvalue2");
	DataToStream(m_client_socket, temp);

	Data recv_data = StreamToData(m_client_socket);
	//recv(m_client_socket, buffer, MAXBUF, 0);
	printf("data �޾ƿ�\n \s", buffer);
	/*/
	if (m_client_socket != INVALID_SOCKET){
		printf("=== ���� �Ϸ� ===");
		// Ŭ���̾�Ʈ�� buf�� �ִ� ��I like you!" ���ڿ� ����
		if (send(m_client_socket, buffer, MAXBUF, 0) <= 0) printf("send error : ");

	}
	*/
	 
	// ������ ����
  	closesocket(m_client_socket);

	// �޾ƿ� ���ڿ��� ȭ�鿡 ���
	printf("\nrecv : %s\n\n", buffer);
	system("pause");
	return 0;
}

//src �κ��� integer ���� �����´�. 
int byte2Int(char src[] , int *idx)
{
	int size = sizeof(int);
	int i;
	int temp;
	for (i = 0; i < size; i++)
	{
		temp =  (src[*idx+i] & 0xFF)<<((size-i-1)*8);
	}
	*idx += size;
	return temp;
}

//dest idx ���� integer ���� ����
int int2byte(int number, char  dest[], int *idx)
{
	int size = sizeof(int);
	int i;
	char* temp;
	temp = (char*)malloc(sizeof(char)*size/sizeof(char));
	for (i = 0; i < size; i++)
		temp[i] = char(number >> ((size - i -1) * 8));
	memcpy_s(dest + *idx, sizeof(temp), temp, sizeof(temp));
	*idx += sizeof(temp);
	free(temp);
	return 0;

}
void string2byte(string st, char dest[], int *idx)
{
	int size = st.length();
	char * temp;
	int i;
	temp = (char*)malloc(size);
	for (i = 0; i < size; i++)
	{
		temp[i] = st.at(i);
	}
	memcpy_s(dest + *idx, size, temp, size);
	*idx += size;
	free(temp);
}
string byte2string(char src[], int size, int *idx)
{
	char* temp;
	temp = new char[size+1];
	for (int i = 0; i < size; i++)
	{
		temp[i] = src[*idx + i];
	}
	temp[size] = '\0';
	string str = temp;
	*idx += size;
	return str;
}
/*
buffer ����� �Ѿ�� ��� ó���ϱ�

ó���� S������ �κ� ������µ� S����� �߰��ϱ�

*/

boolean DataToStream(SOCKET m_client_socket, Data* data)
{
	char buffer[MAXBUF] = {};
	char temp[4];
	int *idx;
	int num_of_node = data->getSize();

	idx = new int(1);

	strcat_s(buffer, "S");
	int2byte(1, buffer, idx);
	int2byte(data->getPurpose().length(), buffer, idx);
  	string2byte(data->getPurpose(), buffer, idx);
	for (int i = 0; i < num_of_node; i++)
	{
		if (*idx + 8 >= MAXBUF && bufferSizeCheck(idx, m_client_socket, buffer) == false)
			break;
		//node�� ���۵��� �˷���
		int2byte(2, buffer, idx);
		int2byte(data->getSize(), buffer, idx);
		//type�� ���۵��� �˷���
		int2byte(3, buffer, idx);		
		if (*idx + 8 >= MAXBUF && bufferSizeCheck(idx, m_client_socket, buffer) == false)
			break;
		//type string �� byte�� ���� ����
		int2byte(data->getNode(i).getType().length(), buffer, idx);
		if (*idx + 8 >= MAXBUF && bufferSizeCheck(idx, m_client_socket, buffer) == false)
			break;
		string2byte(data->getNode(i).getType(), buffer, idx);
		if (*idx + 8 >= MAXBUF && bufferSizeCheck(idx, m_client_socket, buffer) == false)
			break;
		//value ���۵��� �˷���
		int2byte(4, buffer, idx);
		//value ����
		int2byte(data->getNode(i).getValue().length(), buffer, idx);
		if (*idx + 8 >= MAXBUF && bufferSizeCheck(idx, m_client_socket, buffer) == false)
			break;
		string2byte(data->getNode(i).getValue(), buffer, idx);
	}
	string2byte("E", buffer, idx);
	if (send(m_client_socket, buffer, MAXBUF, 0) <= 0)
	{
		printf("send error : ");
		return false;
	}
//	memcpy_s(buffer + idx, data->getPurpose().length(), &(data->getPurpose()), data->getPurpose().length());

	printf("%d", strlen(buffer));

	if (m_client_socket == INVALID_SOCKET){
		printf("INVALID SOCKET �Դϴ�.");
		return false;
	}
	//client ���� data structure �� buufer ���·� ���� ����

	printf("\nrecvaaa : %s\n\n", buffer);

	
	//if (send(m_client_socket, buffer, MAXBUF, 0) <= 0) printf("send error : ");
	
}

Data StreamToData(SOCKET m_client_socket)
{
	char buffer[MAXBUF] = {};
	char protocol_id;
	int num_of_node = 0;
	int *idx , string_size,string_type;
	string temp_string;
	string temp_type, temp_value;
	Data* return_data;
	idx = new int(0);
	recv(m_client_socket, buffer, MAXBUF, 0);
	protocol_id = buffer[(*idx)++];
	if (protocol_id != 'S')
		return *new Data("error");
	string_type = byte2Int(buffer, idx);
	string_size = byte2Int(buffer, idx);
	if (string_type == 1)
		temp_string =byte2string(buffer, string_size, idx);
	else
		return *new Data("error");
	return_data = new Data(temp_string);
	string_type = byte2Int(buffer, idx);
	num_of_node = byte2Int(buffer, idx);
	if (string_type != 2)
		return *new Data("error");
	for (int i = 0; i < num_of_node;)
	{
		string_type = byte2Int(buffer, idx);
		string_size = byte2Int(buffer, idx);
		switch (string_type)
		{
		case 2:
			continue;
		case 3:
			temp_type = byte2string(buffer, string_size, idx);
			continue;
		case 4:
			temp_value = byte2string(buffer, string_size, idx);
			return_data->addNode(temp_type, temp_value);
			i++;
			break;
		default:
			return *new Data("error");			
		}
		cout << i << "��° data" << ", type : " << temp_type << ", string : " << temp_value << "\n";
	}
	cout << "type : " << string_type << ", string_size : " << string_size << ", string : " << temp_string << "\n";

	return *return_data;	
}
boolean bufferSizeCheck(int * idx, SOCKET m_client_socket, char* buffer)
{
	if (send(m_client_socket, buffer, MAXBUF, 0) <= 0)
	{
		printf("send error : ");
		return false;
	}
	else
	{
		for (int i = 0; i < MAXBUF; i++)
			buffer[i] = 0;
		buffer[0] = 'C';
		*idx = 1;
		return true;
	}
	return true;
}
