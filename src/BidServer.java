

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;










import com.example.auctionapplicationIntermed.AuctionItem;
import com.example.auctionapplicationIntermed.DateParser;
import com.example.auctionapplicationIntermed.ItemClientException;
import com.example.auctionapplicationIntermed.ItemNotFoundException;

public class BidServer{

	static HashMap<Long, AuctionItem> itemlist = new HashMap<>();
	File itemdb = new File("/sdcard/itemDB.txt");

	//showDirectory
	//File directoy = new File(".");
	//File[] list = directory.listFile();
	//File file = new File("./sdcard", "items");
	//file.createNewFile();
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		try(ServerSocket ss = new ServerSocket(31415)){ // You can reserve any port

			while(true){	

				Socket s = ss.accept();	
				System.out.println("Welcome to the Server!");
				InputStream is = s.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String command;
				AuctionItem itemDealtWith;
				while((command = (String) ois.readObject()) != null)
					itemDealtWith = (AuctionItem) ois.readObject(); //ASK PROF ABOUT how to read multiple things

				if(command.equalsIgnoreCase("ADD")){

				}
				else if(command.equalsIgnoreCase("BID")){

				}
				else if(command.equalsIgnoreCase("DELETE")){

				}
				else if(command.equalsIgnoreCase("UPDATE")){

				}



			}



		}}


	public void writeToDB(AuctionItem newitem) throws IOException{
		FileWriter fw = new FileWriter(itemdb,true);
		BufferedReader br = new BufferedReader(new FileReader(itemdb));

		fw.write("ADD;"+ newitem.getItemID() + ";" + newitem.getName()+";"+newitem.getDescription()+";"+newitem.getBidPrice()+";"+DateParser.format(newitem.getStartDate())+";"
				+DateParser.format(newitem.getEndDate()));
		fw.write("\n");
		fw.close();

	}
	public void readThatLog() throws NumberFormatException, Exception{


		BufferedReader br = new BufferedReader(new FileReader(itemdb));
		while(true){
			String line = br.readLine();
			if(line != null){
				String[] lines = line.split(";");
				if(lines[0].equalsIgnoreCase("add")){

					if(!lines[3].equals("null")){
						addItemToList(new AuctionItem(BigDecimal.valueOf((long) Double.parseDouble((lines[4]))), lines[2], lines[3], Integer.parseInt(lines[1]),
								DateParser.parse(lines[5]), DateParser.parse(lines[6])));


					}
					else
						addItemToList(new AuctionItem(BigDecimal.valueOf((long) Double.parseDouble((lines[3]))), lines[2], "", Integer.parseInt(lines[1]),
								DateParser.parse(lines[5]), DateParser.parse(lines[6])));
				}
				else if(lines[0].equalsIgnoreCase("update")){

					if(!lines[3].equals("null")){
						updateItemToList(new AuctionItem(BigDecimal.valueOf((long) Double.parseDouble((lines[4]))), lines[2], lines[3], Integer.parseInt(lines[1]),
								DateParser.parse(lines[5]), DateParser.parse(lines[6])));
					}
					else
						updateItemToList(new AuctionItem(BigDecimal.valueOf((long) Double.parseDouble((lines[4]))), lines[2], "", Integer.parseInt(lines[1]),
								DateParser.parse(lines[5]), DateParser.parse(lines[6])));
				}
				else if(lines[0].equalsIgnoreCase("delete")){

					deleteItemInList(Long.valueOf(lines[1]));
				}
				else if(lines[0].equalsIgnoreCase("bid")){
					System.out.println("ItemBidded");

					itemBid(Long.valueOf(lines[1]), BigDecimal.valueOf((long) Double.parseDouble((lines[2]))));

				}

				//ADD,1,Arush,<description>,0.01,August 18, 2015,August 25, 2015
				//call add item, also	 make the add item method reg ex through the wierd line in DBClient

			}
			else
				break;
		}
		br.close();

	}

	private void updateItemToList(AuctionItem auctionItem) {
		itemlist.put((long) auctionItem.getItemID(), auctionItem);

	}
	private void deleteItemInList(Long valueOf) throws ItemNotFoundException {

		if(itemlist.get(valueOf)==null){
			//null because log is ran each time the window is created
			throw new ItemNotFoundException("No Item with the selected id!?");	
		}
		itemlist.remove(valueOf);

	}
	private void itemBid(Long id, BigDecimal bidIncrease) throws ItemClientException {


		if(itemlist.get(id).getEndDate().after(new Date())){

			itemlist.get(id).setBidPrice(itemlist.get(id).getBidPrice().add(bidIncrease));

		}
		else
			throw new ItemClientException("Can't bid on this time");


	}
	private void addItemToList(AuctionItem auctionItem) {
		itemlist.put((long)auctionItem.getItemID(), auctionItem);

	}
	public void updateLine(AuctionItem newitem) throws IOException{
		FileWriter fw = new FileWriter(itemdb,true);
		BufferedReader br = new BufferedReader(new FileReader(itemdb));

		if(newitem.getDescription().isEmpty()){
			fw.write("UPDATE;"+ newitem.getItemID() + ";" + newitem.getName()+";"+ "null" + ";"+newitem.getBidPrice()+";"+DateParser.format(newitem.getStartDate())+";"
					+DateParser.format(newitem.getEndDate()));
		}
		else{
			fw.write("UPDATE;"+ newitem.getItemID() + ";" + newitem.getName()+";"+newitem.getDescription()+";"+newitem.getBidPrice()+";"+DateParser.format(newitem.getStartDate())+";"
					+DateParser.format(newitem.getEndDate()));
		}
		fw.write("\n");
		fw.close();
		br.close();
	}

	public void deleteLine(long l) throws IOException {
		//take the long, check the line with the id that equals long, and replace line some how...

		BufferedReader br = new BufferedReader(new FileReader(itemdb));
		FileWriter fw = new FileWriter(itemdb,true);


		fw.write("DELETE;"+l);
		fw.write("\n");
		fw.close();
		br.close();

	}
	public void bidLine(long id, BigDecimal bidIncrease) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(itemdb));
		FileWriter fw = new FileWriter(itemdb,true);
		fw.write("BID;"+id+";"+bidIncrease);
		fw.write("\n");
		fw.close();
		br.close();

	}




}