
package com.juzicool.gather.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.juzicool.gather.BasePageProcessor;
import com.juzicool.gather.Juzi;

import org.apache.log4j.PropertyConfigurator;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class JuzimiProcessor extends BasePageProcessor {

	private boolean parseCase1(Html html,List<Juzi> result) {
		List lists = html.xpath("div[@class='views-field views-field-phpcode']").nodes();

		if(lists.size() > 0) {
			for(int i = 0 ;i < lists.size();i++) {
				Selectable selct = (Selectable)lists.get(i);

				Selectable juziE =selct.xpath("a[@class='xlistju']/text()");
				Selectable fromE=selct.xpath("span[@class='views-field-field-oriarticle-value']/text()");
				Selectable authorE =selct.xpath("a[@class='views-field-field-oriwriter-value']/text()");

				Juzi juzi = new Juzi();
				juzi.content =juziE.toString();
				juzi.from = fromE.toString();
				juzi.author = authorE.toString();

				write(juzi);


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

				Selectable juziE =selct.xpath("a[@class='xlistju']/text()");
				Selectable fromE=selct.xpath("span[@class='views-field-field-oriarticle-value']/text()");
				Selectable authorE =selct.xpath("a[@class='views-field-field-oriwriter-value']/text()");

				Juzi juzi = new Juzi();
				juzi.content =juziE.toString();
				juzi.from = fromE.toString();
				juzi.author = authorE.toString();

				write(juzi);


			}
			if(result.size()>0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void process(Page page) {
		String html = page.getHtml().toString();

		ArrayList<Juzi> rest = new ArrayList<>();
		if(parseCase1(page.getHtml(), rest)
				|| parseCase2(page.getHtml(),rest)
				) {
		}
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("E:\\destop-worksapce\\gather\\WebMagic-demo\\src\\log4j.properties");

		
		HttpClientDownloader downloader = new HttpClientDownloader();
		
		final Proxy proxy = new Proxy("dev-proxy.oa.com", 8080);
		downloader.setProxyProvider(new ProxyProvider() {
			
			
			@Override
			public void returnProxy(Proxy proxy, Page page, Task task) {
				
			}
			
			@Override
			public Proxy getProxy(Task task) {
				return proxy;
			}
		});
		
		JuzimiProcessor p = new JuzimiProcessor();

		String urlPrfix = "https://www.juzimi.com/tags/%E5%8F%8B%E6%83%85";
		//urlPrfix="https://www.baidu.com";
		Spider spider =  Spider.create(p);
		spider.setDownloader(downloader);
	/*	for(int i = 0; i< 1;i++) {
			spider.addUrl(urlPrfix+"?page="+i);
		}*/
		spider.addUrl("https://www.juzimi.com/tags/%E7%88%B1%E6%83%85");

		spider.thread(5).run();
	}

}
