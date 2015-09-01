

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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.auctionapplicationIntermed.CrudModel;
import com.example.auctionapplicationIntermed.AuctionItem;
import com.example.auctionapplicationIntermed.DateParser;
import com.example.auctionapplicationIntermed.ItemClientException;
import com.example.auctionapplicationIntermed.ItemNotFoundException;

public class BidServer{

	static HashMap<Long, AuctionItem> itemlist = new HashMap<>();
	File itemdb = new File("/itemDB.txt");
	private static int AvailID = 0;

	//showDirectory
	//File directoy = new File(".");
	//File[] list = directory.listFile();
	//File file = new File("./sdcard", "items");
	//file.createNewFile();
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ItemServiceServer iss = new ItemServiceServer();
		System.out.println("Started Server.");
		try(ServerSocket ss = new ServerSocket(31415)){ // You can reserve any port

			while(true){	
				System.out.println("Welcome to the Server!");

				Socket s = ss.accept();	
				System.out.println("Accepted Connection...");
				InputStream is = s.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				CrudModel command;
				if((command = (CrudModel) ois.readObject()) != null){
					System.out.println(command);

					switch(command.getCommand()){
					case DELETE:

						//ItemServiceClient.deleteItem(Integer.valueOf(command.getArgs()));
						break;
					case ADD:
						System.out.println("ADD");
						Pattern pattern = Pattern.compile(("NAME: ([a-z]*[A-Z]*) DESC: ([a-z]*[A-Z]*) STARTPRICE: ([0-9]*\\.*[0-9]*) STARTDATE: ([0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}) ENDDATE: ([0-9]/[0-9]/[0-9]*)"));
						Matcher matcher = pattern.matcher(command.getArgs());
						// price name desc id start end
						iss.addItem(new AuctionItem( BigDecimal.valueOf(Double.valueOf(matcher.group(2))), matcher.group(0), matcher.group(1), getHighestID(), DateParser.parse(matcher.group(3)), DateParser.parse(matcher.group(4))));
						if(matcher.matches()){
							System.out.println("Adding: " + matcher.group(0));
							//ItemServiceClient.addItem(new Item(organize the groups in the way you want.
							
						}
						break;
					case BID:
						Pattern pattern2 = Pattern.compile(("ID: ([0-9]*) BIDUP: ([0-9]*\\.[0-9]*)"));
						Matcher matcher2 = pattern2.matcher(command.getArgs());
						//the Args should contain the value to increase by.
						//ITemServiceClient.bid(Integer.valueOf(matcher2.group(1)),Long.valueOf(matcher2.group(2)))


						break;
					case UPDATE:
						Pattern pattern3 = Pattern.compile(("ID: ([0-9]*) NAME: ([a-z]*[A-Z]*) DESC: ([a-z]*[A-Z]*) STARTPRICE: ([0-9]*\\.[0-9]*) STARTDATE: ([0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}) ENDDATE: ([0-9]/[0-9]/[0-9]*) IMGREF: (.*)"));
						Matcher matcher3 = pattern3.matcher(command.getArgs());
						if(matcher3.matches()){
							//ItemServiceClient.deleteItem(Integer.valueOf(matcher3.group(1)
							//ItemServiceClient.addItem(new Item(organize the groups in the way you want.
						}
						break;
					case READ:
						System.out.println("READ");
						//hashmap w/ items from search
						//nextID int
						//getItembyID

						ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

						switch(command.getArgs()){
						case "GETHASHMAP":
//							oos.writeObject(getSearchedItems());
							break;
						case "GETNEXTID":
							//sync this
//							synchronize(AvailID) { 
//							oos.writeObject(AvailID++);
//							}
							break;
						case "GETITEMBYID":
							break;
						default:
							//FIx THIS
							oos.writeObject(itemlist);
							System.out.println("the thing wants to read");
							break;
						}
					}
				}
			}
		}
	}
	
	private static int getHighestID(){
		return 9;
	}
	
	public void writeToDB(AuctionItem newitem) throws IOException{
		
		RandomAccessFile raf = new RandomAccessFile(itemdb, "rw");
		FileWriter fw = new FileWriter(itemdb,true);
		BufferedReader br = new BufferedReader(new FileReader(itemdb));
		
		if(br.read() != 'A' && br.read() != 'D' && br.read() != 'B' && br.read() != 'U'){
			raf.seek(0);
			raf.write(br.readLine().getBytes());
			raf.close();
		}

		fw.write("ADD;"+ newitem.getItemID() + ";" + newitem.getName()+";"+newitem.getDescription()+";"+newitem.getBidPrice()+";"+DateParser.format(newitem.getStartDate())+";"
				+DateParser.format(newitem.getEndDate()));
		fw.write("\n");
		fw.close();

	}
	public void readThatLog() throws NumberFormatException, Exception{


		BufferedReader br = new BufferedReader(new FileReader(itemdb));
		br.readLine();
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