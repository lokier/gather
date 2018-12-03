
package com.juzicool.gather.processor.juzimi;

import com.juzicool.gather.Gloabal;
import com.juzicool.gather.Juzi;
import com.juzicool.gather.utils.SelectableUtls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class JuzimiAlbumProcessor extends JuzimiProcessor {

	private HashMap<String,JuziAlbum> mMap = new HashMap<>();

	@Override
	protected String getFileName(){
		return "句子迷句集"+EXCEL_XLS;
	}

	@Override
	protected Juzi crateJuzi(String url){
		Juzi juzi = new Juzi();

		int index = url.indexOf("?");
		if(index!=-1){
			url = url.substring(0,index);
		}

		JuziAlbum album = mMap.get(url);
		if(album!= null){
			juzi.category = album.categoy;
		}

		return juzi;
	}

	public void addAlbum(JuziAlbum album){
		mMap.put(album.url,album);
	}


	public static void main(String[] args) {

		Gloabal.beforeMain();

		JuzimiAlbumProcessor p = new JuzimiAlbumProcessor();

		String urlPrfix = "https://www.juzimi.com/tags/%E5%8F%8B%E6%83%85";
		Spider spider =  Spider.create(p);

		spider.addUrl("https://www.juzimi.com/album/2364?page=0");
		spider.addUrl("https://www.juzimi.com/album/2364?page=1");  //优美的句子,美好,难过，或暂，长久,难忘

		spider.thread(5).run();
	}



	private static class JuziAlbum{
		public String url;
		public int pageSize;

		public String categoy;
		public String remark; //点评
		public String tags; //鉴赏标签
		public String applyTags; //应用标签



	}

}
