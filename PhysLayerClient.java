//Joshua Itagaki
//CS 380

import java.net.Socket;
import java.util.Hashtable;
import java.io.InputStream;
import java.io.OutputStream;

public class PhysLayerClient {
	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("codebank.xyz", 38002)){
			System.out.println("Connected to server.");
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			double base = 0.0;
			for(int i = 0; i < 64; i++){
				int received = in.read();
				base += received;
			}
			base = base / 64;
			System.out.println("Baseline established from preamble: " + base);
			Hashtable<String,String> fourToFiveB = new Hashtable<>();
			hTable(fourToFiveB);
			String[] hBytes = new String[64];
			boolean signal = false;
			for(int i = 0; i < hBytes.length; i++){
				String fBits = "";
				for(int j = 0; j < 5; j++){
					boolean currSignal = in.read()>base;
					if (signal == currSignal){
						fBits += "0";
					} else{
						fBits += "1";
					}
					signal = currSignal;
				}
				hBytes[i] = fourToFiveB.get(fBits);
			}
			System.out.print("Received 32 bytes: ");
			byte[] newByte = new byte[32];
			for(int i = 0; i < newByte.length; i++){
				String half1 = hBytes[2*i];
				String half2 = hBytes[2*i + 1];
				System.out.println(Integer.parseInt(half1, 2));
				System.out.println(Integer.parseInt(half2, 2));
				String fullB = half1 + half2;
				newByte[i] = (byte)Integer.parseInt(fullB, 2);
			}
			System.out.println();
			out.write(newByte);
			if(in.read()==1){
				System.out.println("Response is good");
			}
		}
		System.out.println("Disconnected from server.");
	}
	public static void hTable(Hashtable<String,String> table){
		table.put("11110", "0000");
		table.put("01001", "0001");
		table.put("10100", "0010");
		table.put("10101", "0011");
		table.put("01010", "0100");
		table.put("01011", "0101");
		table.put("01110", "0110");
		table.put("01111", "0111");
		table.put("10010", "1000");
		table.put("10011", "1001");
		table.put("10110", "1010");
		table.put("10111", "1011");
		table.put("11010", "1100");
		table.put("11011", "1101");
		table.put("11100", "1110");
		table.put("11101", "1111");
	}
}
