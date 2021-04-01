import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	public MyLinkedList<Genre> genreList;
    public MovieDB() {
    	genreList = new MyLinkedList<Genre>();
    }

    public void insert(MovieDBItem item) {
    	
    	Genre nowGenre = new Genre(item.genre);
    	boolean exist = false;
    	
    	// DB에 같은 장르가 있는 경우
    	for(Genre gen : genreList) {
    		if (gen.item.equals(nowGenre.item)) {
    			exist = true;
    			gen.movieList.add(item);
    		}
    	}
    	// DB에 같은 장르가 없는 경우
    	if (!exist) {
    		genreList.add(nowGenre);
    		this.insert(item);
    	}
    	
    }

    public void delete(MovieDBItem item) {
    	
    	String genreString = item.genre;
    	String titleString = item.title;
    	for(Genre gen : genreList) {
    		if (gen.item.equals(genreString)) {
    			MyLinkedList<MovieDBItem> nowList = gen.movieList;
    			Node<MovieDBItem> now = nowList.head;
    			Node<MovieDBItem> prev;
    			while(now.next != null) {
    				prev = now;
    				now = now.next;
    				if(now.item.title.equals(titleString)) {
    					prev.next = now.next;
    					nowList.numItems --;
    				}
    			}
    		}
    	}
    	
    }

    public MyLinkedList<MovieDBItem> search(String term) {
    	
    	MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
    	
    	for(Genre gen : genreList) {
    		MyLinkedList<MovieDBItem> nowList = gen.movieList;
    		for(MovieDBItem nowItem : nowList) {
    			if (nowItem.title.contains(term)){
    				results.add(new MovieDBItem(nowItem.genre, nowItem.title));
    			}
    		}
    	}
    	
        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        
    	MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
    	for(Genre gen : genreList) {
    		
    		MyLinkedList<MovieDBItem> nowList = gen.movieList;
    		
    		for(MovieDBItem nowItem: nowList) {
				results.add(new MovieDBItem(nowItem.genre, nowItem.title));
    		}
    	}
    	
    	
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	public MyLinkedList<MovieDBItem> movieList;
	
	public Genre(String name) {
		super(name);
		movieList = new MyLinkedList<MovieDBItem>();
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.item.compareTo(o.item);
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object o) {
		Genre other = (Genre) o;
		return this.item.equals(other.item);
	}
}

