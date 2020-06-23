package com.yuown.urlpreview.primefaces;

import java.net.URL;

import javax.faces.bean.ManagedBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@ManagedBean
public class LinkPreviewer {

	private String url;
	
	private UrlMeta urlMeta;

	public void getPreview() throws Exception {
		UrlMeta metaData = new UrlMeta();
		System.out.println("Starting Preview: " + url);
		try {
			Document doc = Jsoup.connect(url).get();
			URL aURL = new URL(url);
			
			metaData.setTitle(nullOrValue(getMetaData(doc, "title"), getHtmlTitle(doc, "h1")));
			metaData.setDescription(nullOrValue(getMetaData(doc, "description"), getHtmlTitle(doc, "p")));
			metaData.setDomain(aURL.getHost());
			metaData.setUrl(url);
			metaData.setImage(nullOrValue(getMetaData(doc, "image"), "./images/no-image.png"));
		} catch (Exception e) {
			metaData.setTitle("Not available");
			metaData.setDescription(e.getMessage());
			metaData.setDomain("Not available");
			metaData.setUrl(url);
			metaData.setImage("./images/no-image.png");
		}

		setUrlMeta(metaData);
	}

	private String nullOrValue(String metaData, String htmlTitle) {
		return metaData != null && metaData.trim() != "" ? metaData : htmlTitle;
	}

	private String getMetaData(Document doc, String field) {
		Element tag = doc.select("meta[name=" + field + "]").first();
		if(null == tag) {
			tag = doc.select("meta[name=og:" + field + "]").first();
		}
		if(null == tag) {
			tag = doc.select("meta[name=twitter:" + field + "]").first();
		}
		if(null == tag) {
			tag = doc.select("meta[property=" + field + "]").first();
		}
		if(null == tag) {
			tag = doc.select("meta[property=og:" + field + "]").first();
		}
		if(null == tag) {
			tag = doc.select("meta[property=twitter:" + field + "]").first();
		}
		return tag.attr("content");
	}

	private String getHtmlTitle(Document doc, String tag) {
		return doc.select(tag).first().text();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		System.out.println("Setting URL: "  +url);
	}

	public UrlMeta getUrlMeta() {
		return urlMeta;
	}

	public void setUrlMeta(UrlMeta urlMeta) {
		this.urlMeta = urlMeta;
	}

}
