package com.example.androidlabs.businessLogic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;

import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.example.androidlabs.dataAccess.roomdDb.entities.UserAdditionalInfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.example.androidlabs.MainActivity.currentUser;

public class NewsLoader extends AsyncTask<Void, Void, Void> {

    public ArrayList<News> getFeedItems() {
        return feedItems;
    }
    private AppDatabase appAdditionalInfoDatabase;

    private ArrayList<News> feedItems;
    private String address;

    public NewsLoader(String address) {
        this.address = address;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            ProcessXML(getDocument());
        } catch (Exception ex){
            feedItems = null;
        }
        return null;
    }

    private void ProcessXML(Document document) {
        if (document != null) {
            feedItems = new ArrayList<News>();
            Element root=document.getDocumentElement();
            Node channel=root.getChildNodes().item(0);
            NodeList items=channel.getChildNodes();
            for(int i=0;i<items.getLength();i++) {
                Node currentChild = items.item(i);

                if (currentChild.getNodeName().equalsIgnoreCase("item")) {
                    News item = new News();
                    NodeList itemchilds = currentChild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node currenet = itemchilds.item(j);
                        if(currenet.getNodeName().equalsIgnoreCase("title"))
                        {
                            item.title = currenet.getTextContent();
                        }
                        else if(currenet.getNodeName().equalsIgnoreCase("description"))
                        {
                            item.description = currenet.getTextContent();
                        }
                        else if(currenet.getNodeName().equalsIgnoreCase("pubDate"))
                        {
                            item.pubDate = currenet.getTextContent();
                        }

                        else if(currenet.getNodeName().equalsIgnoreCase("link"))
                        {
                            item.link = currenet.getTextContent();
                        }
                        else if(currenet.getNodeName().equalsIgnoreCase("media:content"))
                        {
                            String url=currenet.getAttributes().item(0).getTextContent();
                            item.thumbnailUrl = url;
                        }
                        else if(currenet.getNodeName().equalsIgnoreCase("media:thumbnail"))
                        {
                            String url=currenet.getAttributes().item(0).getTextContent();
                            item.thumbnailUrl = url;
                        }
                    }
                    feedItems.add(item);

                }
            }
        }

    }

    private Document getDocument(){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            return builder.parse(inputStream);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute(){
        onLoadingNews.onLoadingStart();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        onLoadingNews.onLoadingEnd();
    }

    private OnLoadingNews onLoadingNews;

    public interface OnLoadingNews{
        void onLoadingStart();
        void onLoadingEnd();
    }

    public void setOnLoadingNews(OnLoadingNews onLoadingNews){
        this.onLoadingNews = onLoadingNews;
    }

    public void updateUserRssUrl(String rssNewsUrl){
        UserAdditionalInfo userAdditionalInfo = appAdditionalInfoDatabase.userDAdditionalInfo()
                .getUserAdditionalInfo(currentUser.uid);
        userAdditionalInfo.rssNewsUrl = rssNewsUrl;
        appAdditionalInfoDatabase.userDAdditionalInfo().updateUserAdditionalInfo(userAdditionalInfo);
        currentUser.rssNewsUrl = rssNewsUrl;
    }
}
