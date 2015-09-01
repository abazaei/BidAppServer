import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;




import com.example.auctionapplication.search.AndPredicate;
import com.example.auctionapplication.search.CollectionUtils;
import com.example.auctionapplication.search.Contain;
import com.example.auctionapplication.search.OrPredicate;
import com.example.auctionapplication.search.Predicate;
import com.example.auctionapplicationIntermed.AuctionItem;
import com.example.auctionapplicationIntermed.ItemClientException;
import com.example.auctionapplicationIntermed.ItemNotFoundException;


public class ItemServiceServer {
	
	public void itemBid(Long id, BigDecimal bidIncrease) throws Exception {
		
		if(BidServer.itemlist.get(id).getEndDate().after(new Date())){
			
			BidServer.itemlist.get(id).setBidPrice(BidServer.itemlist.get(id).getBidPrice().add(bidIncrease));
			
		}
		else
			throw new ItemClientException("Can't bid on this time");
		//NOT PROPERLY READING IN BIDS! It is not BIDDING!
	}

	public static void addItem(AuctionItem newitem) throws IOException{
		//dbc.writeToDB(newitem);

		BidServer.itemlist.put((long) newitem.getItemID(), newitem);
	}
	public void deleteItemInList(Long valueOf) throws Exception {

		if(BidServer.itemlist.get(valueOf)==null){
			//null because log is ran each time the window is created
			throw new ItemNotFoundException("No Item with the selected id!?");
			
			
		}


		BidServer.itemlist.remove(valueOf);

	}
	public void updateItemToList(AuctionItem auctionItem) {
		BidServer.itemlist.put((long) auctionItem.getItemID(), auctionItem);
	}
	
	public Collection<AuctionItem> search(String query){

		String [ ] words = query.split(" ");

		Stack <Predicate> P = new Stack<>();
		Stack <String> S = new Stack<>();
		Collection <AuctionItem> i = new ArrayList<>();

		for(String s : words){
			if(s.equals("and")){
				S.push(s);
			}
			else if(s.equals("or")){
				if(S.isEmpty()){
					S.push(s);
				}
				else if(S.peek().equals("and")){
					while(true){
						if(S.isEmpty()){
							break;
						}
						else if(S.peek().equals("and")){
							S.pop();
							if(P.size() >= 2)
								P.push(new AndPredicate(P.pop(),P.pop()));
						}
						else break;
					}
					S.push(s);
				}
				else S.push(s);
			}
			else P.push(new Contain(s));
		}
		while(true){
			if(P.isEmpty()){
				break;
			}
			else if(P.peek().equals("and")){
				S.pop();
				if(P.size() >= 2){

					P.push(new AndPredicate(P.pop(),P.pop()));

				}

			}
			else { 
				if(P.size() >= 2){
					P.push(new OrPredicate(P.pop(),P.pop()));
				}
				else{
					break;
				}
			}
		}


		return (CollectionUtils.filter(BidServer.itemlist.values(), P.pop()));
		//timeCheck(the return) , but I took it out becaues of compiler errors
	}
}


