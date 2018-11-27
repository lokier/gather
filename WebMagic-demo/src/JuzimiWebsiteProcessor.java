import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;



/*****
 * ¾ä×ÓÃÔÍøÕ¾×¥È¡
 * @author rao
 *
 */
public class JuzimiWebsiteProcessor implements PageProcessor {
	private static final int MIN_JUZI_LENGTH = 18;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
   

    //true = append file
  
    /*fileWritter.write(data);
    fileWritter.close();*/
    
    private FileWriter mFileWriter;
    private FileWriter getFileWriter() {
    	if(mFileWriter == null) {
    		 try {
    	    File file =new File(this.getClass().getSimpleName());

    	    //if file doesnt exists, then create it
    	    if(!file.exists()){
    	     file.createNewFile();
    	    }
    	    mFileWriter = new FileWriter(file.getName(),false);
    		 }catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	
    	return mFileWriter;
    }

    private boolean parseCase1(Html html,List<Juzi> result) {
    	List lists = html.xpath("div[@class='views-field views-field-phpcode']").nodes();
    	
    	if(lists.size() > 0) {
	    	for(int i = 0 ;i < lists.size();i++) {
	    		Selectable selct = (Selectable)lists.get(i);
	    		
	    		Selectable juziE =selct.xpath("a[@class='xlistju']/text()");
	    		Selectable fromE=selct.xpath("span[@class='views-field-field-oriarticle-value']/text()");
	    		Selectable authorE =selct.xpath("a[@class='views-field-field-oriwriter-value']/text()");
	
	    		String juziText = juziE.toString();
	    		String from = fromE!= null ?fromE.toString() :" ";
	    		String author = authorE!=null?authorE.toString():" ";
	    		
	    		from = from == null ||from.equals("¡¶¡·")? " " : from;
	    		author = author == null ||from.equals("¡¶¡·")? " " : author;
	    		
	    		if(juziText != null && juziText.length() > MIN_JUZI_LENGTH) {
		    		Juzi juzi = new Juzi();
		    		juzi.content =juziText;
		    		juzi.from = from;
		    		juzi.author = author;
		    		result.add(juzi);
	    		}
	    	
	    			    		
	    		}
    		return true;
    	}
    	return false;
    }
    
    private boolean parseCase2(Html html,List<Juzi> result) {
    	List lists = html.xpath("div[@class='views-field-phpcode']").nodes();
    	
    	if(lists.size() > 0) {
	    	for(int i = 0 ;i < lists.size();i++) {
	    		Selectable selct = (Selectable)lists.get(i);
	    		
	    		//selct.
	    		
	    		Selectable juziE =selct.xpath("a[@class='xlistju']/text()");
	    		Selectable fromE=selct.xpath("span[@class='views-field-field-oriarticle-value']/text()");
	    		Selectable authorE =selct.xpath("a[@class='views-field-field-oriwriter-value']/text()");
	    		
	    		String juziText = juziE.toString();
	    		String from = fromE!= null ?fromE.toString() :" ";
	    		String author = authorE!=null?authorE.toString():" ";
	    		
	    		from = from == null ||from.equals("¡¶¡·")? " " : from;
	    		author = author == null ||from.equals("¡¶¡·")? " " : author;
	    		
	    		if(juziText != null && juziText.length() > MIN_JUZI_LENGTH) {
		    		Juzi juzi = new Juzi();
		    		juzi.content =juziText;
		    		juzi.from = from;
		    		juzi.author = author;
		    		result.add(juzi);
	    		}
	    		
	    		
	    		}
	    	if(result.size()>0) {
	    		return true;
	    	}
    	}
    	return false;
    }

    
    @Override
    public void process(Page page) {
       // page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
        //page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-])").all());
        
    	String html = page.getHtml().toString();
    	
    	//System.out.println(html);
   
    	ArrayList<Juzi> rest = new ArrayList<>();
    	
    	if(parseCase1(page.getHtml(), rest)
    			|| parseCase2(page.getHtml(),rest)
    			) {
    		try {
	    		for(Juzi juzi :rest) {
					getFileWriter().write(juzi.content+"\t" + juzi.from + "\t" + juzi.author+"\n");
				
	    		}
    		getFileWriter().flush();
  		   System.out.println("write: size:" + rest.size());

    		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    
    	
    	//System.out.println("list size:" + lists.size());
    	//page.getHtml().x

    /*	page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));*/
    }
    
    
    

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	
    	JuzimiWebsiteProcessor p = new JuzimiWebsiteProcessor();
    	
    	String urlPrfix = "https://www.juzimi.com/tags/%E5%8F%8B%E6%83%85";
    	Spider spider =  Spider.create(p);
    	for(int i = 0; i< 3;i++) {
    		spider.addUrl(urlPrfix+"?page="+i);
    	}

        spider.thread(5).run();
    }
}