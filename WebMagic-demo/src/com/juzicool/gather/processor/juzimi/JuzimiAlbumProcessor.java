
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
	protected int minJuziLength() {
		return 10;
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
			juzi.applyTags =album.applyTags;
			juzi.tags = album.tags;
			juzi.remark = album.remark;
		}

		return juzi;
	}

	public void addAlbum(JuziAlbum album){
		mMap.put(album.url,album);
	}


	public static void main(String[] args) {

		Gloabal.beforeMain();

		JuzimiAlbumProcessor p = new JuzimiAlbumProcessor();

		Spider spider =  Spider.create(p);

		p.addAlbum(JuziAlbum.build().setUrl("https://www.juzimi.com/album/2364").setPageSize(2).setCategoy("感悟").setRemark("美好,难过，或暂，长久,难忘").setTags("唯美").setApplyTags("写作"));
		
		
		for(JuziAlbum album : p.mMap.values()) {
			for(String url:album.toUrls()) {
				spider.addUrl(url);
			}
		}
		
		//spider.addUrl("https://www.juzimi.com/album/2364?page=1");  //优美的句子,美好,难过，或暂，长久,难忘

		spider.thread(5).run();
	}



	private static class JuziAlbum{
		public String url;
		public int pageSize;

		public String categoy;
		public String remark; //点评
		public String tags; //鉴赏标签
		public String applyTags; //应用标签
		
		public static JuziAlbum build() {
			return new JuziAlbum();
		}

		public JuziAlbum setUrl(String url) {
			this.url = url;
			return this;
		}

		public JuziAlbum setPageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}



		public JuziAlbum setCategoy(String categoy) {
			this.categoy = categoy;
			return this;
		}

	

		public JuziAlbum setRemark(String remark) {
			this.remark = remark;
			return this;
		}

	

		public JuziAlbum setTags(String tags) {
			this.tags = tags;
			return this;
		}

	

		public JuziAlbum setApplyTags(String applyTags) {
			this.applyTags = applyTags;
			return this;
		}
		
		public List<String> toUrls(){
			ArrayList<String> list = new ArrayList<>();
			
			for(int i = 0; i < this.pageSize;i++) {
				list.add(this.url+"?page=" + i);
			}
			
			return list;
		}


	}

}
