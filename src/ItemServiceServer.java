import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import com.example.auctionapplication.search.AndPredicate;
import com.example.auctionapplication.search.CollectionUtils;
import com.example.auctionapplication.search.Contain;
import com.example.auctionapplication.search.OrPredicate;
import com.example.auctionapplication.search.Predicate;
import com.example.auctionapplicationIntermed.AuctionItem;


public class ItemServiceServer {
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
